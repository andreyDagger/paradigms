package expression;

import expression.exceptions.CheckedNegate;
import expression.generic.Calculator;

public class UnaryMinus extends UnaryOperation {
    public UnaryMinus(AnyExpression expression) {
        super("-", Priority.UNARY_MINUS, expression);
    }

    public static class UnaryMinusInfo extends UnaryOperationInfo {
        public UnaryMinusInfo() {
            super(Priority.UNARY_MINUS);
        }

        @Override
        public UnaryMinus getUnaryOperationInstance(AnyExpression expression, boolean isChecked) {
            if (isChecked) {
                return new CheckedNegate(expression);
            } else {
                return new UnaryMinus(expression);
            }
        }
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -expression.evaluate(x, y, z);
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return calculator.negate(expression.evaluateGeneric(x, y, z, calculator));
    }
}
