import server.entities.packets.Packet;
import server.packets.PacketsDecoder;
import server.packets.PacketsEncoder;

public class Testing {

    public static void main(String[] args) {
        PacketsEncoder packetsEncoder = new PacketsEncoder();
        System.out.println(packetsEncoder.encode(new Packet(null)));

        PacketsDecoder packetsDecoder = new PacketsDecoder();
        System.out.println(packetsDecoder.decode("1fciao"));
    }

}
