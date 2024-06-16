package expression.exceptions;

public interface CharSource {
    boolean hasNext();
    char next();
    char[] read(int count);
    IllegalArgumentException error(String message);
}
