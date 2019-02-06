package server.entities.packets;

/**
 * POJO that represents the specific packet used to
 * notify the client if the access was allowed or not.
 */
public class AccessResultPacket extends Packet {
    private boolean isAllowed;

    public AccessResultPacket(boolean isLoginResultPacket) {
        super(isLoginResultPacket ? HeaderType.LOGIN_RESULT : HeaderType.REGISTER_RESULT);
    }

    public AccessResultPacket(boolean isLoginResultPacket, boolean isAllowed) {
        this(isLoginResultPacket);
        this.isAllowed = isAllowed;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }
}
