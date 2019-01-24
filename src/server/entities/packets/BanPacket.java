package server.entities.packets;

/**
 * POJO that represents a specific packet used to
 * notify the user about its ban status.
 */
public class BanPacket extends Packet {
    private boolean isBanned;

    public BanPacket() {
    }

    public BanPacket(HeaderType headerType, boolean isBanned) {
        super(headerType);
        this.isBanned = isBanned;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
