package expression;

import expression.generic.Calculator;

public class Const extends AnyExpression {
    private final Number value;

    public Const(int value) {
        super(Priority.CONST);
        this.value = value;
    }

    public Const(Number value) {
        super(Priority.CONST);
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof Const constant) {
            return constant.value.equals(value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return calculator.getConst(value);
    }
}
