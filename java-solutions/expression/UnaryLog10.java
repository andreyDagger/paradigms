package expression;

import base.Log;
import expression.exceptions.CheckedAdd;
import expression.generic.Calculator;

public class UnaryLog10 extends UnaryOperation {
    public UnaryLog10(AnyExpression expression) {
        super("log10", Priority.UNARY_LOG10, expression);
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return null;
    }

    public static class UnaryLog10Info extends UnaryOperationInfo {
        public UnaryLog10Info() {
            super(Priority.UNARY_LOG10);
        }

        @Override
        public UnaryLog10 getUnaryOperationInstance(AnyExpression expression, boolean isChecked) {
            if (isChecked) {
                return new UnaryLog10(expression);
            } else {
                throw new AssertionError("Only checked Log10 exists");
            }
        }
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return MyMath.logi10(expression.evaluate(x, y, z));
    }
}