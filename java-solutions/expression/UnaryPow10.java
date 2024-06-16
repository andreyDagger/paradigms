package expression;

import expression.generic.Calculator;

public class UnaryPow10 extends UnaryOperation {
    public UnaryPow10(AnyExpression expression) {
        super("pow10", Priority.UNARY_POW10, expression);
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return null;
    }

    public static class UnaryPow10Info extends UnaryOperationInfo {
        public UnaryPow10Info() {
            super(Priority.UNARY_POW10);
        }

        @Override
        public UnaryPow10 getUnaryOperationInstance(AnyExpression expression, boolean isChecked) {
            if (isChecked) {
                return new UnaryPow10(expression);
            } else {
                throw new AssertionError("Only checked pow10 exists");
            }
        }
    }

    @Override
    public int evaluate(int x, int y, int z) {
        //System.out.println("For: " + expression.toString());
        return MyMath.pow10(expression.evaluate(x, y, z));
    }
}
