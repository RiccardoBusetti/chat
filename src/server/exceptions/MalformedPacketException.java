package server.exceptions;

/**
 * Exception thrown when the packet is malformed.
 */
public class MalformedPacketException extends Throwable {
    private boolean isDecoding;

    public MalformedPacketException(String message, boolean isDecoding) {
        super(message);
        this.isDecoding = isDecoding;
    }

    @Override
    public String getMessage() {
        return "Malformed packet during " + (isDecoding ? "decoding" : "encoding") + ": " + super.getMessage();
    }
}
