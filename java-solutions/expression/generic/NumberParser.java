package expression.generic;

public interface NumberParser {
    Number parseConst(String number);
    boolean isCorrectChar(char ch);
}
