package expression.myExceptions;

public class BadUnaryOperationException extends ParseException {
    public BadUnaryOperationException(String message) {
        super(message);
    }
}
