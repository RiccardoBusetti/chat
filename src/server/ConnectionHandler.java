package server;

import server.constants.Constants;
import server.entities.User;
import server.exceptions.UserNotFoundException;
import server.logging.Logger;
import server.login.LoginHelper;
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
            e.printStackTrace();
        }
    }

    // TODO: implement the login with the messages parser.
    private void listen() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String loginCredentials = bufferedReader.readLine();
        String[] credentials = loginCredentials.split(Constants.COMMA_SEPARATOR);
        user = new User(credentials[0], credentials[1]);

        switch (LoginHelper.login(user.getUsername(), user.getPassword())) {
            case LOGIN_SUCCESSFUL:
                OnlineUsers.getInstance().addUser(user, clientSocket);
                Logger.logStatus(this, "The user is registered!");

                try {
                    PrintWriter printWriter = new PrintWriter(OnlineUsers.getInstance().getUserByUsername("riccardo").getValue().getOutputStream(), true);
                    printWriter.println("Login successful");
                } catch (UserNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case LOGIN_NOT_EXISTING_USER:
                Logger.logStatus(this, "The user is not registered!");
                break;
            case LOGIN_WRONG_CREDENTIALS:
                Logger.logStatus(this, "The credentials are wrong!");
                break;
        }
    }
}
