package server.constants;

import java.nio.file.Paths;

/**
 * Class containing all the constants used in the server.
 */
public class Constants {
    // Files constants.
    public static final String BASE_PROJECT_DIR = System.getProperty("user.dir");
    public static final String REGISTERED_USERS_FILE_NAME = "registered_users";
    public static final String TXT_EXTENSION = ".txt";
    public static final String COMMA_SEPARATOR = ",";
    public static final String EMPTY_FILE = "";

    // Packets constants.
    public static final String DIVIDE_REGEX = "1f";
    public static final String LOGIN_DATA = "Login";
    public static final String LOGIN_RESULT = "LoginResult";
    public static final String REGISTER_DATA = "Register";
    public static final String REGISTER_RESULT = "RegisterResult";
    public static final String UNICAST_MESSAGE = "UniMsg";
    public static final String MULTICAST_MESSAGE = "MultiMsg";
    public static final String MESSAGE_RESULT = "MsgResult";
    public static final String ONLINE_USERS_DATA = "OnlineUsers";
    public static final String BAN_STATUS = "BanStatus";
    public static final String ERROR_MESSAGE = "Error";
    public static final String EMPTY_PACKET = "EmptyPacket";

    // Null constants.
    public static final String NO_DATE = "-1";

    // Paths constants.
    public static final String OOF_SFX = Paths.get(System.getProperty("user.dir"), "src", "client", "assets", "oof.mp3").toString();
    public static final String CHAT_BOOT_SFX = Paths.get(System.getProperty("user.dir"), "src", "client", "assets", "chat_boot.mp3").toString();
    public static final String CHAT_NEW_MESSAGE = Paths.get(System.getProperty("user.dir"), "src", "client", "assets", "chat_new.mp3").toString();

    // Server constants.
    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 8888;
}
