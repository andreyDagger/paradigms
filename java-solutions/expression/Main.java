package expression;

import expression.exceptions.ExpressionParser;
import expression.exceptions.TripleParser;
import expression.generic.GenericTabulator;
import expression.generic.UnknownModeException;
import expression.myExceptions.ParseException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Must be at least 2 arguments");
            return;
        }
        String mode = args[0];
        String expression = args[1];
        Object[][][] result;
        try {
            result = new GenericTabulator().tabulate(mode.substring(1), expression, -2, 2, -2, 2, -2, 2);
        } catch (ParseException | UnknownModeException e) {
            System.err.println(e);
            return;
        }
        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j <= 4; j++) {
                for (int k = 0; k <= 4; k++) {
                    System.out.println((i - 2) + " " + (j - 2) + " " + (k - 2) + ": " + result[i][j][k]);
                }
            }
        }
    }
}
