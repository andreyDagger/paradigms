package expression.exceptions;

import expression.Variable;
import expression.myExceptions.InvalidVariableNameException;

public class CheckedVariable extends Variable {
    public CheckedVariable(String name) throws InvalidVariableNameException {
        super(name);
        if (!name.equals("x") && !name.equals("y") && !name.equals("z")) {
            throw new InvalidVariableNameException("Variable must be x, y or z", name);
        }
    }
}
