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
public class RegisteredUsers implements Users<User, Boolean> {
    @Override
    public void addUser(User user, Boolean information) {
        String encodedUser = TxtUserHelper.encodeUser(user, information);
        TxtFilesHelper.write(Constants.REGISTERED_USERS_FILE_NAME, encodedUser);
    }

    @Override
    public void removeUser(String username) {
        try {
            removeUserAndRewriteFile(username);
        } catch (UserNotFoundException exc) {
            Logger.logError(this, exc.getMessage());
        }
    }

    @Override
    public void removeAllUsers() {
        TxtFilesHelper.clear(Constants.REGISTERED_USERS_FILE_NAME);
    }

    @Override
    public Pair<User, Boolean> getUserByUsername(String username) throws UserNotFoundException {
        return searchUserByUsernameOnFile(username);
    }

    @Override
    public List<Pair<User, Boolean>> getAllUsers() {
        return getAllRegisteredUsersOnFile();
    }

    private void removeUserAndRewriteFile(String username) throws UserNotFoundException {
        List<Pair<User, Boolean>> registeredUsers = getAllUsers();

        registeredUsers.remove(searchUserByUsernameOnFile(username, registeredUsers));

        TxtFilesHelper.clear(Constants.REGISTERED_USERS_FILE_NAME);

        for (Pair<User, Boolean> registeredUser : registeredUsers) {
            TxtFilesHelper.write(Constants.REGISTERED_USERS_FILE_NAME, TxtUserHelper.encodeUser(registeredUser.getKey(), registeredUser.getValue()));
        }
    }

    private Pair<User, Boolean> searchUserByUsernameOnFile(String username) throws UserNotFoundException {
        List<String> registeredUsers = TxtFilesHelper.getAllLines(Constants.REGISTERED_USERS_FILE_NAME);
        int counter = 0;

        while (counter < registeredUsers.size()) {
            Pair<User, Boolean> registeredUser = TxtUserHelper.decodeUser(registeredUsers.get(counter));

            if (registeredUser.getKey().getUsername().equals(username)) {
                return registeredUser;
            }

            counter++;
        }

        throw new UserNotFoundException();
    }

    private Pair<User, Boolean> searchUserByUsernameOnFile(String username, List<Pair<User, Boolean>> registeredUsers) throws UserNotFoundException {
        int counter = 0;

        while (counter < registeredUsers.size()) {
            Pair<User, Boolean> registeredUser = registeredUsers.get(counter);

            if (registeredUser.getKey().getUsername().equals(username)) {
                return registeredUser;
            }

            counter++;
        }

        throw new UserNotFoundException();
    }

    private List<Pair<User, Boolean>> getAllRegisteredUsersOnFile() {
        List<Pair<User, Boolean>> registeredUsers = new ArrayList<>();

        for (String line : TxtFilesHelper.getAllLines(Constants.REGISTERED_USERS_FILE_NAME)) {
            registeredUsers.add(TxtUserHelper.decodeUser(line));
        }

        return registeredUsers;
    }
}
