package server.packets;

import server.constants.Constants;
import server.entities.packets.*;
import server.exceptions.MalformedPacketException;
import server.logging.Logger;

/**
 * Class responsible of decoding the incoming packets.
 */
public class PacketsDecoder {
    public Packet decode(String packet) {
        try {
            Logger.logStatus(this, "Decoding " + packet);

            return decodePacket(packet);
        } catch (MalformedPacketException exc) {
            Logger.logError(this, exc.getMessage());
        }

        return new Packet();
    }

    private Packet decodePacket(String packet) throws MalformedPacketException {
        String[] packetData = packet.split(Constants.DIVIDE_REGEX);

        PacketsHeaderHelper packetsHeaderHelper = new PacketsHeaderHelper();
        Packet.HeaderType packetHeader = packetsHeaderHelper.decodeHeader(packetData[0]);

        Packet decodedPacket;

        switch (packetHeader) {
            case LOGIN_DATA:
                decodedPacket = decodeAccessData(packetHeader, packetData);
                break;
            case LOGIN_RESULT:
                decodedPacket = decodeAccessResult(packetHeader, packetData);
                break;
            case REGISTER_DATA:
                decodedPacket = decodeAccessData(packetHeader, packetData);
                break;
            case REGISTER_RESULT:
                decodedPacket = decodeAccessResult(packetHeader, packetData);
                break;
            case UNICAST_MESSAGE:
                decodedPacket = decodeUnicastMessage(packetHeader, packetData);
                break;
            case MULTICAST_MESSAGE:
                decodedPacket = decodeMulticastMessage(packetHeader, packetData);
                break;
            case BAN_STATUS:
                decodedPacket = decodeBanStatus(packetHeader, packetData);
                break;
            case ERROR_MESSAGE:
                decodedPacket = decodeErrorMessage(packetHeader, packetData);
                break;
            default:
                decodedPacket = new Packet(Packet.HeaderType.EMPTY_PACKET);
        }

        return decodedPacket;
    }

    /**
     * Decodes the access data packet that has the following semantics:
     * [header,username,password].
     */
    private AccessPacket decodeAccessData(Packet.HeaderType packetHeader, String[] packetData) {
        AccessPacket accessPacket = new AccessPacket();
        accessPacket.setHeaderType(packetHeader);
        accessPacket.setUsername(packetData[1]);
        accessPacket.setPassword(packetData[2]);

        return accessPacket;
    }

    /**
     * Decodes the access result packet that has the following semantics:
     * [header,isAllowed].
     */
    private AccessResultPacket decodeAccessResult(Packet.HeaderType packetHeader, String[] packetData) {
        AccessResultPacket accessResultPacket = new AccessResultPacket();
        accessResultPacket.setHeaderType(packetHeader);
        accessResultPacket.setAllowed(Boolean.valueOf(packetData[1]));

        return accessResultPacket;
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

    /**
     * Decodes the ban status packet that has the following semantics:
     * [header,isBanned].
     */
    private BanPacket decodeBanStatus(Packet.HeaderType packetHeader, String[] packetData) {
        BanPacket banPacket = new BanPacket();
        banPacket.setHeaderType(packetHeader);
        banPacket.setBanned(Boolean.valueOf(packetData[1]));

        return banPacket;
    }

    /**
     * Decodes the error message packet that has the following semantics:
     * [header,errorMessage].
     */
    private ErrorPacket decodeErrorMessage(Packet.HeaderType packetHeader, String[] packetData) {
        ErrorPacket errorPacket = new ErrorPacket();
        errorPacket.setHeaderType(packetHeader);
        errorPacket.setErrorMessage(packetData[1]);

        return errorPacket;
    }
}
