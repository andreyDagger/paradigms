package expression;

import expression.exceptions.CheckedDivide;
import expression.exceptions.CheckedMultiply;
import expression.generic.Calculator;

public class Divide extends BinaryOperation {

    public static class DivideInfo extends BinaryOperationInfo {
        public DivideInfo() {
            super(Priority.DIVIDE);
        }

        @Override
        public Divide getBinaryOperationInstance(AnyExpression leftExpression, AnyExpression rightExpression, boolean isChecked) {
            if (isChecked) {
                return new CheckedDivide(leftExpression, rightExpression);
            } else {
                return new Divide(leftExpression, rightExpression);
            }
        }
    }

    public Divide(AnyExpression leftExpression, AnyExpression rightExpression) {
        super("/", Priority.DIVIDE, leftExpression, rightExpression);
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof Divide divide) {
            return leftExpression.equals(divide.leftExpression) && rightExpression.equals(divide.rightExpression);
        }
        return false;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return leftExpression.evaluate(x, y, z) / rightExpression.evaluate(x, y, z);
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return calculator.divide(leftExpression.evaluateGeneric(x, y, z, calculator),
                rightExpression.evaluateGeneric(x, y, z, calculator));
    }
}
