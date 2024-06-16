package expression.myExceptions;

public class InvalidCharacterException extends ParseException {
    public InvalidCharacterException(String message) {
        super(message);
    }
}
