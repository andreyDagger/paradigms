package expression.generic;

import expression.MyMath;

public class ModularCalculator extends AbstractCalculator<Integer> {

    private static final int MOD = 10079;

    private Integer normalize(Integer a) {
        if (a >= 0) {
            return a % MOD;
        } else {
            return MOD + a % MOD;
        }
    }

    @Override
    public Integer add(Integer a, Integer b) {
        return (normalize(a) + normalize(b)) % MOD;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        return (normalize(a) - normalize(b) + MOD) % MOD;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        return normalize(a) * normalize(b) % MOD;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return normalize(a) * MyMath.binpow(normalize(b), MOD - 2, MOD) % MOD;
    }

    @Override
    public Integer negate(Integer a) {
        return (MOD - normalize(a)) % MOD;
    }

    @Override
    public Integer abs(Integer a) {
        return normalize(a);
    }

    @Override
    public Integer mod(Integer a, Integer b) {
        return normalize(normalize(a) % normalize(b));
    }

    @Override
    public Integer getConst(Number T) {
        return normalize(T.intValue());
    }
}
