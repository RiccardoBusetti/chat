package server.packets;

import server.entities.packets.Packet;

import java.util.HashMap;
import java.util.Map;

public class PacketHeaderHelper {

    private Map<String, Packet.HeaderType> headersMap;
    private Map<Packet.HeaderType, String> headersInverseMap;

    public PacketHeaderHelper() {
        this.headersMap = new HashMap<>();
        this.headersInverseMap = new HashMap<>();
        init();
    }

    private void init() {
        initMap();
        initInverseMap();
    }

    private void initMap() {
        headersMap.put("Login", Packet.HeaderType.LOGIN_DATA);
        headersMap.put("LoginResult", Packet.HeaderType.LOGIN_RESULT);
        headersMap.put("Register", Packet.HeaderType.REGISTER_DATA);
        headersMap.put("RegisterResult", Packet.HeaderType.REGISTER_RESULT);
        headersMap.put("UniMsg", Packet.HeaderType.UNICAST_MESSAGE);
        headersMap.put("MultiMsg", Packet.HeaderType.MULTICAST_MESSAGE);
        headersMap.put("BanStatus", Packet.HeaderType.BAN_STATUS);
    }

    private void initInverseMap() {
        headersInverseMap.put(Packet.HeaderType.LOGIN_DATA, "Login");
        headersInverseMap.put(Packet.HeaderType.LOGIN_RESULT, "LoginResult");
        headersInverseMap.put(Packet.HeaderType.REGISTER_DATA, "Register");
        headersInverseMap.put(Packet.HeaderType.REGISTER_RESULT, "RegisterResult");
        headersInverseMap.put(Packet.HeaderType.UNICAST_MESSAGE, "UniMsg");
        headersInverseMap.put(Packet.HeaderType.MULTICAST_MESSAGE, "MultiMsg");
        headersInverseMap.put(Packet.HeaderType.BAN_STATUS, "BanStatus");
    }

    public Packet.HeaderType decodeHeader(String header) {
        return headersMap.get(header);
    }

    public String encodeHeader(Packet.HeaderType header) {
        return headersInverseMap.get(header);
    }
}
