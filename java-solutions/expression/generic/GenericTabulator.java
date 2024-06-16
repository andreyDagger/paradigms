package expression.generic;

import expression.exceptions.ExpressionParser;
import expression.myExceptions.ParseException;

import java.math.BigInteger;

public class GenericTabulator implements Tabulator {

    private Object calculate(int i, int j, int k, String mode, GenericTripleExpression genericExpression) throws UnknownModeException {
        switch (mode) {
            case "i" -> {
                return genericExpression.evaluateGeneric(i, j, k, new IntegerCalculator(false));
            }
            case "d" -> {
                return genericExpression.evaluateGeneric((double)i, (double)j, (double)k, new DoubleCalculator());
            }
            case "bi" -> {
                return genericExpression.evaluateGeneric(new BigInteger(Integer.toString(i)),
                        new BigInteger(Integer.toString(j)), new BigInteger(Integer.toString(k)), new BigIntegerCalculator());
            }
            case "u" -> {
                return genericExpression.evaluateGeneric(i, j, k, new IntegerCalculator(true));
            }
            case "p" -> {
                return genericExpression.evaluateGeneric(i, j, k, new ModularCalculator());
            }
            case "s" -> {
                return genericExpression.evaluateGeneric((short)i, (short)j, (short)k, new ShortCalculator());
            }
            default -> throw new UnknownModeException("Unknown mode: " + mode);
        }
    }

    private NumberParser getParser(String mode) {
        return switch (mode) {
            case "d" -> new DoubleParser();
            case "bi" -> new BigIntegerParser();
            default -> new IntegerParser();
        };
    }

    public Object[][][] tabulate(String mode, String expression, int x1, int x2,
                                 int y1, int y2,
                                 int z1, int z2) throws ParseException, UnknownModeException {
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        NumberParser calculator = getParser(mode);
        GenericTripleExpression genericExpression = new ExpressionParser().parseGeneric(expression, calculator);
        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        result[i][j][k] = calculate(i + x1, j + y1, k + z1, mode, genericExpression);
                    } catch (ArithmeticException e) {
                        result[i][j][k] = null;
                    }
                }
            }
        }
        return result;
    }
}
