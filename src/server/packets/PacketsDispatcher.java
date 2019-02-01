package server.packets;

import server.entities.packets.DispatchablePacket;
import server.entities.packets.Packet;
import server.logging.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Thread that is always listening for new messages in the queue
 * in order to dispatch them.
 */
public class PacketsDispatcher implements Runnable {

    @Override
    public void run() {
        checkForNewPackets();
    }

    private void checkForNewPackets() {
        // Continues to loop to check for new packets
        // that need to be dispatched.
        while (true) {
            Logger.logStatus(this, "Waiting for new packets in the queue...");

            handlePacketDispatching();
        }
    }

    private void handlePacketDispatching() {
        DispatchablePacket dispatchablePacket = PacketsQueue.getInstance().getPacketToSend();
        // Getting the data from the packet.
        Packet packet = dispatchablePacket.getPacket();
        List<Socket> recipientsSockets = dispatchablePacket.getRecipientsSockets();

        // Checking if we have recipients and also if the packet is not empty.
        if (recipientsSockets.size() > 0 && packet.getHeaderType() != Packet.HeaderType.EMPTY_PACKET) {
            dispatch(packet, recipientsSockets);
        }
    }

    private void dispatch(Packet packet, List<Socket> recipientsSockets) {
        try {
            dispatchPacket(packet, recipientsSockets);
        } catch (IOException exc) {
            Logger.logError(this, "Error while dispatching message.");
        }
    }

    private void dispatchPacket(Packet packet, List<Socket> recipientsSockets) throws IOException {
        PacketsEncoder packetsEncoder = new PacketsEncoder();
        String encodedPacket = packetsEncoder.encode(packet);

        // Looping on every recipient socket and we are going
        // to send to each of them the packet.
        for (Socket recipientSocket : recipientsSockets) {
            PrintWriter printWriter = new PrintWriter(recipientSocket.getOutputStream(), true);
            printWriter.println(encodedPacket);
        }

        Logger.logPacket(this, packet + " dispatched.");
    }

}
