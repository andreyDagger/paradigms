package expression;

import expression.generic.Calculator;

public class Variable extends AnyExpression {
    private final String name;

    public Variable(String name) {
        super(Priority.VARIABLE);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof Variable variable) {
            return name.equals(variable.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toMiniString() {
        return name;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (name) {
            case "x" -> {
                return x;
            }
            case "y" -> {
                return y;
            }
            case "z" -> {
                return z;
            }
            default -> throw new AssertionError("This code never reached");
        }
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        switch (name) {
            case "x" -> {
                return calculator.getConst(x);
            }
            case "y" -> {
                return calculator.getConst(y);
            }
            case "z" -> {
                return calculator.getConst(z);
            }
            default -> throw new AssertionError("This code never reached");
        }
    }
}
