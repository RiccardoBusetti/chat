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

/**
 * Class responsible of handling each connection on a different thread
 * in order to allow for multiple connections.
 */
public class ConnectionHandler implements Runnable {

    private User user;
    private Socket clientSocket;
    private boolean stop = false;

    public ConnectionHandler(Socket clientSocket) {
        this.user = null;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        handleConnection();
    }

    private void handleConnection() {
        try {
            Logger.logConnection(this, "Handling connection with the client " + clientSocket.getRemoteSocketAddress());

            listen();
        } catch (IOException exc) {
            Logger.logError(this, "Error while handling the connection.");
        }
    }

    private void listen() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        handleAccess(bufferedReader);

        handleMessages(bufferedReader);
    }

    private void handleAccess(BufferedReader bufferedReader) throws IOException {
        boolean isAllowed = false;
        PacketsDecoder packetsDecoder = new PacketsDecoder();

        while (!isAllowed || !stop) {
            String inputString = bufferedReader.readLine();
            System.out.println(inputString);

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
                    PacketsQueue.getInstance().enqueuePacket(dispatchablePacket);
                } else {
                    sendErrorMessage("Prima di inviare messaggi devi fare l'accesso.");
                }
            } else {
                disconnectClient();
            }
        }
    }

    private AccessResultPacket handleLogin(AccessPacket accessPacket) {
        Logger.logStatus(this, "Handling login with the client " + clientSocket.getRemoteSocketAddress());

        User user = new User();
        user.setUsername(accessPacket.getUsername());
        user.setPassword(accessPacket.getPassword());

        // Preparing the result packet.
        AccessResultPacket accessResultPacket = new AccessResultPacket();
        accessResultPacket.setHeaderType(Packet.HeaderType.LOGIN_RESULT);

        // Performs the login and handles the result specifically.
        switch (AccessHelper.login(user.getUsername(), user.getPassword())) {
            case LOGIN_SUCCESSFUL:
                accessResultPacket.setAllowed(true);
                // Now the user is online, so we will add it to the online users list.
                OnlineUsers.getInstance().addUser(user, clientSocket);
                break;
            case LOGIN_NOT_EXISTING_USER:
            case LOGIN_BLOCKED_USER:
            case LOGIN_WRONG_CREDENTIALS:
            case LOGIN_USER_ALREADY_ONLINE:
            default:
                accessResultPacket.setAllowed(false);
                break;
        }

        return accessResultPacket;
    }

    private AccessResultPacket handleRegister(AccessPacket accessPacket) {
        Logger.logStatus(this, "Handling registration with the client " + clientSocket.getRemoteSocketAddress());

        User user = new User();
        user.setUsername(accessPacket.getUsername());
        user.setPassword(accessPacket.getPassword());

        // Preparing the result packet.
        AccessResultPacket accessResultPacket = new AccessResultPacket();
        accessResultPacket.setHeaderType(Packet.HeaderType.REGISTER_RESULT);

        // Performs the registration and handles the result specifically.
        switch (AccessHelper.register(accessPacket.getUsername(), accessPacket.getPassword())) {
            case REGISTRATION_SUCCESSFUL:
            case REGISTRATION_ALREADY_EXISTING_USER:
                accessResultPacket.setAllowed(true);
                // Now the user is online, so we will add it to the online users list.
                OnlineUsers.getInstance().addUser(user, clientSocket);
                break;
            case REGISTRATION_BLOCKED_USER:
            case REGISTRATION_USER_ALREADY_ONLINE:
            default:
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
                Packet packet = packetsDecoder.decode(bufferedReader.readLine());

                // We need to check if the packet is a message and depending
                // on its type we are going to handle it differently.
                if (packet instanceof UnicastMessagePacket) {
                    UnicastMessagePacket unicastMessagePacket = (UnicastMessagePacket) packet;

                    handleUnicastMessage(unicastMessagePacket);
                } else if (packet instanceof MulticastMessagePacket) {
                    MulticastMessagePacket multicastMessagePacket = (MulticastMessagePacket) packet;

                    handleMulticastMessage(multicastMessagePacket);
                } else {
                    sendErrorMessage("Il messaggio da te inviato ha un formato errato, riprova.");
                }
            } else {
                disconnectClient();
            }
        }
    }

    private void handleUnicastMessage(UnicastMessagePacket unicastMessagePacket) {
        try {
            Pair<User, Socket> recipientUser = OnlineUsers.getInstance().getUserByUsername(unicastMessagePacket.getRecipientUsername());
        } catch (UserNotFoundException exc) {
            sendMessageResult(false);
        }
    }

    private void handleMulticastMessage(MulticastMessagePacket multicastMessagePacket) {

    }

    // TODO: implement checks on the readline to see if the client is still connected or if it is disconnected or if it is sending the END message
    private boolean checkConnection() {
        return false;
    }

    private void sendMessageResult(boolean isReceived) {
        MessageResultPacket messageResultPacket = new MessageResultPacket(Packet.HeaderType.MESSAGE_RESULT,
                isReceived ? LocalDateTime.now().toString() : Constants.NO_DATE);
        // Prepares the result of the message.
        DispatchablePacket dispatchablePacket = new DispatchablePacket();
        dispatchablePacket.addRecipientSocket(clientSocket);
        dispatchablePacket.setPacket(messageResultPacket);
        // Adds the message result to the queue.
        PacketsQueue.getInstance().enqueuePacket(dispatchablePacket);
    }

    private void sendErrorMessage(String errorMessage) {
        ErrorPacket errorPacket = new ErrorPacket(Packet.HeaderType.ERROR_MESSAGE, errorMessage);
        // Prepares the result of the access.
        DispatchablePacket dispatchablePacket = new DispatchablePacket();
        dispatchablePacket.addRecipientSocket(clientSocket);
        dispatchablePacket.setPacket(errorPacket);
        // Adds the error message to the queue.
        PacketsQueue.getInstance().enqueuePacket(dispatchablePacket);
    }

    private void disconnectClient() throws IOException {
        if (user != null && user.getUsername() != null) {
            OnlineUsers.getInstance().removeUser(user.getUsername());
        }
        clientSocket.close();
        stop = true;
    }
}
