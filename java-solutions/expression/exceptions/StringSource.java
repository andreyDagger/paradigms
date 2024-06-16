package expression.exceptions;

public class StringSource implements CharSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
        this.pos = 0;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public char[] read(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than zero");
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (pos - 1 + i >= data.length()) {
                return new char[]{0};
            }
            result.append(data.charAt(pos - 1 + i));
        }
        return result.toString().toCharArray();
    }

    @Override
    public IllegalArgumentException error(final String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }
}
