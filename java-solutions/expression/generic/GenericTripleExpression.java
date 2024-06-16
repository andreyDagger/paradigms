package expression.generic;

public interface GenericTripleExpression {
    <T extends Number> T evaluateGeneric(T x, T y, T z, Calculator<T> calculator);
}
