package server.entities.packets;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO representing a packet that needs to be dispatched
 * by the server.
 */
public class DispatchablePacket {
    private Packet packet;
    private List<Socket> recipientsSockets;

    public DispatchablePacket() {
        this.recipientsSockets = new ArrayList<>();
    }

    public void addRecipientSocket(Socket recipientSocket) {
        this.recipientsSockets.add(recipientSocket);
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public List<Socket> getRecipientsSockets() {
        return recipientsSockets;
    }

    public void setRecipientsSockets(List<Socket> recipientsSockets) {
        this.recipientsSockets.addAll(recipientsSockets);
    }
}
