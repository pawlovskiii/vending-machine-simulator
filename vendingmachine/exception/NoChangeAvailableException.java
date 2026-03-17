package vendingmachine.exception;

public class NoChangeAvailableException extends RuntimeException {
    public NoChangeAvailableException(String message) {
        super(message);
    }
}
