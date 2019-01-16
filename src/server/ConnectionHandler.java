package server;

import server.constants.Constants;
import server.entities.User;
import server.logging.Logger;
import server.users.OnlineUsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Class responsible of handling each connection on a different thread
 * in order to allow for multiple connections.
 */
public class ConnectionHandler implements Runnable {

    private User user;
    private Socket clientSocket;

    public ConnectionHandler(Socket clientSocket) {
        this.user = new User(clientSocket.getInetAddress().getHostName(), Constants.PASSWORD_NOT_AVAILABLE);
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
            e.printStackTrace();
        }
    }

    private void listen() throws IOException {
        OnlineUsers.getInstance().addUser(user, clientSocket);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    private void handleLogin() {

    }

    private void handleRegistration() {

    }
}
