package expression.exceptions;

import expression.TripleExpression;
import expression.generic.GenericTripleExpression;
import expression.generic.NumberParser;
import expression.myExceptions.ParseException;

public class ExpressionParser implements TripleParser {
    public TripleExpression parse(String expression) throws ParseException {
        //System.out.println(expression);
        return new RealExpressionParser(expression, true).parse();
    }

    public GenericTripleExpression parseGeneric(String expression, NumberParser numberParser) throws ParseException {
        return new RealExpressionParser(expression, true, numberParser).parse();
    }
}
