import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTest {

    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket(InetAddress.getLocalHost(), 8888);

        while (true) {

        }
    }

}
