package expression.exceptions;

import expression.AnyExpression;
import expression.Multiply;
import expression.MyMath;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(AnyExpression leftExpression, AnyExpression rightExpression) {
        super(leftExpression, rightExpression);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return MyMath.multiplyExact(leftExpression.evaluate(x, y, z), rightExpression.evaluate(x, y, z));
    }
}
