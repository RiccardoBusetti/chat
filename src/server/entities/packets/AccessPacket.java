package server.entities.packets;

/**
 * POJO that represents the specific packet used to perform
 * access related operations like login or registration.
 */
public class AccessPacket extends Packet {
    private String username;
    private String password;
    private boolean isLogin;

    public AccessPacket() {
    }

    public AccessPacket(HeaderType headerType, String username, String password, boolean isLogin) {
        super(headerType);
        this.username = username;
        this.password = password;
        this.isLogin = isLogin;
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

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
