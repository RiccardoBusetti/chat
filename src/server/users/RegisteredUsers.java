package server.users;

import javafx.util.Pair;
import server.constants.Constants;
import server.entities.User;
import server.exceptions.UserNotFoundException;
import server.io.TxtFilesHelper;
import server.io.TxtUserHelper;
import server.logging.Logger;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible of providing a way to access the registered users
 * text file.
 * <p>
 * The semantics of the text file are the following:
 * [username,password,boolean] where the boolean refers to the ban status of
 * the user.
 */
public class RegisteredUsers extends ServiceUsers<User, Boolean> {
    private static RegisteredUsers instance;

    private String filePath = Paths.get(Constants.BASE_PROJECT_DIR, Constants.REGISTERED_USERS_FILE_NAME + Constants.TXT_EXTENSION).toString();

    private RegisteredUsers() {

    }

    public static RegisteredUsers getInstance() {
        if (instance == null) instance = new RegisteredUsers();

        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addUser(User user, Boolean information) {
        String encodedUser = TxtUserHelper.encodeUser(user, information);
        TxtFilesHelper.write(filePath, encodedUser);

        if (isObserverAttached()) usersObserver.onUsersChanged(getAllUsers());

        Logger.logRegistration(this, "User " + user.getUsername() + " registered.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void removeUser(String username) {
        try {
            removeUserAndRewriteFile(username);

            if (isObserverAttached()) usersObserver.onUsersChanged(getAllUsers());

            Logger.logRegistration(this, "User " + username + " removed.");
        } catch (UserNotFoundException exc) {
            Logger.logError(this, exc.getMessage());
        }
    }

    /**
     * Blocks a specific user that won't be available to
     * access the service anymore.
     *
     * @param username username of the user we need to block.
     */
    public synchronized void blockUser(String username) {
        try {
            changeBlockedStatusOnFile(username, true);

            if (isObserverAttached()) usersObserver.onUsersChanged(getAllUsers());

            // If the user is online we will disconnect it.
            OnlineUsers.getInstance().removeUser(username);
            OnlineUsers.getInstance().notifyClients();

            Logger.logRegistration(this, "User " + username + " blocked.");
        } catch (UserNotFoundException exc) {
            Logger.logError(this, exc.getMessage());
        }
    }

    /**
     * Unblocks a specific user that will be able again to
     * access the service.
     *
     * @param username username of the user we need to unblock.
     */
    public synchronized void unblockUser(String username) {
        try {
            changeBlockedStatusOnFile(username, false);

            if (isObserverAttached()) usersObserver.onUsersChanged(getAllUsers());

            Logger.logRegistration(this, "User " + username + " unblocked.");
        } catch (UserNotFoundException exc) {
            Logger.logError(this, exc.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void removeAllUsers() {
        TxtFilesHelper.clear(filePath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Pair<User, Boolean> getUserByUsername(String username) throws UserNotFoundException {
        return searchUser(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<Pair<User, Boolean>> getAllUsers() {
        return getAllRegisteredUsersOnFile();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void observe(UsersObserver<User, Boolean> usersObserver) {
        attachObserver(usersObserver);
    }

    /**
     * Removes a user from the list and recreates the whole txt file.
     * This process is not optimized for big lists of users and
     * needs some improvements.
     *
     * @param username username we want to remove from the txt file.
     * @throws UserNotFoundException thrown if the user is not existing.
     */
    private void removeUserAndRewriteFile(String username) throws UserNotFoundException {
        List<Pair<User, Boolean>> registeredUsers = getAllUsers();

        // Removes the user from the registered users.
        registeredUsers.remove(searchUserByUsername(username, registeredUsers, true));

        TxtFilesHelper.clear(filePath);

        // Rewrites the file with the user removed.
        for (Pair<User, Boolean> registeredUser : registeredUsers) {
            TxtFilesHelper.write(filePath, TxtUserHelper.encodeUser(registeredUser.getKey(), registeredUser.getValue()));
        }
    }

    /**
     * Changes the blocking status of a specific user and rewrites
     * the entire file. As in the previous method we need to improve
     * the algorithm if the system userbase will scale.
     *
     * @param username  username of the user we will update the status.
     * @param isBlocked true if we will block the user and false otherwise.
     * @throws UserNotFoundException thrown if the user is not existing.
     */
    private void changeBlockedStatusOnFile(String username, boolean isBlocked) throws UserNotFoundException {
        List<Pair<User, Boolean>> registeredUsers = getAllUsers();

        // Updates the user status from not blocked to blocked.
        Pair<User, Boolean> foundUser = searchUserByUsername(username, registeredUsers, true);
        registeredUsers.remove(foundUser);
        Pair<User, Boolean> blockedUser = new Pair<>(foundUser.getKey(), isBlocked);
        registeredUsers.add(blockedUser);

        TxtFilesHelper.clear(filePath);

        // Rewrites the file with the user blocked status updated.
        for (Pair<User, Boolean> registeredUser : registeredUsers) {
            TxtFilesHelper.write(filePath, TxtUserHelper.encodeUser(registeredUser.getKey(), registeredUser.getValue()));
        }
    }

    /**
     * Searches for a specific user.
     *
     * @param username username of the user we want to search.
     * @return the found user.
     * @throws UserNotFoundException thrown if the user is not existing.
     */
    private Pair<User, Boolean> searchUser(String username) throws UserNotFoundException {
        return searchUserByUsername(username, getAllUsers(), true);
    }

    /**
     * Reads all the lines of the txt files getting all
     * the registered users.
     *
     * @return the list of all users.
     */
    private List<Pair<User, Boolean>> getAllRegisteredUsersOnFile() {
        List<Pair<User, Boolean>> registeredUsers = new ArrayList<>();

        // Converts the file lines in a list of registered user object pairs.
        for (String line : TxtFilesHelper.getAllLines(filePath)) {
            registeredUsers.add(TxtUserHelper.decodeUser(line));
        }

        return registeredUsers;
    }
}
