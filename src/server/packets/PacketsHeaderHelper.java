package server.packets;

import server.constants.Constants;
import server.entities.packets.Packet;

import java.util.HashMap;
import java.util.Map;

public class PacketsHeaderHelper {

    private Map<String, Packet.HeaderType> headersMap;
    private Map<Packet.HeaderType, String> headersInverseMap;

    public PacketsHeaderHelper() {
        this.headersMap = new HashMap<>();
        this.headersInverseMap = new HashMap<>();
        init();
    }

    private void init() {
        initMap();
        initInverseMap();
    }

    private void initMap() {
        headersMap.put(Constants.LOGIN_DATA, Packet.HeaderType.LOGIN_DATA);
        headersMap.put(Constants.LOGIN_RESULT, Packet.HeaderType.LOGIN_RESULT);
        headersMap.put(Constants.REGISTER_DATA, Packet.HeaderType.REGISTER_DATA);
        headersMap.put(Constants.REGISTER_RESULT, Packet.HeaderType.REGISTER_RESULT);
        headersMap.put(Constants.UNICAST_MESSAGE, Packet.HeaderType.UNICAST_MESSAGE);
        headersMap.put(Constants.MULTICAST_MESSAGE, Packet.HeaderType.MULTICAST_MESSAGE);
        headersMap.put(Constants.BAN_STATUS, Packet.HeaderType.BAN_STATUS);
        headersMap.put(Constants.ERROR_MESSAGE, Packet.HeaderType.ERROR_MESSAGE);
    }

    private void initInverseMap() {
        headersInverseMap.put(Packet.HeaderType.LOGIN_DATA, Constants.LOGIN_DATA);
        headersInverseMap.put(Packet.HeaderType.LOGIN_RESULT, Constants.LOGIN_RESULT);
        headersInverseMap.put(Packet.HeaderType.REGISTER_DATA, Constants.REGISTER_DATA);
        headersInverseMap.put(Packet.HeaderType.REGISTER_RESULT, Constants.REGISTER_RESULT);
        headersInverseMap.put(Packet.HeaderType.UNICAST_MESSAGE, Constants.UNICAST_MESSAGE);
        headersInverseMap.put(Packet.HeaderType.MULTICAST_MESSAGE, Constants.MULTICAST_MESSAGE);
        headersInverseMap.put(Packet.HeaderType.BAN_STATUS, Constants.BAN_STATUS);
        headersInverseMap.put(Packet.HeaderType.ERROR_MESSAGE, Constants.ERROR_MESSAGE);
    }

    public Packet.HeaderType decodeHeader(String header) {
        return headersMap.get(header);
    }

    public String encodeHeader(Packet.HeaderType header) {
        return headersInverseMap.get(header);
    }
}
