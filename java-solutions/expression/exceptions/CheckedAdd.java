package expression.exceptions;

import expression.Add;
import expression.AnyExpression;
import expression.MyMath;

public class CheckedAdd extends Add {
    public CheckedAdd(AnyExpression leftExpression, AnyExpression rightExpression) {
        super(leftExpression, rightExpression);
    }
    @Override
    public int evaluate(int x, int y, int z) {
        return MyMath.addExact(leftExpression.evaluate(x, y, z), rightExpression.evaluate(x, y, z));
    }
}
