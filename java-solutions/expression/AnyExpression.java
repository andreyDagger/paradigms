package expression;

import expression.generic.GenericTripleExpression;

public abstract class AnyExpression implements TripleExpression, GenericTripleExpression {
    protected final int priority;

    public AnyExpression(int priority) {
        this.priority = priority;
    }
}
