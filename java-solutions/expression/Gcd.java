package expression;

import expression.generic.Calculator;

public class Gcd extends BinaryOperation {
    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return null;
    }

    public static class GcdInfo extends BinaryOperationInfo {
        public GcdInfo() {
            super(Priority.GCD);
        }

        @Override
        public Gcd getBinaryOperationInstance(AnyExpression leftExpression, AnyExpression rightExpression, boolean isChecked) {
            return new Gcd(leftExpression, rightExpression);
        }
    }

    public Gcd(AnyExpression leftExpression, AnyExpression rightExpression) {
        super("gcd", Priority.GCD, leftExpression, rightExpression);
    }

    @Override
    public String toMiniString() {
        String leftString = leftExpression.toMiniString();
        String rightString = rightExpression.toMiniString();

        if (leftExpression.priority < priority) {
            leftString = "(" + leftString + ")";
        }
        if (rightExpression.priority < priority || (rightExpression.priority == priority && rightExpression instanceof Lcm)) {
            rightString = "(" + rightString + ")";
        }
        return leftString + " " + operation + " " + rightString;
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof Gcd gcd) {
            return leftExpression.equals(gcd.leftExpression) && rightExpression.equals(gcd.rightExpression);
        }
        return false;
    }
    @Override
    public int evaluate(int x, int y, int z) {
        return MyMath.calculateGcd(leftExpression.evaluate(x, y, z), rightExpression.evaluate(x, y, z));
    }
}
