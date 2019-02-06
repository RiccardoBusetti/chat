package server.packets;

import server.entities.packets.DispatchablePacket;
import server.logging.Logger;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class used to manage the packets queue.
 */
public class PacketsQueue {
    private static PacketsQueue instance;

    private LinkedBlockingQueue<DispatchablePacket> packetsQueue;

    private PacketsQueue() {
        this.packetsQueue = new LinkedBlockingQueue<>();
    }

    public static PacketsQueue getInstance() {
        if (instance == null) instance = new PacketsQueue();

        return instance;
    }

    public void sendPacket(DispatchablePacket dispatchablePacket) {
        try {
            put(dispatchablePacket);
        } catch (Exception e) {
            Logger.logError(this, "Error during enqueuing packet.");
        }
    }

    private void put(DispatchablePacket dispatchablePacket) throws InterruptedException {
        packetsQueue.put(dispatchablePacket);
    }

    public DispatchablePacket getPacketToSend() {
        try {
            return take();
        } catch (Exception e) {
            Logger.logError(this, "Error during dequeuing packet.");
        }

        return null;
    }

    private DispatchablePacket take() throws InterruptedException {
        return packetsQueue.take();
    }
}
