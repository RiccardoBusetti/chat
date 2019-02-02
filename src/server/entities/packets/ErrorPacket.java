package server.entities.packets;

/**
 * POJO that represents a specific packet containing an
 * error happened in the server.
 */
public class ErrorPacket extends Packet {
    private String errorMessage;

    public ErrorPacket(HeaderType headerType) {
        super(headerType);
    }

    public ErrorPacket(HeaderType headerType, String errorMessage) {
        super(headerType);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
