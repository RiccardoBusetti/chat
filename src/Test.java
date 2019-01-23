import server.entities.packets.Packet;
import server.packets.PacketDispatcher;
import server.packets.PacketHeaderHelper;
import server.packets.PacketsQueue;

public class Test {

    public static void main(String[] args) {
        PacketDispatcher dispatcher = new PacketDispatcher();
        new Thread(dispatcher).start();
        // new Thread(() -> PacketsQueue.getInstance().enqueuePacket(new Packet())).start();
        PacketsQueue.getInstance().enqueuePacket(new Packet());
    }

}
