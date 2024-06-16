package expression;

import expression.generic.Calculator;

public class Lcm extends BinaryOperation {
    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return null;
    }

    public static class LcmInfo extends BinaryOperationInfo {
        public LcmInfo() {
            super(Priority.LCM);
        }

        @Override
        public Lcm getBinaryOperationInstance(AnyExpression leftExpression, AnyExpression rightExpression, boolean isChecked) {
            return new Lcm(leftExpression, rightExpression);
        }
    }

    public Lcm(AnyExpression leftExpression, AnyExpression rightExpression) {
        super("lcm", Priority.LCM, leftExpression, rightExpression);
    }

    @Override
    public String toMiniString() {
        String leftString = leftExpression.toMiniString();
        String rightString = rightExpression.toMiniString();

        if (leftExpression.priority < priority) {
            leftString = "(" + leftString + ")";
        }
        if (rightExpression.priority < priority || (rightExpression.priority == priority && rightExpression instanceof Gcd)) {
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
        return MyMath.calculateLcm(leftExpression.evaluate(x, y, z), rightExpression.evaluate(x, y, z));
    }
}
