package server;

import server.access.AccessHelper;
import server.entities.User;
import server.entities.packets.*;
import server.logging.Logger;
import server.packets.PacketsDecoder;
import server.packets.PacketsQueue;
import server.users.OnlineUsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Class responsible of handling each connection on a different thread
 * in order to allow for multiple connections.
 */
public class ConnectionHandler implements Runnable {

    private User user;
    private Socket clientSocket;

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

        while (!isAllowed) {
            Packet packet = packetsDecoder.decode(bufferedReader.readLine());

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
                    handleAccessError();
                    break;
                }

                isAllowed = accessResultPacket.isAllowed();

                // Prepares the result of the access.
                DispatchablePacket dispatchablePacket = new DispatchablePacket();
                dispatchablePacket.setPacket(accessResultPacket);
                dispatchablePacket.addRecipientSocket(clientSocket);
                // Adds the result message to the queue.
                PacketsQueue.getInstance().enqueuePacket(dispatchablePacket);
            } else {
                handleAccessError();
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

    private void handleAccessError() {
        ErrorPacket errorPacket = new ErrorPacket(Packet.HeaderType.ERROR_MESSAGE, "You need to authenticate before using the chat.");
        // Prepares the result of the access.
        DispatchablePacket dispatchablePacket = new DispatchablePacket();
        dispatchablePacket.setPacket(errorPacket);
        dispatchablePacket.addRecipientSocket(clientSocket);
        // Adds the error message to the queue.
        PacketsQueue.getInstance().enqueuePacket(dispatchablePacket);
    }

    // TODO: handle messages.
    private void handleMessages(BufferedReader bufferedReader) {

    }

    // TODO: implement checks on the readline to see if the client is still connected or if it is disconnected or if it is sending the END message
    private boolean checkConnection() {
        return false;
    }
}
