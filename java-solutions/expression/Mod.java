package expression;

import expression.exceptions.CheckedMultiply;
import expression.generic.Calculator;

public class Mod extends BinaryOperation {
    public static class ModInfo extends BinaryOperationInfo {
        public ModInfo() {
            super(Priority.MOD);
        }

        @Override
        public Mod getBinaryOperationInstance(AnyExpression leftExpression, AnyExpression rightExpression, boolean isChecked) {
            if (isChecked) {
                return new Mod(leftExpression, rightExpression);
            } else {
                assert false;
                return null;
            }
        }
    }

    public Mod(AnyExpression leftExpression, AnyExpression rightExpression) {
        super("mod", Priority.MOD, leftExpression, rightExpression);
    }

    @Override
    public boolean equals(Object expression) {
        if (expression instanceof Mod mod) {
            return leftExpression.equals(mod.leftExpression) && rightExpression.equals(mod.rightExpression);
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
        return leftExpression.evaluate(x, y, z) % rightExpression.evaluate(x, y, z);
    }

    @Override
    public <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator) {
        return calculator.mod(leftExpression.evaluateGeneric(x, y, z, calculator),
                rightExpression.evaluateGeneric(x, y, z, calculator));
    }
}
