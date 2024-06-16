package expression;

import expression.exceptions.CheckedMultiply;
import expression.generic.Calculator;

public class Multiply extends BinaryOperation {
    public static class MultiplyInfo extends BinaryOperationInfo {
        public MultiplyInfo() {
            super(Priority.MULTIPLY);
        }

        @Override
        public Multiply getBinaryOperationInstance(AnyExpression leftExpression, AnyExpression rightExpression, boolean isChecked) {
            if (isChecked) {
                return new CheckedMultiply(leftExpression, rightExpression);
            } else {
                return new Multiply(leftExpression, rightExpression);
            }
        }
    }

    public Multiply(AnyExpression leftExpression, AnyExpression rightExpression) {
        super("*", Priority.MULTIPLY, leftExpression, rightExpression);
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof Multiply multiply) {
            return leftExpression.equals(multiply.leftExpression) && rightExpression.equals(multiply.rightExpression);
        }
        return false;
    }

    @Override
    public String toMiniString() {
        String leftString = leftExpression.toMiniString();
        String rightString = rightExpression.toMiniString();

        if (leftExpression.priority < priority) {
            leftString = "(" + leftString + ")";
        }
        if (rightExpression.priority < priority ||
                (rightExpression.priority == priority && rightExpression instanceof Divide)) {
            rightString = "(" + rightString + ")";
        }
        return leftString + " " + operation + " " + rightString;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return leftExpression.evaluate(x, y, z) * rightExpression.evaluate(x, y, z);
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return calculator.multiply(leftExpression.evaluateGeneric(x, y, z, calculator),
                rightExpression.evaluateGeneric(x, y, z, calculator));
    }
}
