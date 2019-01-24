package server.entities.packets;

/**
 * POJO that represents the base packet.
 */
public class Packet {
    private HeaderType headerType;

    public Packet() {
    }

    public Packet(HeaderType headerType) {
        this.headerType = headerType;
    }

    public HeaderType getHeaderType() {
        return headerType;
    }

    public void setHeaderType(HeaderType headerType) {
        this.headerType = headerType;
    }

    public enum HeaderType {
        LOGIN_DATA,
        LOGIN_RESULT,
        REGISTER_DATA,
        REGISTER_RESULT,
        UNICAST_MESSAGE,
        MULTICAST_MESSAGE,
        BAN_STATUS
    }
}
