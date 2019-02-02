package server.entities.packets;

/**
 * POJO that represents a specific packet that contains
 * all the data necessary to send a message to a group of clients.
 */
public class MulticastMessagePacket extends Packet {
    private String senderUsername;
    private String content;

    public MulticastMessagePacket() {
        super(HeaderType.MULTICAST_MESSAGE_DATA);
    }

    public MulticastMessagePacket(String senderUsername, String content) {
        this();
        this.senderUsername = senderUsername;
        this.content = content;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
