package expression.myExceptions;

public class UnexpectedEofException extends ParseException {
    public UnexpectedEofException(String message) {
        super(message);
    }
}
