package server.users;

import javafx.util.Pair;
import server.entities.User;
import server.entities.packets.DispatchablePacket;
import server.entities.packets.OnlineUsersPacket;
import server.exceptions.UserNotFoundException;
import server.logging.Logger;
import server.packets.PacketsDispatcher;
import server.packets.PacketsQueue;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible of managing the online users of the chat.
 */
public class OnlineUsers extends ServiceUsers<User, Socket> {
    private static OnlineUsers instance;

    private List<Pair<User, Socket>> onlineUsers;

    private OnlineUsers() {
        onlineUsers = new ArrayList<>();
    }

    public static OnlineUsers getInstance() {
        if (instance == null) instance = new OnlineUsers();

        return instance;
    }

    @Override
    public synchronized void addUser(User user, Socket information) {
        onlineUsers.add(new Pair<>(user, information));

        if (isObserverAttached()) usersObserver.onUsersChanged(getAllUsers());

        Logger.logConnection(this, "User " + user.getUsername() + " connected!");
    }

    @Override
    public synchronized void removeUser(String username) {
        try {
            Pair<User, Socket> user = searchUserByUsername(username, onlineUsers, false);
            user.getValue().close();
            onlineUsers.remove(user);

            if (isObserverAttached()) usersObserver.onUsersChanged(getAllUsers());

            Logger.logConnection(this, "User " + username + " disconnected!");
        } catch (UserNotFoundException | IOException exc) {
            Logger.logError(this, exc.getMessage());
        }
    }

    @Override
    public synchronized void removeAllUsers() {
        onlineUsers.clear();

        if (isObserverAttached()) usersObserver.onUsersChanged(getAllUsers());
    }

    @Override
    public synchronized Pair<User, Socket> getUserByUsername(String username) throws UserNotFoundException {
        return searchUserByUsername(username, onlineUsers, false);
    }

    @Override
    public synchronized List<Pair<User, Socket>> getAllUsers() {
        return onlineUsers;
    }

    @Override
    public synchronized void observe(UsersObserver<User, Socket> usersObserver) {
        attachObserver(usersObserver);
    }

    /**
     * Notifies all the connected clients about the current state
     * of online users.
     */
    public void notifyClients() {
        DispatchablePacket dispatchablePacket = new DispatchablePacket();
        List<Socket> onlineUsersSockets = new ArrayList<>();
        OnlineUsersPacket onlineUsersPacket = new OnlineUsersPacket();

        for (Pair<User, Socket> onlineUser : getAllUsers()) {
            onlineUsersPacket.addUser(onlineUser.getKey().getUsername());
            onlineUsersSockets.add(onlineUser.getValue());
        }

        dispatchablePacket.setRecipientsSockets(onlineUsersSockets);
        dispatchablePacket.setPacket(onlineUsersPacket);

        PacketsQueue.getInstance().sendPacket(dispatchablePacket);
    }
}
