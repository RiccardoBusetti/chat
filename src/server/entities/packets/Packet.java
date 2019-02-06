package server.entities.packets;

/**
 * POJO that represents the base packet.
 */
public class Packet {
    private HeaderType headerType;

    public Packet(HeaderType headerType) {
        this.headerType = headerType;
    }

    public HeaderType getHeaderType() {
        return headerType;
    }

    public void setHeaderType(HeaderType headerType) {
        this.headerType = headerType;
    }

    /**
     * Enum containing all the possible headers that a packet can have.
     * The headers are used to identify which content will be inside of
     * the packet. Is really important that the encoder and decoder are doing
     * their work right in order to avoid malformed packets.
     */
    public enum HeaderType {
        LOGIN_DATA,
        LOGIN_RESULT,
        REGISTER_DATA,
        REGISTER_RESULT,
        UNICAST_MESSAGE_DATA,
        MULTICAST_MESSAGE_DATA,
        MESSAGE_RESULT,
        ONLINE_USERS_DATA,
        BAN_STATUS,
        ERROR_MESSAGE,
        EMPTY_PACKET
    }
}
