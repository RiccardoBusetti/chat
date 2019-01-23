package server.entities.packets;

public class MulticastMessagePacket extends Packet {
    private String senderUsername;
    private String content;

    public MulticastMessagePacket() {
    }

    public MulticastMessagePacket(HeaderType headerType, String senderUsername, String content) {
        super(headerType);
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
