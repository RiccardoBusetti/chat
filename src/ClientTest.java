import server.entities.packets.AccessPacket;
import server.entities.packets.MulticastMessagePacket;
import server.entities.packets.Packet;
import server.packets.PacketsEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket clientSocket = new Socket(InetAddress.getLocalHost(), 8888);

        PacketsEncoder packetsEncoder = new PacketsEncoder();
        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        printWriter.println(packetsEncoder.encode(new AccessPacket(Packet.HeaderType.REGISTER_DATA, "paola", "12345")));

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String line = bufferedReader.readLine();
        System.out.println(line);

        printWriter.println(packetsEncoder.encode(new MulticastMessagePacket(Packet.HeaderType.MULTICAST_MESSAGE_DATA, "paola", "Ciao come stai?")));
        line = bufferedReader.readLine();
        System.out.println(line);

        while (true) {

        }
    }

}
