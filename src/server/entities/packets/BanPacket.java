package server.entities.packets;

/**
 * POJO that represents a specific packet used to
 * notify the user about its ban status.
 */
public class BanPacket extends Packet {
    private boolean isBanned;

    public BanPacket() {
        super(HeaderType.BAN_STATUS);
    }

    public BanPacket(boolean isBanned) {
        this();
        this.isBanned = isBanned;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }
}
