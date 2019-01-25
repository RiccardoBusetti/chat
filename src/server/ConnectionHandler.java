package server;

import server.access.AccessHelper;
import server.entities.User;
import server.entities.packets.AccessPacket;
import server.entities.packets.AccessResultPacket;
import server.entities.packets.Packet;
import server.logging.Logger;
import server.packets.PacketsDecoder;
import server.packets.PacketsQueue;
import server.users.OnlineUsers;
import server.users.RegisteredUsers;

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

    // TODO: implement the access with the messages parser.
    private void listen() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        handleAccess(bufferedReader);
    }

    private void handleAccess(BufferedReader bufferedReader) throws IOException {
        boolean isAllowed = false;
        PacketsDecoder packetsDecoder = new PacketsDecoder();

        while (!isAllowed) {
            Packet decodedPacket = packetsDecoder.decode(bufferedReader.readLine());

            if (decodedPacket instanceof AccessPacket) {
                AccessPacket accessPacket = (AccessPacket) decodedPacket;

                if (accessPacket.isLogin()) {
                    isAllowed = handleLogin(accessPacket);
                } else {
                    isAllowed = handleRegister(accessPacket);
                }
            }
        }
    }

    private boolean handleLogin(AccessPacket accessPacket) {
        User user = new User();
        user.setUsername(accessPacket.getUsername());
        user.setPassword(accessPacket.getPassword());

        AccessResultPacket accessResultPacket = new AccessResultPacket();

        switch (AccessHelper.login(user.getUsername(), user.getPassword())) {
            case LOGIN_SUCCESSFUL:
                accessResultPacket.setAllowed(true);

                OnlineUsers.getInstance().addUser(user, clientSocket);
                break;
            case LOGIN_NOT_EXISTING_USER:
                accessResultPacket.setAllowed(false);
                break;
            case LOGIN_WRONG_CREDENTIALS:
                accessResultPacket.setAllowed(false);
                break;
        }

        PacketsQueue.getInstance().enqueuePacket(accessResultPacket);

        return accessResultPacket.isAllowed();
    }

    private boolean handleRegister(AccessPacket accessPacket) {
        boolean isAllowed = false;

        switch (AccessHelper.register(accessPacket.getUsername(), accessPacket.getPassword())) {
            case REGISTRATION_SUCCESSFUL:
                isAllowed = true;

                RegisteredUsers.getInstance().addUser(user, false);
                OnlineUsers.getInstance().addUser(user, clientSocket);
                break;
            case REGISTRATION_ALREADY_EXISTING_USER:

                break;
        }

        return isAllowed;
    }
}
