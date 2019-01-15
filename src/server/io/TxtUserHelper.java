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

    public static String encodeUser(User user, Boolean isBlocked) {
        return user.getUsername() + Constants.COMMA_SEPARATOR +
                user.getPassword() + Constants.COMMA_SEPARATOR +
                isBlocked;
    }

    public static Pair<User, Boolean> decodeUser(String userData) {
        String[] decodedString = decodeString(userData);

        User user = new User(decodedString[USERNAME], decodedString[PASSWORD]);
        Boolean isBlocked = Boolean.valueOf(decodedString[BLOCKED_STATUS]);

        return new Pair<>(user, isBlocked);
    }

    private static String[] decodeString(String string) {
        return string.contains(Constants.COMMA_SEPARATOR) ? string.split(Constants.COMMA_SEPARATOR) : new String[3];
    }

}
