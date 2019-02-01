package server.access;

import javafx.util.Pair;
import server.entities.User;
import server.exceptions.UserNotFoundException;
import server.users.OnlineUsers;
import server.users.RegisteredUsers;

/**
 * Class responsible of providing helper methods to
 * perform actions related to the access.
 */
public class AccessHelper {

    public static RegistrationResult register(String username, String password) {
        RegistrationResult registrationResult;

        try {
            // Checking if the user is registered.
            Pair<User, Boolean> foundRegisteredUser = RegisteredUsers.getInstance().getUserByUsername(username);

            // Checking it the user is blocked.
            if (foundRegisteredUser.getValue()) {
                registrationResult = RegistrationResult.REGISTRATION_BLOCKED_USER;
            } else {
                registrationResult = RegistrationResult.REGISTRATION_ALREADY_EXISTING_USER;
            }
        } catch (UserNotFoundException exc) {
            // The user is not registered so we are going to register it.
            RegisteredUsers.getInstance().addUser(new User(username, password), false);

            registrationResult = RegistrationResult.REGISTRATION_SUCCESSFUL;
        }

        if (isAlreadyOnline(username)) {
            registrationResult = RegistrationResult.REGISTRATION_USER_ALREADY_ONLINE;
        }

        return registrationResult;
    }

    public static LoginResult login(String username, String password) {
        LoginResult loginResult;

        try {
            // Checking if the user is registered.
            Pair<User, Boolean> foundRegisteredUser = RegisteredUsers.getInstance().getUserByUsername(username);

            // Checking if the user password is correct and if the user is not banned.
            if (foundRegisteredUser.getKey().getPassword().equals(password) && !foundRegisteredUser.getValue()) {
                if (foundRegisteredUser.getValue()) {
                    loginResult = LoginResult.LOGIN_BLOCKED_USER;
                } else {
                    loginResult = LoginResult.LOGIN_SUCCESSFUL;
                }
            } else {
                loginResult = LoginResult.LOGIN_WRONG_CREDENTIALS;
            }
        } catch (UserNotFoundException exc) {
            loginResult = LoginResult.LOGIN_NOT_EXISTING_USER;
        }

        if (isAlreadyOnline(username)) {
            loginResult = LoginResult.LOGIN_USER_ALREADY_ONLINE;
        }

        return loginResult;
    }

    private static boolean isAlreadyOnline(String username) {
        boolean isAlreadyOnline;

        try {
            OnlineUsers.getInstance().getUserByUsername(username);

            isAlreadyOnline = true;
        } catch (UserNotFoundException exc) {
            isAlreadyOnline = false;
        }

        return isAlreadyOnline;
    }

    public enum RegistrationResult {
        REGISTRATION_SUCCESSFUL,
        REGISTRATION_ALREADY_EXISTING_USER,
        REGISTRATION_BLOCKED_USER,
        REGISTRATION_USER_ALREADY_ONLINE
    }

    public enum LoginResult {
        LOGIN_SUCCESSFUL,
        LOGIN_NOT_EXISTING_USER,
        LOGIN_BLOCKED_USER,
        LOGIN_WRONG_CREDENTIALS,
        LOGIN_USER_ALREADY_ONLINE
    }
}
