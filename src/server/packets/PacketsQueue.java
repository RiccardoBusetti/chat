package server.packets;

import server.entities.packets.Packet;
import server.logging.Logger;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class used to manage the packets queue.
 */
public class PacketsQueue {
    private static PacketsQueue instance;

    private LinkedBlockingQueue<Packet> packetsQueue;

    private PacketsQueue() {
        this.packetsQueue = new LinkedBlockingQueue<>();
    }

    public static PacketsQueue getInstance() {
        if (instance == null) instance = new PacketsQueue();

        return instance;
    }

    public void enqueuePacket(Packet packet) {
        try {
            put(packet);
        } catch (InterruptedException e) {
            Logger.logError(this, "Error during enqueuing packet.");
        }
    }

    private void put(Packet packet) throws InterruptedException {
        packetsQueue.put(packet);
    }

    public Packet dequeuePacket() {
        try {
            return take();
        } catch (InterruptedException e) {
            Logger.logError(this, "Error during dequeuing packet.");
        }

        return null;
    }

    private Packet take() throws InterruptedException {
        return packetsQueue.take();
    }
}
