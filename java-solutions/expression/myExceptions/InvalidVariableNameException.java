package expression.myExceptions;

public class InvalidVariableNameException extends ParseException {
    private final String varName;

    public InvalidVariableNameException(String message, String varName) {
        super(message);
        this.varName = varName;
    }

    public String getVarName() {
        return varName;
    }
}
