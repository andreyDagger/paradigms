package expression;

import expression.generic.Calculator;

public class Square extends UnaryOperation {
    public Square(AnyExpression expression) {
        super("square", Priority.SQUARE, expression);
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return calculator.square(expression.evaluateGeneric(x, y, z, calculator));
    }

    public static class SquareInfo extends UnaryOperationInfo {
        public SquareInfo() {
            super(Priority.SQUARE);
        }

        @Override
        public Square getUnaryOperationInstance(AnyExpression expression, boolean isChecked) {
            if (isChecked) {
                return new Square(expression);
            } else {
                throw new AssertionError("Only checked Abs exists");
            }
        }
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return MyMath.square(expression.evaluate(x, y, z));
    }
}
