package server;

import server.access.AccessHelper;
import server.entities.User;
import server.entities.packets.AccessPacket;
import server.entities.packets.AccessResultPacket;
import server.entities.packets.ErrorPacket;
import server.entities.packets.Packet;
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
        } catch (IOException e) {
            Logger.logError(this, "Error while handling the connection.");
        }
    }

    private void listen() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        handleAccess(bufferedReader);
    }

    private void handleAccess(BufferedReader bufferedReader) throws IOException {
        boolean isAllowed = false;
        PacketsDecoder packetsDecoder = new PacketsDecoder();

        while (!isAllowed) {
            Packet packet = packetsDecoder.decode(bufferedReader.readLine());

            if (packet instanceof AccessPacket) {
                AccessPacket accessPacket = (AccessPacket) packet;

                if (accessPacket.getHeaderType() == Packet.HeaderType.LOGIN_DATA) {
                    isAllowed = handleLogin(accessPacket);
                } else {
                    isAllowed = handleRegister(accessPacket);
                }
            } else {
                PacketsQueue.getInstance().enqueuePacket(new ErrorPacket(Packet.HeaderType.ERROR_MESSAGE, "You need to login or register before continuing."));
            }
        }
    }

    private boolean handleLogin(AccessPacket accessPacket) {
        Logger.logStatus(this, "Handling login with the client " + clientSocket.getRemoteSocketAddress());

        User user = new User();
        user.setUsername(accessPacket.getUsername());
        user.setPassword(accessPacket.getPassword());

        AccessResultPacket accessResultPacket = new AccessResultPacket();
        accessResultPacket.setHeaderType(Packet.HeaderType.LOGIN_RESULT);

        // Performs the login and handles the result specifically.
        switch (AccessHelper.login(user.getUsername(), user.getPassword())) {
            case LOGIN_SUCCESSFUL:
                accessResultPacket.setAllowed(true);

                OnlineUsers.getInstance().addUser(user, clientSocket);
                break;
            case LOGIN_NOT_EXISTING_USER:
            case LOGIN_WRONG_CREDENTIALS:
            case LOGIN_USER_ALREADY_ONLINE:
            default:
                accessResultPacket.setAllowed(false);
        }

        // Adds the packet to the queue.
        PacketsQueue.getInstance().enqueuePacket(accessResultPacket);

        return accessResultPacket.isAllowed();
    }

    private boolean handleRegister(AccessPacket accessPacket) {
        Logger.logStatus(this, "Handling registration with the client " + clientSocket.getRemoteSocketAddress());

        User user = new User();
        user.setUsername(accessPacket.getUsername());
        user.setPassword(accessPacket.getPassword());

        AccessResultPacket accessResultPacket = new AccessResultPacket();
        accessResultPacket.setHeaderType(Packet.HeaderType.REGISTER_RESULT);

        switch (AccessHelper.register(accessPacket.getUsername(), accessPacket.getPassword())) {
            case REGISTRATION_SUCCESSFUL:
            case REGISTRATION_ALREADY_EXISTING_USER:
                accessResultPacket.setAllowed(true);

                OnlineUsers.getInstance().addUser(user, clientSocket);
                break;
            case REGISTRATION_USER_ALREADY_ONLINE:
            default:
                accessResultPacket.setAllowed(false);
                break;
        }

        // Adds the response message to the queue.
        PacketsQueue.getInstance().enqueuePacket(accessResultPacket);

        return accessResultPacket.isAllowed();
    }
}
