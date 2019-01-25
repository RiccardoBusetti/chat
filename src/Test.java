import server.entities.packets.Packet;
import server.entities.packets.UnicastMessagePacket;
import server.packets.PacketsEncoder;

public class Test {

    public static void main(String[] args) {
        PacketsEncoder packetsEncoder = new PacketsEncoder();
        System.out.println(packetsEncoder.encode(new UnicastMessagePacket(Packet.HeaderType.UNICAST_MESSAGE, "riccardo", "matteo", "ciao")));
    }

}
