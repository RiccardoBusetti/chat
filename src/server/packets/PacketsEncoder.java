package server.packets;

import server.constants.Constants;
import server.entities.packets.*;
import server.exceptions.MalformedPacketException;
import server.logging.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible of encoding the outcoming packets.
 */
public class PacketsEncoder {

    /**
     * Decodes a Java POJO packet into a string packet that will be
     * sent with the sockets.
     *
     * @param packet packet that needs to be encoded.
     * @return the encoded packet as a string.
     */
    public String encode(Packet packet) {
        try {
            Logger.logStatus(this, "Encoding packet.");

            return encodePacket(packet);
        } catch (MalformedPacketException exc) {
            Logger.logError(this, exc.getMessage());
        }

        return new PacketsHeaderHelper().encodeHeader(Packet.HeaderType.EMPTY_PACKET);
    }

    private String encodePacket(Packet packet) throws MalformedPacketException {
        validatePacket(packet);

        List<String> stringsToEncode = new ArrayList<>();

        PacketsHeaderHelper packetsHeaderHelper = new PacketsHeaderHelper();
        String packetHeader = packetsHeaderHelper.encodeHeader(packet.getHeaderType());

        stringsToEncode.add(packetHeader);

        switch (packet.getHeaderType()) {
            case LOGIN_DATA:
                encodeAccessData(stringsToEncode, (AccessPacket) packet);
                break;
            case LOGIN_RESULT:
                encodeAccessResult(stringsToEncode, (AccessResultPacket) packet);
                break;
            case REGISTER_DATA:
                encodeAccessData(stringsToEncode, (AccessPacket) packet);
                break;
            case REGISTER_RESULT:
                encodeAccessResult(stringsToEncode, (AccessResultPacket) packet);
                break;
            case UNICAST_MESSAGE_DATA:
                encodeUnicastMessageData(stringsToEncode, (UnicastMessagePacket) packet);
                break;
            case MULTICAST_MESSAGE_DATA:
                encodeMulticastMessageData(stringsToEncode, (MulticastMessagePacket) packet);
                break;
            case MESSAGE_RESULT:
                encodeMessageResult(stringsToEncode, (MessageResultPacket) packet);
                break;
            case ONLINE_USERS_DATA:
                encodeOnlineUsersData(stringsToEncode, (OnlineUsersPacket) packet);
                break;
            case BAN_STATUS:
                encodeBanStatus(stringsToEncode, (BanPacket) packet);
                break;
            case ERROR_MESSAGE:
                encodeErrorMessage(stringsToEncode, (ErrorPacket) packet);
                break;
            // If the packet is empty we won't do anything because the empty packet
            // has already been appended and since the empty packet has no content
            // we don't need to do anything.
            case EMPTY_PACKET:
                break;
        }

        return encodeStrings(stringsToEncode);
    }

    /**
     * Validates the Java POJO representing the packet.
     *
     * @param packet packet represented in a Java POJO.
     * @throws MalformedPacketException thrown if the packet is not correctly made.
     */
    private void validatePacket(Packet packet) throws MalformedPacketException {
        if (packet == null) throw new MalformedPacketException("The packet is null.", true);
        // Checking if the packet header equals to null in order
        // to prevent it from being encoded.
        if (packet.getHeaderType() == null) {
            throw new MalformedPacketException("The packet header is null.", false);
        }
    }

    /**
     * Encodes the access data packet with the following semantics:
     * [header,username,password].
     */
    private void encodeAccessData(List<String> stringsToEncode, AccessPacket accessPacket) {
        stringsToEncode.add(accessPacket.getUsername());
        stringsToEncode.add(accessPacket.getPassword());
    }

    /**
     * Encodes the access result packet with the following semantics:
     * [header,result].
     */
    private void encodeAccessResult(List<String> stringsToEncode, AccessResultPacket accessResultPacket) {
        stringsToEncode.add(String.valueOf(accessResultPacket.isAllowed()));
    }

    /**
     * Encodes the unicast message packet with the following semantics:
     * [header,sender,recipient,content].
     */
    private void encodeUnicastMessageData(List<String> stringsToEncode, UnicastMessagePacket unicastMessagePacket) {
        stringsToEncode.add(unicastMessagePacket.getSenderUsername());
        stringsToEncode.add(unicastMessagePacket.getRecipientUsername());
        stringsToEncode.add(unicastMessagePacket.getContent());
    }

    /**
     * Encodes the multicast message packet with the following semantics:
     * [header,sender,content].
     */
    private void encodeMulticastMessageData(List<String> stringsToEncode, MulticastMessagePacket multicastMessagePacket) {
        stringsToEncode.add(multicastMessagePacket.getSenderUsername());
        stringsToEncode.add(multicastMessagePacket.getContent());
    }

    /**
     * Encodes the message result packet with the following semantics:
     * [header,receiveDate].
     */
    private void encodeMessageResult(List<String> stringsToEncode, MessageResultPacket messageResultPacket) {
        stringsToEncode.add(messageResultPacket.getReceiveDate());
    }

    /**
     * Encodes the online users packet with the following semantics:
     * [header,usersList].
     */
    private void encodeOnlineUsersData(List<String> stringsToEncode, OnlineUsersPacket onlineUsersPacket) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < onlineUsersPacket.getUsers().size(); i++) {
            stringBuilder.append(onlineUsersPacket.getUsers().get(i));
            if (i < onlineUsersPacket.getUsers().size() - 1) stringBuilder.append(Constants.COMMA_SEPARATOR);
        }

        stringsToEncode.add(stringBuilder.toString());
    }

    /**
     * Encodes the ban status packet with the following semantics:
     * [header,status].
     */
    private void encodeBanStatus(List<String> stringsToEncode, BanPacket banPacket) {
        stringsToEncode.add(String.valueOf(banPacket.isBanned()));
    }

    /**
     * Encodes the error packet with the following semantics:
     * [header,errorMessage].
     */
    private void encodeErrorMessage(List<String> stringsToEncode, ErrorPacket errorPacket) {
        stringsToEncode.add(errorPacket.getErrorMessage());
    }

    /**
     * Adds the divider regex to the different message parts.
     *
     * @param stringsToEncode list of the strings to divide with the regex.
     * @return a string containing the divider for each part.
     */
    private String encodeStrings(List<String> stringsToEncode) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < stringsToEncode.size(); i++) {
            stringBuilder.append(stringsToEncode.get(i));
            // We are going to append the divider on all the strings besides the last one.
            if (i < stringsToEncode.size() - 1) stringBuilder.append(PacketsUtils.hexToAscii(Constants.DIVIDE_REGEX));
        }

        return stringBuilder.toString();
    }
}
