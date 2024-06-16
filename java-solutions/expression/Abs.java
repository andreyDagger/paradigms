package expression;

import expression.generic.Calculator;

public class Abs extends UnaryOperation {
    public Abs(AnyExpression expression) {
        super("abs", Priority.ABS, expression);
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return calculator.abs(expression.evaluateGeneric(x, y, z, calculator));
    }

    public static class AbsInfo extends UnaryOperationInfo {
        public AbsInfo() {
            super(Priority.ABS);
        }

        @Override
        public Abs getUnaryOperationInstance(AnyExpression expression, boolean isChecked) {
            if (isChecked) {
                return new Abs(expression);
            } else {
                throw new AssertionError("Only checked Abs exists");
            }
        }
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return MyMath.absExact(expression.evaluate(x, y, z));
    }
}
