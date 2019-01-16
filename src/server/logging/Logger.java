package server.logging;

/**
 * Class responsible of providing custom server.logging for the server.
 */
public class Logger {
    public static void logStatus(Object o, String statusMessage) {
        System.out.println(ConsoleColors.CYAN + "STATUS <" + o.getClass() + ">: " + statusMessage + ConsoleColors.RESET);
    }

    public static void logConnection(Object o, String connectionMessage) {
        System.out.println(ConsoleColors.YELLOW + "CONNECTION <" + o.getClass() + ">: " + connectionMessage + ConsoleColors.RESET);
    }

    public static void logError(Object o, String errorMessage) {
        System.out.println(ConsoleColors.RED + "ERROR: <" + o.getClass() + ">" + errorMessage + ConsoleColors.RESET);
    }
}
