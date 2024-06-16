package expression;

import expression.generic.Calculator;

import static expression.MyMath.multiplyExact;
import static expression.MyMath.addExact;

public class UnaryReverse extends UnaryOperation {
    public UnaryReverse(AnyExpression expression) {
        super("reverse", Priority.UNARY_REVERSE, expression);
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return null;
    }

    public static class UnaryReverseInfo extends UnaryOperationInfo {
        public UnaryReverseInfo() {
            super(Priority.UNARY_REVERSE);
        }

        @Override
        public UnaryReverse getUnaryOperationInstance(AnyExpression expression, boolean isChecked) {
            if (isChecked) {
                return new UnaryReverse(expression);
            } else {
                throw new AssertionError("Only checked reverse exists");
            }
        }
    }

    private int reverse(int x) {
        int result = 0;
        String strX = Long.toString(Math.abs((long)x));
        for (int i = strX.length() - 1; i >= 0; i--) {
            result = addExact(multiplyExact(10, result), Integer.parseInt(Character.toString(strX.charAt(i))));
        }
        return multiplyExact(result, MyMath.sign(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return reverse(expression.evaluate(x, y, z));
    }
}
