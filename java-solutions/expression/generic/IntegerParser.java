package expression.generic;

public class IntegerParser implements NumberParser {

    @Override
    public Number parseConst(String number) {
        return Integer.parseInt(number);
    }

    @Override
    public boolean isCorrectChar(char ch) {
        return Character.isDigit(ch);
    }
}
