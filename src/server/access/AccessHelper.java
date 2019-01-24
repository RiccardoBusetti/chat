package server.access;

import javafx.util.Pair;
import server.entities.User;
import server.exceptions.UserNotFoundException;
import server.users.RegisteredUsers;

/**
 * Class responsible of providing helper methods to
 * perform actions related to the access.
 */
public class AccessHelper {

    public static RegistrationResult register(String username, String password) {
        try {
            RegisteredUsers.getInstance().getUserByUsername(username);

            return RegistrationResult.REGISTRATION_ALREADY_EXISTING_USER;
        } catch (UserNotFoundException exc) {
            RegisteredUsers.getInstance().addUser(new User(username, password), false);

            return RegistrationResult.REGISTRATION_SUCCESSFUL;
        }
    }

    public static LoginResult login(String username, String password) {
        try {
            Pair<User, Boolean> foundUser = RegisteredUsers.getInstance().getUserByUsername(username);

            if (foundUser.getKey().getPassword().equals(password) && !foundUser.getValue()) {
                return LoginResult.LOGIN_SUCCESSFUL;
            } else {
                return LoginResult.LOGIN_WRONG_CREDENTIALS;
            }
        } catch (UserNotFoundException exc) {
            return LoginResult.LOGIN_NOT_EXISTING_USER;
        }
    }

    public enum RegistrationResult {
        REGISTRATION_SUCCESSFUL,
        REGISTRATION_ALREADY_EXISTING_USER
    }

    public enum LoginResult {
        LOGIN_SUCCESSFUL,
        LOGIN_NOT_EXISTING_USER,
        LOGIN_WRONG_CREDENTIALS
    }
}
