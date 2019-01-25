package server.exceptions;

/**
 * Exception thrown when the user is not found.
 */
public class UserNotFoundException extends Throwable {
    private boolean isRegisteredUser;

    public UserNotFoundException(boolean isRegisteredUser) {
        this.isRegisteredUser = isRegisteredUser;
    }

    public boolean isRegisteredUser() {
        return isRegisteredUser;
    }

    @Override
    public String getMessage() {
        return (isRegisteredUser ? "Registered user" : "Online user") + " cannot be found.";
    }
}
