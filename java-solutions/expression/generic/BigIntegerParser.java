package expression.generic;

import java.math.BigInteger;

public class BigIntegerParser implements NumberParser {
    @Override
    public Number parseConst(String number) {
        return new BigInteger(number);
    }

    @Override
    public boolean isCorrectChar(char ch) {
        return Character.isDigit(ch);
    }
}
