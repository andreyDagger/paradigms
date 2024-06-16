package expression.exceptions;

import expression.AnyExpression;
import expression.MyMath;
import expression.UnaryMinus;

public class CheckedNegate extends UnaryMinus {
    public CheckedNegate(AnyExpression expression) {
        super(expression);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return MyMath.negateExact(expression.evaluate(x, y, z));
    }
}
