package server.entities.packets;

/**
 * POJO that represents the specific packet used to notify the
 * client that the message is received by the server.
 */
public class MessageResultPacket extends Packet {
    private String receiveDate;

    public MessageResultPacket() {
        super(HeaderType.MESSAGE_RESULT);
    }

    public MessageResultPacket(String receiveDate) {
        this();
        this.receiveDate = receiveDate;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }
}
