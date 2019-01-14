package server.users;

import data.entities.OnlineUser;
import data.entities.User;
import exceptions.UserNotFoundException;
import logging.Logger;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class implementing {@link Users} behavior with the use of
 * {@link User} and {@link Socket}.
 */
public class OnlineUsers implements Users<User, Socket> {

    private static OnlineUsers instance;

    private List<OnlineUser<User, Socket>> onlineUsers;

    private OnlineUsers() {
        onlineUsers = new ArrayList<>();
    }

    public static OnlineUsers getInstance() {
        if (instance != null) instance = new OnlineUsers();

        return instance;
    }

    @Override
    public void userConnected(User user, Socket clientSocket) {
        onlineUsers.add(new OnlineUser<>(user, clientSocket));

        Logger.logConnection(this, "User " + user.getUsername() + " connected!");
    }

    @Override
    public void userConnected(OnlineUser<User, Socket> onlineUser) {
        onlineUsers.add(onlineUser);

        Logger.logConnection(this, "User " + onlineUser.getUser().getUsername() + " connected!");
    }

    @Override
    public void userDisconnected(String username) {
        try {
            onlineUsers.remove(searchUserByUsername(username));

            Logger.logConnection(this, "User " + username + " disconnected!");
        } catch (UserNotFoundException exc) {
            Logger.logError(this, exc.getMessage());
        }
    }

    @Override
    public void disconnectAllUsers() {
        onlineUsers.clear();
    }

    @Override
    public OnlineUser<User, Socket> getUserByUsername(String username) throws UserNotFoundException {
        return searchUserByUsername(username);
    }

    @Override
    public List<OnlineUser<User, Socket>> getAllUsers() {
        return onlineUsers;
    }

    private OnlineUser<User, Socket> searchUserByUsername(String username) throws UserNotFoundException {
        int counter = 0;

        while (counter < onlineUsers.size()) {
            OnlineUser<User, Socket> onlineUser = onlineUsers.get(counter);

            if (onlineUser.getUser().getUsername().equals(username))
                return onlineUser;

            counter++;
        }

        throw new UserNotFoundException();
    }
}
