package expression;

import expression.exceptions.CheckedSubtract;
import expression.generic.Calculator;

public class Subtract extends BinaryOperation {

    public static class SubtractInfo extends BinaryOperationInfo {
        public SubtractInfo() {
            super(Priority.SUBTRACT);
        }

        @Override
        public Subtract getBinaryOperationInstance(AnyExpression leftExpression, AnyExpression rightExpression, boolean isChecked) {
            if (isChecked) {
                return new CheckedSubtract(leftExpression, rightExpression);
            } else {
                return new Subtract(leftExpression, rightExpression);
            }
        }
    }

    public Subtract(AnyExpression leftExpression, AnyExpression rightExpression) {
        super("-", Priority.SUBTRACT, leftExpression, rightExpression);
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof Subtract subtract) {
            return leftExpression.equals(subtract.leftExpression) && rightExpression.equals(subtract.rightExpression);
        }
        return false;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return leftExpression.evaluate(x, y, z) - rightExpression.evaluate(x, y, z);
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return calculator.subtract(leftExpression.evaluateGeneric(x, y, z, calculator),
                rightExpression.evaluateGeneric(x, y, z, calculator));
    }
}
