package expression.generic;

public class DoubleParser implements NumberParser {

    @Override
    public Number parseConst(String number) {
        return Double.parseDouble(number);
    }

    @Override
    public boolean isCorrectChar(char ch) {
        return Character.isDigit(ch) || ch == '.';
    }
}
