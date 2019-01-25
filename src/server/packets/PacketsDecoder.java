package server.packets;

import server.constants.Constants;
import server.entities.packets.AccessPacket;
import server.entities.packets.MulticastMessagePacket;
import server.entities.packets.Packet;
import server.entities.packets.UnicastMessagePacket;

/**
 * Class responsible of decoding the incoming packets.
 */
public class PacketsDecoder {

    public Packet decode(String packet) {
        return decodePacket(packet);
    }

    private Packet decodePacket(String packet) {
        String[] packetData = packet.split(Constants.DIVIDE_REGEX);

        PacketsHeaderHelper packetsHeaderHelper = new PacketsHeaderHelper();
        Packet.HeaderType packetHeader = packetsHeaderHelper.decodeHeader(packetData[0]);

        Packet decodedPacket;

        switch (packetHeader) {
            case LOGIN_DATA:
                decodedPacket = decodeAccessData(packetHeader, packetData, true);
                break;
            case REGISTER_DATA:
                decodedPacket = decodeAccessData(packetHeader, packetData, false);
                break;
            case UNICAST_MESSAGE:
                decodedPacket = decodeUnicastMessage(packetHeader, packetData);
                break;
            case MULTICAST_MESSAGE:
                decodedPacket = decodeMulticastMessage(packetHeader, packetData);
                break;
            default:
                decodedPacket = new Packet(packetHeader);
        }

        return decodedPacket;
    }

    /**
     * Decodes the access data packet that has the following semantics:
     * [header,username,password].
     */
    private AccessPacket decodeAccessData(Packet.HeaderType packetHeader, String[] packetData, boolean isLogin) {
        AccessPacket accessPacket = new AccessPacket();
        accessPacket.setHeaderType(packetHeader);
        accessPacket.setUsername(packetData[1]);
        accessPacket.setPassword(packetData[2]);
        accessPacket.setLogin(isLogin);

        return accessPacket;
    }

    /**
     * Decodes the unicast message packet that has the following semantics:
     * [header,sender,recipient,content].
     */
    private UnicastMessagePacket decodeUnicastMessage(Packet.HeaderType packetHeader, String[] packetData) {
        UnicastMessagePacket unicastMessagePacket = new UnicastMessagePacket();
        unicastMessagePacket.setHeaderType(packetHeader);
        unicastMessagePacket.setSenderUsername(packetData[1]);
        unicastMessagePacket.setRecipientUsername(packetData[2]);
        unicastMessagePacket.setContent(packetData[3]);

        return unicastMessagePacket;
    }

    /**
     * Decodes the multicast message packet that has the following semantics:
     * [header,sender,content].
     */
    private MulticastMessagePacket decodeMulticastMessage(Packet.HeaderType packetHeader, String[] packetData) {
        MulticastMessagePacket multicastMessagePacket = new MulticastMessagePacket();
        multicastMessagePacket.setHeaderType(packetHeader);
        multicastMessagePacket.setSenderUsername(packetData[1]);
        multicastMessagePacket.setContent(packetData[2]);

        return multicastMessagePacket;
    }
}
