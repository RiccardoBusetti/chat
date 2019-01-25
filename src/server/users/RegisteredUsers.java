package server.users;

import javafx.util.Pair;
import server.constants.Constants;
import server.entities.User;
import server.exceptions.UserNotFoundException;
import server.io.TxtFilesHelper;
import server.io.TxtUserHelper;
import server.logging.Logger;

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

    private RegisteredUsers() {

    }

    public static RegisteredUsers getInstance() {
        if (instance == null) instance = new RegisteredUsers();

        return instance;
    }

    @Override
    public synchronized void addUser(User user, Boolean information) {
        String encodedUser = TxtUserHelper.encodeUser(user, information);
        TxtFilesHelper.write(Constants.REGISTERED_USERS_FILE_NAME, encodedUser);

        if (isObserverAttached()) usersObserver.onUsersChanged(getAllUsers());

        Logger.logRegistration(this, "User " + user.getUsername() + " registered.");
    }

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

    public synchronized void blockUser(String username) {
        try {
            blockUserAndRewriteFile(username);

            if (isObserverAttached()) usersObserver.onUsersChanged(getAllUsers());

            Logger.logRegistration(this, "User " + username + " blocked.");
        } catch (UserNotFoundException exc) {
            Logger.logError(this, exc.getMessage());
        }
    }

    @Override
    public synchronized void removeAllUsers() {
        TxtFilesHelper.clear(Constants.REGISTERED_USERS_FILE_NAME);
    }

    @Override
    public synchronized Pair<User, Boolean> getUserByUsername(String username) throws UserNotFoundException {
        return searchUser(username);
    }

    @Override
    public synchronized List<Pair<User, Boolean>> getAllUsers() {
        return getAllRegisteredUsersOnFile();
    }

    @Override
    public synchronized void observe(UsersObserver<User, Boolean> usersObserver) {
        attachObserver(usersObserver);
    }

    private void removeUserAndRewriteFile(String username) throws UserNotFoundException {
        List<Pair<User, Boolean>> registeredUsers = getAllUsers();

        // Removes the user from the registered users.
        registeredUsers.remove(searchUser(username, registeredUsers));

        TxtFilesHelper.clear(Constants.REGISTERED_USERS_FILE_NAME);

        // Rewrites the file with the user removed.
        for (Pair<User, Boolean> registeredUser : registeredUsers) {
            TxtFilesHelper.write(Constants.REGISTERED_USERS_FILE_NAME, TxtUserHelper.encodeUser(registeredUser.getKey(), registeredUser.getValue()));
        }
    }

    private void blockUserAndRewriteFile(String username) throws UserNotFoundException {
        List<Pair<User, Boolean>> registeredUsers = getAllUsers();

        // Updates the user status from not blocked to blocked.
        Pair<User, Boolean> foundUser = searchUser(username, registeredUsers);
        registeredUsers.remove(foundUser);
        Pair<User, Boolean> blockedUser = new Pair<>(foundUser.getKey(), true);
        registeredUsers.add(blockedUser);

        TxtFilesHelper.clear(Constants.REGISTERED_USERS_FILE_NAME);

        // Rewrites the file with the user blocked status updated.
        for (Pair<User, Boolean> registeredUser : registeredUsers) {
            TxtFilesHelper.write(Constants.REGISTERED_USERS_FILE_NAME, TxtUserHelper.encodeUser(registeredUser.getKey(), registeredUser.getValue()));
        }
    }

    private Pair<User, Boolean> searchUser(String username) throws UserNotFoundException {
        return searchUser(username, getAllUsers());
    }

    // TODO: find a way to avoid this code duplication.
    private Pair<User, Boolean> searchUser(String username, List<Pair<User, Boolean>> registeredUsers) throws UserNotFoundException {
        int counter = 0;

        while (counter < registeredUsers.size()) {
            Pair<User, Boolean> registeredUser = registeredUsers.get(counter);

            if (registeredUser.getKey().getUsername().equals(username)) {
                return registeredUser;
            }

            counter++;
        }

        throw new UserNotFoundException(true);
    }

    private List<Pair<User, Boolean>> getAllRegisteredUsersOnFile() {
        List<Pair<User, Boolean>> registeredUsers = new ArrayList<>();

        // Converts
        for (String line : TxtFilesHelper.getAllLines(Constants.REGISTERED_USERS_FILE_NAME)) {
            registeredUsers.add(TxtUserHelper.decodeUser(line));
        }

        return registeredUsers;
    }
}
