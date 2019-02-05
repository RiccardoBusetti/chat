package server;

import javafx.util.Pair;
import server.access.AccessHelper;
import server.constants.Constants;
import server.entities.User;
import server.entities.packets.*;
import server.exceptions.UserNotFoundException;
import server.logging.Logger;
import server.packets.PacketsDecoder;
import server.packets.PacketsQueue;
import server.users.OnlineUsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible of handling each connection on a different thread
 * in order to allow for multiple connections.
 */
public class ConnectionHandler implements Runnable {

    private User user;
    private Socket clientSocket;
    private boolean stop = false;

    /* package */ ConnectionHandler(Socket clientSocket) {
        this.user = null;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        handleConnection();
    }

    /**
     * Handles the connection with a specific client on a separate thread.
     */
    private void handleConnection() {
        // We are going to listen for new packets with this top level
        // function that allows us to catch any king of errors during
        // the communication. If there is any kind of error we will
        // disconnect the user. A better behavior needs to be thought and
        // implemented.
        try {
            Logger.logConnection(this, "Handling connection with the client " + clientSocket.getRemoteSocketAddress());

            listen();
        } catch (IOException exc) {
            Logger.logError(this, "An error occurred while handling the connection: " + exc.getMessage() + ", disconnection in progress...");
            disconnectClient();
        }
    }

    /**
     * Listens for the packets coming from the specific client.
     *
     * @throws IOException thrown when an IO problem occurs.
     */
    private void listen() throws IOException {
        // We need to open the input stream of the client in order
        // to be able to read his messages.
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Handles the access, because before being able
        // to send new messages the user needs to be authenticated.
        handleAccess(bufferedReader);

        // After the user is authenticated now we can accept messages.
        handleMessages(bufferedReader);
    }

    private void handleAccess(BufferedReader bufferedReader) throws IOException {
        boolean isAllowed = false;
        PacketsDecoder packetsDecoder = new PacketsDecoder();

        while (!isAllowed && !stop) {
            String inputString = bufferedReader.readLine();

            if (inputString != null) {
                Packet packet = packetsDecoder.decode(inputString);

                // We need to check if the packet is an access packet, because
                // we must perform the login/registration before letting the user
                // send messages.
                if (packet instanceof AccessPacket) {
                    AccessPacket accessPacket = (AccessPacket) packet;
                    AccessResultPacket accessResultPacket;

                    if (accessPacket.getHeaderType() == Packet.HeaderType.LOGIN_DATA) {
                        accessResultPacket = handleLogin(accessPacket);
                    } else if (accessPacket.getHeaderType() == Packet.HeaderType.REGISTER_DATA) {
                        accessResultPacket = handleRegister(accessPacket);
                    } else {
                        sendErrorMessage("Prima di inviare messaggi devi fare l'accesso.");
                        break;
                    }

                    isAllowed = accessResultPacket.isAllowed();

                    // Prepares the result of the access.
                    DispatchablePacket dispatchablePacket = new DispatchablePacket();
                    dispatchablePacket.addRecipientSocket(clientSocket);
                    dispatchablePacket.setPacket(accessResultPacket);

                    // Adds the result message to the queue.
                    PacketsQueue.getInstance().sendPacket(dispatchablePacket);
                } else {
                    sendErrorMessage("Prima di inviare messaggi devi fare l'accesso.");
                }
            } else {
                Logger.logStatus(this, "Received null line from the client, disconnection in progress...");
                disconnectClient();
            }
        }
    }

    private AccessResultPacket handleLogin(AccessPacket accessPacket) {
        Logger.logStatus(this, "Handling login with the client " + clientSocket.getRemoteSocketAddress());

        user = new User();
        user.setUsername(accessPacket.getUsername());
        user.setPassword(accessPacket.getPassword());

        // Preparing the result packet.
        AccessResultPacket accessResultPacket = new AccessResultPacket(true);

        // Performs the login and handles the result specifically.
        switch (AccessHelper.login(user.getUsername(), user.getPassword())) {
            case LOGIN_SUCCESSFUL:
                Logger.logConnection(this, "Login successful for the user " + user.getUsername());
                accessResultPacket.setAllowed(true);

                // Now the user is online, so we will add it to the online users list.
                OnlineUsers.getInstance().addUser(user, clientSocket);
                break;
            case LOGIN_NOT_EXISTING_USER:
            case LOGIN_BLOCKED_USER:
            case LOGIN_WRONG_CREDENTIALS:
            case LOGIN_USER_ALREADY_ONLINE:
            default:
                Logger.logConnection(this, "Login not allowed for the user " + user.getUsername());
                accessResultPacket.setAllowed(false);
                break;
        }

        return accessResultPacket;
    }

    private AccessResultPacket handleRegister(AccessPacket accessPacket) {
        Logger.logStatus(this, "Handling registration with the client " + clientSocket.getRemoteSocketAddress());

        user = new User();
        user.setUsername(accessPacket.getUsername());
        user.setPassword(accessPacket.getPassword());

        // Preparing the result packet.
        AccessResultPacket accessResultPacket = new AccessResultPacket(false);

        // Performs the registration and handles the result specifically.
        switch (AccessHelper.register(accessPacket.getUsername(), accessPacket.getPassword())) {
            case REGISTRATION_SUCCESSFUL:
            case REGISTRATION_ALREADY_EXISTING_USER:
                Logger.logConnection(this, "Registration successful for the user " + user.getUsername());
                accessResultPacket.setAllowed(true);

                // Now the user is online, so we will add it to the online users list.
                OnlineUsers.getInstance().addUser(user, clientSocket);
                break;
            case REGISTRATION_BLOCKED_USER:
            case REGISTRATION_USER_ALREADY_ONLINE:
            default:
                Logger.logConnection(this, "Registration not allowed for the user " + user.getUsername());
                accessResultPacket.setAllowed(false);
                break;
        }

        return accessResultPacket;
    }

    private void handleMessages(BufferedReader bufferedReader) throws IOException {
        PacketsDecoder packetsDecoder = new PacketsDecoder();

        while (!stop) {
            String inputString = bufferedReader.readLine();

            if (inputString != null) {
                Packet packet = packetsDecoder.decode(inputString);

                // We need to check if the packet is a message and depending
                // on its type we are going to handle it differently.
                if (packet instanceof UnicastMessagePacket) {
                    UnicastMessagePacket unicastMessagePacket = (UnicastMessagePacket) packet;

                    Logger.logStatus(this, "Handling unicast message from " + unicastMessagePacket.getSenderUsername() + " to " + unicastMessagePacket.getRecipientUsername() + ".");

                    handleUnicastMessage(unicastMessagePacket);
                } else if (packet instanceof MulticastMessagePacket) {
                    MulticastMessagePacket multicastMessagePacket = (MulticastMessagePacket) packet;

                    Logger.logStatus(this, "Handling multicast message from " + multicastMessagePacket.getSenderUsername() + ".");

                    handleMulticastMessage(multicastMessagePacket);
                } else {
                    sendErrorMessage("Il messaggio da te inviato ha un formato errato, riprova.");
                }
            } else {
                Logger.logStatus(this, "Received null line from the client, disconnection in progress...");
                disconnectClient();
            }
        }
    }

    private void handleUnicastMessage(UnicastMessagePacket unicastMessagePacket) {
        try {
            Pair<User, Socket> recipientUser = OnlineUsers.getInstance().getUserByUsername(unicastMessagePacket.getRecipientUsername());

            // Creating the dispatchable packet in order to send the message
            // to the other client.
            DispatchablePacket dispatchablePacket = new DispatchablePacket();
            dispatchablePacket.addRecipientSocket(recipientUser.getValue());
            dispatchablePacket.setPacket(unicastMessagePacket);

            // Adds the message result to the queue.
            PacketsQueue.getInstance().sendPacket(dispatchablePacket);

            // Notifies the sender that the message has been sent.
            sendMessageResult(true);
        } catch (UserNotFoundException exc) {
            sendMessageResult(false);
        }
    }

    private void handleMulticastMessage(MulticastMessagePacket multicastMessagePacket) {
        // Gets all the users that are currently online.
        List<Pair<User, Socket>> onlineUsers = OnlineUsers.getInstance().getAllUsers();
        List<Socket> recipientsSockets = new ArrayList<>();

        // Creates the list of all recipients.
        for (Pair<User, Socket> onlineUser : onlineUsers) {
            if (clientSocket != onlineUser.getValue())
                recipientsSockets.add(onlineUser.getValue());
        }

        // Checking if there is at least 1 user online because if there aren't
        // users online then the message is not being sent.
        if (recipientsSockets.size() > 0) {
            // Creating the dispatchable packet in order to send the message
            // to the other client.
            DispatchablePacket dispatchablePacket = new DispatchablePacket();
            dispatchablePacket.setRecipientsSockets(recipientsSockets);
            dispatchablePacket.setPacket(multicastMessagePacket);

            // Adds the message result to the queue.
            PacketsQueue.getInstance().sendPacket(dispatchablePacket);

            // Notifies the sender that the message has been sent.
            sendMessageResult(true);
        } else {
            sendMessageResult(false);
        }
    }

    private void sendMessageResult(boolean isReceived) {
        // Building the packet which will notify the client that the
        // message has been received by the server.
        // NB: if the packet is not received we will send a null date.
        MessageResultPacket messageResultPacket = new MessageResultPacket();
        messageResultPacket.setReceiveDate(isReceived ? LocalDateTime.now().toString() : Constants.NO_DATE);

        // Prepares the result of the message.
        DispatchablePacket dispatchablePacket = new DispatchablePacket();
        dispatchablePacket.addRecipientSocket(clientSocket);
        dispatchablePacket.setPacket(messageResultPacket);

        // Adds the message result to the queue.
        PacketsQueue.getInstance().sendPacket(dispatchablePacket);
    }

    private void sendErrorMessage(String errorMessage) {
        ErrorPacket errorPacket = new ErrorPacket(errorMessage);

        // Prepares the result of the access.
        DispatchablePacket dispatchablePacket = new DispatchablePacket();
        dispatchablePacket.addRecipientSocket(clientSocket);
        dispatchablePacket.setPacket(errorPacket);

        // Adds the error message to the queue.
        PacketsQueue.getInstance().sendPacket(dispatchablePacket);
    }

    private void disconnectClient() {
        try {
            // Closing the connection with the client.
            clientSocket.close();
        } catch (IOException e) {
            Logger.logError(this, "Error while disconnecting client.");
        }

        // Checks if the user has already logged in, because
        // if yes we will remove it from the online users.
        if (user != null && user.getUsername() != null) {
            OnlineUsers.getInstance().removeUser(user.getUsername());
        }

        // We set stop to true in order to stop the current loop in which the thread
        // is currently running.
        stop = true;
    }
}
