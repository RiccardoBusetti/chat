package server.packets;

import server.entities.packets.Packet;
import server.logging.Logger;

public class PacketDispatcher implements Runnable {

    @Override
    public void run() {
        checkNewMessages();
    }

    private void checkNewMessages() {
        while (true) {
            Logger.logPacket(this, "Waiting for new packets in the queue...");

            Packet packet = PacketsQueue.getInstance().dequeuePacket();
            PacketEncoder packetEncoder = new PacketEncoder();
            String encodedPacket = packetEncoder.encode(packet);

            // TODO: implement message sending.

            Logger.logPacket(this, "New packet sent.");
        }
    }

}
