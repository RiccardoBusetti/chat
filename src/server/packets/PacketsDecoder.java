package server.packets;

import server.constants.Constants;
import server.entities.packets.*;
import server.exceptions.MalformedPacketException;
import server.logging.Logger;

/**
 * Class responsible of decoding the incoming packets.
 */
public class PacketsDecoder {

    /**
     * Decodes the incoming packet from a string to a Java POJO.
     *
     * @param packet string containing the packet.
     * @return the Java POJO representing the packet.
     */
    public Packet decode(String packet) {
        try {
            Logger.logStatus(this, "Decoding packet.");

            return decodePacket(packet);
        } catch (MalformedPacketException exc) {
            Logger.logError(this, exc.getMessage());
        }

        return new Packet(Packet.HeaderType.EMPTY_PACKET);
    }

    private Packet decodePacket(String packet) throws MalformedPacketException {
        String[] packetFields = validatePacket(packet);

        PacketsHeaderHelper packetsHeaderHelper = new PacketsHeaderHelper();
        Packet.HeaderType packetHeader = packetsHeaderHelper.decodeHeader(packetFields[0]);

        Packet decodedPacket;

        switch (packetHeader) {
            case LOGIN_DATA:
                decodedPacket = decodeAccessData(packetHeader, packetFields);
                break;
            case LOGIN_RESULT:
                decodedPacket = decodeAccessResult(packetHeader, packetFields);
                break;
            case REGISTER_DATA:
                decodedPacket = decodeAccessData(packetHeader, packetFields);
                break;
            case REGISTER_RESULT:
                decodedPacket = decodeAccessResult(packetHeader, packetFields);
                break;
            case UNICAST_MESSAGE_DATA:
                decodedPacket = decodeUnicastMessageData(packetHeader, packetFields);
                break;
            case MULTICAST_MESSAGE_DATA:
                decodedPacket = decodeMulticastMessageData(packetHeader, packetFields);
                break;
            case MESSAGE_RESULT:
                decodedPacket = decodeMessageResult(packetHeader, packetFields);
                break;
            case ONLINE_USERS_DATA:
                decodedPacket = decodeOnlineUsersData(packetHeader, packetFields);
                break;
            case BAN_STATUS:
                decodedPacket = decodeBanStatus(packetHeader, packetFields);
                break;
            case ERROR_MESSAGE:
                decodedPacket = decodeErrorMessage(packetHeader, packetFields);
                break;
            case EMPTY_PACKET:
            default:
                decodedPacket = new Packet(Packet.HeaderType.EMPTY_PACKET);
                break;
        }

        return decodedPacket;
    }

    /**
     * Validates the incoming packet string in order to prevent
     * server crashes.
     *
     * @param packet string containing the packet.
     * @return the array of fields inside of the packet.
     * @throws MalformedPacketException thrown when the packet is malformed.
     */
    private String[] validatePacket(String packet) throws MalformedPacketException {
        if (packet == null || packet.equals("")) throw new MalformedPacketException("The packet is null or empty.", true);

        String[] packetFields = packet.split(Constants.DIVIDE_REGEX);

        // Checking if the incoming packet has less than two fields,
        // because we need at least the header.
        if (packetFields.length < 1) {
            throw new MalformedPacketException("The packet has no header.", true);
        }

        // Checking if the packet header exists.
        if (new PacketsHeaderHelper().decodeHeader(packetFields[0]) == null) {
            throw new MalformedPacketException("The packet header is invalid.", true);
        }

        return packetFields;
    }

    /**
     * Decodes the access data packet that has the following semantics:
     * [header,username,password].
     */
    private AccessPacket decodeAccessData(Packet.HeaderType packetHeader, String[] packetData) {
        AccessPacket accessPacket = new AccessPacket(packetHeader);
        accessPacket.setUsername(packetData[1]);
        accessPacket.setPassword(packetData[2]);

        return accessPacket;
    }

    /**
     * Decodes the access result packet that has the following semantics:
     * [header,isAllowed].
     */
    private AccessResultPacket decodeAccessResult(Packet.HeaderType packetHeader, String[] packetData) {
        AccessResultPacket accessResultPacket = new AccessResultPacket(packetHeader);
        accessResultPacket.setAllowed(Boolean.valueOf(packetData[1]));

        return accessResultPacket;
    }

    /**
     * Decodes the unicast message packet that has the following semantics:
     * [header,sender,recipient,content].
     */
    private UnicastMessagePacket decodeUnicastMessageData(Packet.HeaderType packetHeader, String[] packetData) {
        UnicastMessagePacket unicastMessagePacket = new UnicastMessagePacket(packetHeader);
        unicastMessagePacket.setSenderUsername(packetData[1]);
        unicastMessagePacket.setRecipientUsername(packetData[2]);
        unicastMessagePacket.setContent(packetData[3]);

        return unicastMessagePacket;
    }

    /**
     * Decodes the multicast message packet that has the following semantics:
     * [header,sender,content].
     */
    private MulticastMessagePacket decodeMulticastMessageData(Packet.HeaderType packetHeader, String[] packetData) {
        MulticastMessagePacket multicastMessagePacket = new MulticastMessagePacket(packetHeader);
        multicastMessagePacket.setSenderUsername(packetData[1]);
        multicastMessagePacket.setContent(packetData[2]);

        return multicastMessagePacket;
    }

    /**
     * Decodes the message result packet that has the following semantics:
     * [header,receiveDate].
     */
    private MessageResultPacket decodeMessageResult(Packet.HeaderType packetHeader, String[] packetData) {
        MessageResultPacket messageResultPacket = new MessageResultPacket(packetHeader);
        messageResultPacket.setReceiveDate(packetData[1]);

        return messageResultPacket;
    }

    /**
     * Decodes the online users packet that has the following semantics:
     * [header,usersList].
     */
    private OnlineUsersPacket decodeOnlineUsersData(Packet.HeaderType packetHeader, String[] packetData) {
        OnlineUsersPacket onlineUsersPacket = new OnlineUsersPacket(packetHeader);
        String[] users = packetData[1].split(Constants.COMMA_SEPARATOR);

        for (String user : users) {
            onlineUsersPacket.addUser(user);
        }

        return onlineUsersPacket;
    }

    /**
     * Decodes the ban status packet that has the following semantics:
     * [header,isBanned].
     */
    private BanPacket decodeBanStatus(Packet.HeaderType packetHeader, String[] packetData) {
        BanPacket banPacket = new BanPacket(packetHeader);
        banPacket.setBanned(Boolean.valueOf(packetData[1]));

        return banPacket;
    }

    /**
     * Decodes the error message packet that has the following semantics:
     * [header,errorMessage].
     */
    private ErrorPacket decodeErrorMessage(Packet.HeaderType packetHeader, String[] packetData) {
        ErrorPacket errorPacket = new ErrorPacket(packetHeader);
        errorPacket.setErrorMessage(packetData[1]);

        return errorPacket;
    }
}
