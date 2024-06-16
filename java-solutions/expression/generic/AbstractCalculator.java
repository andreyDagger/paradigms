package expression.generic;

public abstract class AbstractCalculator<T extends Number> implements Calculator<T> {

    @Override
    public T square(T a) {
        return multiply(a, a);
    }
}
