package server;

import constants.Constants;
import data.entities.OnlineUser;
import data.entities.User;
import logging.Logger;
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
    private OnlineUser<User, Socket> onlineUser;

    public ConnectionHandler(Socket clientSocket) {
        this.onlineUser = new OnlineUser<>(new User(clientSocket.getInetAddress().getHostName(), Constants.PASSWORD_NOT_AVAILABLE), clientSocket);
    }

    @Override
    public void run() {
        handleConnection();
    }

    private void handleConnection() {
        try {
            Logger.logConnection(this, "Handling connection with the client " + onlineUser.getClient().getRemoteSocketAddress());

            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() throws IOException {
        OnlineUsers.getInstance().userConnected(onlineUser);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(onlineUser.getClient().getInputStream()));
        PrintWriter printWriter = new PrintWriter(onlineUser.getClient().getOutputStream(), true);
    }

    private void handleLogin() {

    }

    private void handleRegistration() {

    }
}
