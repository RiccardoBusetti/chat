package server.entities.packets;

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
