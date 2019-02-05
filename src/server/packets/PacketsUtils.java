package server.packets;

/**
 * Class containing utils methods to work with packets.
 */
public class PacketsUtils {

    /**
     * Source: https://www.baeldung.com/java-convert-hex-to-ascii
     * Needed for encoding the hex ascii to ascii char
     */
    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }
}
