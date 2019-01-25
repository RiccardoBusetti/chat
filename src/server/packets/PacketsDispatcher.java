package server.packets;

import server.entities.packets.AccessResultPacket;
import server.entities.packets.Packet;
import server.logging.Logger;

public class PacketsDispatcher implements Runnable {

    @Override
    public void run() {
        checkNewMessages();
    }

    private void checkNewMessages() {
        while (true) {
            Logger.logStatus(this, "Waiting for new packets in the queue...");

            Packet packet = PacketsQueue.getInstance().dequeuePacket();
            PacketsEncoder packetsEncoder = new PacketsEncoder();

            Logger.logPacket(this, "New packet sent " + packetsEncoder.encode(packet));
        }
    }

}
