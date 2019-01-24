import server.entities.packets.Packet;
import server.entities.packets.UnicastMessagePacket;
import server.packets.PacketEncoder;

public class Test {

    public static void main(String[] args) {
        PacketEncoder packetEncoder = new PacketEncoder();
        System.out.println(packetEncoder.encode(new UnicastMessagePacket(Packet.HeaderType.UNICAST_MESSAGE, "riccardo", "matteo", "ciao")));
    }

}
