import server.entities.packets.AccessPacket;
import server.entities.packets.Packet;
import server.entities.packets.UnicastMessagePacket;
import server.packets.PacketsEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTest {

    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket(InetAddress.getLocalHost(), 8888);

        PacketsEncoder packetsEncoder = new PacketsEncoder();
        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        printWriter.println(packetsEncoder.encode(new AccessPacket(Packet.HeaderType.REGISTER_DATA, "paola", "12345")));
        // printWriter.println(packetsEncoder.encode(new UnicastMessagePacket(Packet.HeaderType.UNICAST_MESSAGE, "ciao", "ric", "bella")));

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        while (true) {
            String line = bufferedReader.readLine();

            if (line != null) {
                System.out.println(line);
            } else {
                System.out.println("The server disconnected you!");
                break;
            }
        }
    }

}
