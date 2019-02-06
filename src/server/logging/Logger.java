package server.logging;

/**
 * Class responsible of providing a logging system for the server, with colors
 * in the console in order to emphasize the different things happening.
 */
public class Logger {
    public static void logStatus(Object o, String statusMessage) {
        System.out.println(ConsoleColors.CYAN + "STATUS[" + o.getClass() + "]: " + statusMessage + ConsoleColors.RESET);
    }

    public static void logRegistration(Object o, String registrationMessage) {
        System.out.println(ConsoleColors.GREEN + "REGISTRATION[" + o.getClass() + "]: " + registrationMessage + ConsoleColors.RESET);
    }

    public static void logConnection(Object o, String connectionMessage) {
        System.out.println(ConsoleColors.YELLOW + "CONNECTION[" + o.getClass() + "]: " + connectionMessage + ConsoleColors.RESET);
    }

    public static void logError(Object o, String errorMessage) {
        System.out.println(ConsoleColors.RED + "ERROR[" + o.getClass() + "]: " + errorMessage + ConsoleColors.RESET);
    }

    public static void logPacket(Object o, String packetMessage) {
        System.out.println(ConsoleColors.PURPLE + "PACKET[" + o.getClass() + "]: " + packetMessage + ConsoleColors.RESET);
    }
}
