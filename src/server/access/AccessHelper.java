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

    /**
     * Performs the registration of the user inside of the txt file,
     * used as a database.
     *
     * @param username username of the user.
     * @param password password of the user.
     * @return the result of the registration.
     */
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

        // If the user is already online we will stop the registration.
        if (isAlreadyOnline(username)) {
            registrationResult = RegistrationResult.REGISTRATION_USER_ALREADY_ONLINE;
        }

        return registrationResult;
    }

    /**
     * Performs the login of the user by looking at the txt file,
     * used as a database.
     *
     * @param username username of the user.
     * @param password password of the user.
     * @return the result of the login.
     */
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

        // If the user is already online we will stop the login.
        if (isAlreadyOnline(username)) {
            loginResult = LoginResult.LOGIN_USER_ALREADY_ONLINE;
        }

        return loginResult;
    }

    /**
     * Checks if a specific user is online.
     *
     * @param username username of the user we want to see the status.
     * @return true if the user is online false if not.
     */
    private static boolean isAlreadyOnline(String username) {
        boolean isAlreadyOnline;

        try {
            // Tries to find if the username exists in the current
            // online users.
            OnlineUsers.getInstance().getUserByUsername(username);

            isAlreadyOnline = true;
        } catch (UserNotFoundException exc) {
            isAlreadyOnline = false;
        }

        return isAlreadyOnline;
    }

    /**
     * Enum representing the possible results that can
     * happen during the registration.
     */
    public enum RegistrationResult {
        REGISTRATION_SUCCESSFUL,
        REGISTRATION_ALREADY_EXISTING_USER,
        REGISTRATION_BLOCKED_USER,
        REGISTRATION_USER_ALREADY_ONLINE
    }

    /**
     * Enum representing the possible results that can
     * happen during the login.
     */
    public enum LoginResult {
        LOGIN_SUCCESSFUL,
        LOGIN_NOT_EXISTING_USER,
        LOGIN_BLOCKED_USER,
        LOGIN_WRONG_CREDENTIALS,
        LOGIN_USER_ALREADY_ONLINE
    }
}
