package server.packets;

import server.entities.packets.Packet;

/**
 * Class responsible of encoding the different packets.
 */
public class PacketEncoder {

    public String encode(Packet packet) {
        return null;
    }

    // TODO: implement encoding.
    private String encodePacket(Packet packet) {
        String encodedPacket;

        switch (packet.getHeaderType()) {
            case LOGIN_DATA:
                break;
            case LOGIN_RESULT:
                break;
            case REGISTER_DATA:
                break;
            case REGISTER_RESULT:
                break;
            case UNICAST_MESSAGE:
                break;
            case MULTICAST_MESSAGE:
                break;
            case BAN_STATUS:
                break;
        }

        return null;
    }

}
