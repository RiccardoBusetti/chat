import server.entities.packets.OnlineUsersPacket;
import server.entities.packets.Packet;
import server.packets.PacketsDecoder;
import server.packets.PacketsEncoder;
import server.users.OnlineUsers;

import java.nio.file.Paths;

public class Test {

    public static void main(String[] args) {
        PacketsEncoder packetsEncoder = new PacketsEncoder();
        OnlineUsersPacket onlineUsersPacket = new OnlineUsersPacket(Packet.HeaderType.ONLINE_USERS_DATA);
        onlineUsersPacket.addUser("riccardo");
        onlineUsersPacket.addUser("paola");

        String encodedPacket = packetsEncoder.encode(onlineUsersPacket);

        System.out.println(encodedPacket);

        PacketsDecoder packetsDecoder = new PacketsDecoder();
        OnlineUsersPacket packet = (OnlineUsersPacket) packetsDecoder.decode(encodedPacket);
        System.out.println(packet.getUsers().size());
    }

}