package expression.exceptions;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseParser {
    private static final char END = '\0';
    private final CharSource source;
    private char ch = 0xffff;
    private char prev = 0xffff;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected char look() {
        return ch;
    }

    protected char lookPrev() {
        return prev;
    }

    protected char take() {
        prev = ch;
        ch = source.hasNext() ? source.next() : END;
        return prev;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean test(final String expected) {
        char[] readArray = source.read(expected.length());
        if (readArray[0] == 0) {
            return false;
        }
        return new String(readArray).equals(expected);
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean take(final String expected) {
        if (test(expected)) {
            for (int i = 0; i < expected.length(); i++) {
                take();
            }
            return true;
        }
        return false;
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return take(END);
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
