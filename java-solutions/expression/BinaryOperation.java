package expression;

public abstract class BinaryOperation extends Operation {
    protected final AnyExpression leftExpression;
    protected final AnyExpression rightExpression;

    public static abstract class BinaryOperationInfo extends OperationInfo {
        protected BinaryOperationInfo(int priority) {
            super(priority);
        }

        public abstract Operation getBinaryOperationInstance(AnyExpression leftExpression,
                                                             AnyExpression rightExpression,
                                                             boolean isChecked);
    }

    protected BinaryOperation(String operation, int priority, AnyExpression leftExpression, AnyExpression rightExpression) {
        super(operation, priority);
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public String toString() {
        return "(" + leftExpression + " " + operation + " " + rightExpression + ")";
    }

    @Override
    public String toMiniString() {
        String leftString = leftExpression.toMiniString();
        String rightString = rightExpression.toMiniString();

        if (leftExpression.priority < priority) {
            leftString = "(" + leftString + ")";
        }
        if (rightExpression.priority <= priority) {
            rightString = "(" + rightString + ")";
        }
        return leftString + " " + operation + " " + rightString;
    }

    @Override
    public int hashCode() {
        int result = 31 * leftExpression.hashCode() + rightExpression.hashCode();
        result ^= result >> 1;
        return result + 32 * 32 * operation.hashCode();
    }
}
