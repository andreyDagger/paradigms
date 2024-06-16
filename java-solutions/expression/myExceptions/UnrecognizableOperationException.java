package expression.myExceptions;

public class UnrecognizableOperationException extends ParseException {
    public UnrecognizableOperationException(String message) {
        super(message);
    }
}
