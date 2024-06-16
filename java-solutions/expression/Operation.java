package expression;

public abstract class Operation extends AnyExpression {
    protected final String operation;

    public static abstract class OperationInfo {
        private final int priority;

        protected OperationInfo(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }

    public Operation(String operation, int priority) {
        super(priority);
        this.operation = operation;
    }
}
