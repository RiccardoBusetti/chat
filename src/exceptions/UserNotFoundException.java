package exceptions;

public class UserNotFoundException extends Throwable {

    @Override
    public String getMessage() {
        return "User cannot be found.";
    }
}
