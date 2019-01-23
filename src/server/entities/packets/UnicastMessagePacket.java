package server.entities.packets;

public class UnicastMessagePacket extends Packet {
    private String senderUsername;
    private String recipientUsername;
    private String content;

    public UnicastMessagePacket() {
    }

    public UnicastMessagePacket(HeaderType headerType, String senderUsername, String recipientUsername, String content) {
        super(headerType);
        this.senderUsername = senderUsername;
        this.recipientUsername = recipientUsername;
        this.content = content;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
