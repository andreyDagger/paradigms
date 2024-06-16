package expression.generic;

import java.math.BigInteger;

public class BigIntegerCalculator extends AbstractCalculator<BigInteger> {
    @Override
    public BigInteger add(BigInteger a, BigInteger b) {
        return a.add(b);
    }

    @Override
    public BigInteger subtract(BigInteger a, BigInteger b) {
        return a.subtract(b);
    }

    @Override
    public BigInteger multiply(BigInteger a, BigInteger b) {
        return a.multiply(b);
    }

    @Override
    public BigInteger divide(BigInteger a, BigInteger b) {
        return a.divide(b);
    }

    @Override
    public BigInteger negate(BigInteger a) {
        return a.negate();
    }

    @Override
    public BigInteger abs(BigInteger a) {
        return a.abs();
    }

    @Override
    public BigInteger mod(BigInteger a, BigInteger b) {
        return a.mod(b);
    }

    @Override
    public BigInteger getConst(Number T) {
        // :NOTE: intValue не сработает с большими константами
        return new BigInteger(Integer.toString(T.intValue()));
    }
}
