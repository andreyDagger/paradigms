package expression.generic;

import expression.MyMath;

public class IntegerCalculator extends AbstractCalculator<Integer> {

    boolean unsigned;

    public IntegerCalculator(boolean unsigned) {
        this.unsigned = unsigned;
    }

    @Override
    public Integer add(Integer a, Integer b) {
        if (unsigned) {
            return a + b;
        }
        else {
            return MyMath.addExact(a, b);
        }
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        if (unsigned) {
            return a - b;
        } else {
            return MyMath.subtractExact(a, b);
        }
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        if (unsigned) {
            return a * b;
        } else {
            return MyMath.multiplyExact(a, b);
        }
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        if (unsigned) {
            return a / b;
        } else {
            return MyMath.divideExact(a, b);
        }
    }

    @Override
    public Integer negate(Integer a) {
        if (unsigned) {
            return -a;
        } else {
            return MyMath.negateExact(a);
        }
    }

    @Override
    public Integer abs(Integer a) {
        if (unsigned) {
            return Math.abs(a);
        } else {
            return MyMath.absExact(a);
        }
    }

    @Override
    public Integer mod(Integer a, Integer b) {
        return a % b;
    }

    @Override
    public Integer getConst(Number T) {
        return T.intValue();
    }
}
