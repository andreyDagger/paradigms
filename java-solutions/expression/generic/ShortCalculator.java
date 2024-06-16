package expression.generic;

public class ShortCalculator extends AbstractCalculator<Short> {

    @Override
    public Short add(Short a, Short b) {
        return (short) (a + b);
    }

    @Override
    public Short subtract(Short a, Short b) {
        return (short) (a - b);
    }

    @Override
    public Short multiply(Short a, Short b) {
        return (short)(a * b);
    }

    @Override
    public Short divide(Short a, Short b) {
        return (short)(a / b);
    }

    @Override
    public Short negate(Short a) {
        return (short)(-a);
    }

    @Override
    public Short abs(Short a) {
        return (short)(Math.abs(a));
    }

    @Override
    public Short mod(Short a, Short b) {
        return (short)(a % b);
    }

    @Override
    public Short getConst(Number T) {
        return (short)(T.intValue());
    }
}
