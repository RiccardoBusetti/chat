package client.message.parser;

public class MessageParser {
    public static String[] messageParse(String input) {
        //Here the message will be splitted with the delimiter ASCII character 0x1F
        return input.split(hexToAscii());
    }

    public static String messageCompose(String[] input) {
        //Here the message will be ready to be sent by gluing it with the ASCII character 0x1F
        StringBuilder out = new StringBuilder();

        for (String s : input)
            out.append(s).append(hexToAscii());

        return out.toString();
    }

    //Source: https://www.baeldung.com/java-convert-hex-to-ascii
    private static String hexToAscii() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < "1f".length(); i += 2) {
            String str = "1f".substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }
}