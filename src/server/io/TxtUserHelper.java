package server.io;

import javafx.util.Pair;
import server.constants.Constants;
import server.entities.User;

/**
 * Class containing helper methods to encode and decode the user
 * data inside of the registered users .txt file.
 */
public class TxtUserHelper {
    private static final int USERNAME = 0;
    private static final int PASSWORD = 1;
    private static final int BLOCKED_STATUS = 2;

    /**
     * Encodes the user with the following semantics:
     * [username,password,isBlocked].
     *
     * @param user      user object.
     * @param isBlocked true if the user is blocked false otherwise.
     * @return the encoded string.
     */
    public static String encodeUser(User user, Boolean isBlocked) {
        return user.getUsername() + Constants.COMMA_SEPARATOR +
                user.getPassword() + Constants.COMMA_SEPARATOR +
                isBlocked;
    }

    /**
     * Decodes the user with the following semantics:
     * [username,password,isBlocked].
     *
     * @param userData string with the encoded user data.
     * @return the user object.
     */
    public static Pair<User, Boolean> decodeUser(String userData) {
        String[] decodedString = splitString(userData);

        User user = new User(decodedString[USERNAME], decodedString[PASSWORD]);
        Boolean isBlocked = Boolean.valueOf(decodedString[BLOCKED_STATUS]);

        return new Pair<>(user, isBlocked);
    }

    /**
     * Splits the string with the regex in order to have
     * access to the 3 fields that contain the user information.
     *
     * @param string string to split.
     * @return an array of strings corresponding to the user information.
     */
    private static String[] splitString(String string) {
        return string.contains(Constants.COMMA_SEPARATOR) ? string.split(Constants.COMMA_SEPARATOR) : new String[3];
    }
}
