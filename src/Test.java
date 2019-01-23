import server.entities.packets.Packet;
import server.packets.PacketHeaderHelper;

public class Test {

    public static void main(String[] args) {
        PacketHeaderHelper headerHelper = new PacketHeaderHelper();

        System.out.println(headerHelper.encodeHeader(Packet.HeaderType.LOGIN_DATA));
    }

}
