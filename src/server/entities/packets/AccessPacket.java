package server.entities.packets;

/**
 * POJO that represents the specific packet used to perform
 * access related operations like access or registration.
 */
public class AccessPacket extends Packet {
    private String username;
    private String password;

    public AccessPacket(boolean isLoginPacket) {
        super(isLoginPacket ? HeaderType.LOGIN_DATA : HeaderType.REGISTER_DATA);
    }

    public AccessPacket(boolean isLoginPacket, String username, String password) {
        this(isLoginPacket);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
