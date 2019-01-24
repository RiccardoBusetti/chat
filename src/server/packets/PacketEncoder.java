package server.packets;

import server.constants.Constants;
import server.entities.packets.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible of encoding the outcoming packets.
 */
public class PacketEncoder {

    public String encode(Packet packet) {
        return encodePacket(packet);
    }

    private String encodePacket(Packet packet) {
        List<String> stringsToEncode = new ArrayList<>();

        PacketHeaderHelper packetHeaderHelper = new PacketHeaderHelper();
        String packetHeader = packetHeaderHelper.encodeHeader(packet.getHeaderType());

        stringsToEncode.add(packetHeader);

        switch (packet.getHeaderType()) {
            case LOGIN_RESULT:
                encodeAccessResult(stringsToEncode, (AccessResultPacket) packet);
                break;
            case REGISTER_RESULT:
                encodeAccessResult(stringsToEncode, (AccessResultPacket) packet);
                break;
            case UNICAST_MESSAGE:
                encodeUnicastMessage(stringsToEncode, (UnicastMessagePacket) packet);
                break;
            case MULTICAST_MESSAGE:
                encodeMulticastMessage(stringsToEncode, (MulticastMessagePacket) packet);
                break;
            case BAN_STATUS:
                encodeBanStatus(stringsToEncode, (BanPacket) packet);
                break;
        }

        return encodeStrings(stringsToEncode);
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
    private void encodeUnicastMessage(List<String> stringsToEncode, UnicastMessagePacket unicastMessagePacket) {
        stringsToEncode.add(unicastMessagePacket.getSenderUsername());
        stringsToEncode.add(unicastMessagePacket.getRecipientUsername());
        stringsToEncode.add(unicastMessagePacket.getContent());
    }

    /**
     * Encodes the multicast message packet with the following semantics:
     * [header,sender,content].
     */
    private void encodeMulticastMessage(List<String> stringsToEncode, MulticastMessagePacket multicastMessagePacket) {
        stringsToEncode.add(multicastMessagePacket.getSenderUsername());
        stringsToEncode.add(multicastMessagePacket.getContent());
    }

    /**
     * Encodes the ban status packet with the following semantics:
     * [header,status].
     */
    private void encodeBanStatus(List<String> stringsToEncode, BanPacket banPacket) {
        stringsToEncode.add(String.valueOf(banPacket.isBanned()));
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
            if (i < stringsToEncode.size() - 1) stringBuilder.append(Constants.DIVIDE_REGEX);
        }

        return stringBuilder.toString();
    }

}
