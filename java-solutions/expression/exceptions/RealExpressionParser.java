package expression.exceptions;

import expression.*;
import expression.generic.IntegerCalculator;
import expression.generic.IntegerParser;
import expression.generic.NumberParser;
import expression.myExceptions.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class RealExpressionParser extends BaseParser {

    private final static List<String> operationsStringList = List.of("+", "-", "*", "/", "gcd", "lcm",
            "reverse", "pow10", "log10", "abs", "square", "mod");

    private Element prevElement = Element.NONE;
    private final boolean throwsExceptions;
    private final NumberParser numberParser;

    public RealExpressionParser(CharSource source, boolean throwsExceptions, NumberParser numberParser) {
        super(source);
        this.numberParser = numberParser;
        this.throwsExceptions = throwsExceptions;
    }

    public RealExpressionParser(String source, boolean throwsExceptions) {
        this(source, throwsExceptions, new IntegerParser());
    }

    public RealExpressionParser(String source, boolean throwsExceptions, NumberParser numberParser) {
        this(new StringSource(source), throwsExceptions, numberParser);
    }

    public AnyExpression parse() throws ParseException {
        prevElement = Element.NONE;
        return parseExpression(false);
    }

    private AnyExpression parseExpression(boolean toFirstClosedBracket) throws ParseException {
        // toFirstClosedBracket - парсим до первой ")"
        List<AnyExpression> expressionList = new ArrayList<>();
        List<BinaryOperation.BinaryOperationInfo> operations = new ArrayList<>();

        while (true) {
            skipWhitespaces();
            if (take(')')) {
                if (toFirstClosedBracket) {
                    break;
                } else {
                    throw new BracketsBalanceException("brackets don't match");
                }
            }
            if (eof()) {
                if (toFirstClosedBracket) { // Не встретили ")"
                    throw new BracketsBalanceException("brackets don't match");
                } else {
                    break;
                }
            }
            if (operation() && !unary()) {
                operations.add(parseBinaryOperation());
            } else {
                expressionList.add(parseSingleToken());
            }
        }

        return buildExpression(expressionList, operations);
    }

    private UnaryOperation.UnaryOperationInfo parseUnaryOperation() throws UnrecognizableOperationException {
        String textOperation;
        UnaryOperation.UnaryOperationInfo result;
        if (take("pow10")) {
            textOperation = "pow10";
            result = new UnaryPow10.UnaryPow10Info();
        } else if (take("log10")) {
            textOperation = "log10";
            result = new UnaryLog10.UnaryLog10Info();
        } else if (take("reverse")) {
            textOperation = "reverse";
            result = new UnaryReverse.UnaryReverseInfo();
        } else if (take("-")) {
            textOperation = "-";
            result = new UnaryMinus.UnaryMinusInfo();
        } else if (take("abs")) {
            textOperation = "abs";
            result = new Abs.AbsInfo();
        } else if (take("square")) {
            textOperation = "square";
            result = new Square.SquareInfo();
        } else {
            throw new AssertionError("Not an unary operation");
        }

        if (textOperation.length() >= 2) {
            if (!correctCharAfterLongUnaryOperation(look())) {
                throw new UnrecognizableOperationException("Seems like you wanted to type " + textOperation +
                        " operation" + "but forgot to place whitespaces around her");
            }
        }
        return result;
    }

    private boolean unaryMinus() {
        return test('-') && (prevElement == Element.OPERATION ||
                prevElement == Element.NONE ||
                prevElement == Element.OPEN_BRACKET);
    }

    private boolean unary() {
        return unaryMinus() || test("reverse") || test("log10") || test("pow10") ||
                test("abs") || test("square");
    }

    private AnyExpression parseSingleToken() throws ParseException {
        //           Example:
        // 1 + (2 * -( reverse (4 + 5)))
        // Tokens: 1, 2 * -( reverse (4 + 5))
        //                       |
        //                       V
        //              Tokens: 2, -( reverse (4 + 5))
        //                               |
        //                               V
        //                         Tokens: 4, 5

        skipWhitespaces();
        if (take('(')) {
            prevElement = Element.OPEN_BRACKET;
            return parseExpression(true);
        } else if (take(')')) {
            throw new BracketsBalanceException("Brackets don't match");
        } else if (between('0', '9')) {
            return new Const(parseConst(false));
        } else if (operation()) {
            if (unary()) {
                prevElement = Element.OPERATION;
                UnaryOperation.UnaryOperationInfo unaryOperation = parseUnaryOperation();
                if (unaryOperation instanceof UnaryMinus.UnaryMinusInfo) {
                    if (between('0', '9')) {
                        return new Const(parseConst(true));
                    } else {
                        if (throwsExceptions) {
                            return new CheckedNegate(parseSingleToken());
                        } else {
                            return new UnaryMinus(parseSingleToken());
                        }
                    }
                } else {
                    return unaryOperation.getUnaryOperationInstance(parseSingleToken(), throwsExceptions);
                }
            } else {
                throw new BinaryOperationMatchingException("Binary operations don't match with variables and numbers");
            }
        } else if (variable()) {
            return parseVariable();
        } else if (eof()) {
            throw new UnexpectedEofException("Unexpected eof");
        }
        throw new InvalidCharacterException("Invalid character " + take() + ". ");
    }

    private AnyExpression parseVariable() throws InvalidVariableNameException {
        prevElement = Element.VARIABLE;
        if (throwsExceptions) {
            return new CheckedVariable(Character.toString(take()));
        } else {
            return new Variable(Character.toString(take()));
        }
    }

    private boolean variable() {
        return Character.isAlphabetic(look());
    }

    private BinaryOperation.BinaryOperationInfo parseBinaryOperation() throws UnrecognizableOperationException {
        prevElement = Element.OPERATION;
        char prevChar = lookPrev();
        String textOperation;
        BinaryOperation.BinaryOperationInfo result;

        if (take("gcd")) {
            textOperation = "gcd";
            result = new Gcd.GcdInfo();
        } else if (take("lcm")) {
            textOperation = "lcm";
            result = new Lcm.LcmInfo();
        } else if (take("*")) {
            textOperation = "*";
            result = new Multiply.MultiplyInfo();
        } else if (take("/")) {
            textOperation = "/";
            result = new Divide.DivideInfo();
        } else if (take("-")) {
            textOperation = "-";
            result = new Subtract.SubtractInfo();
        } else if (take("+")) {
            textOperation = "+";
            result = new Add.AddInfo();
        } else if (take("mod")) {
            textOperation = "mod";
            result = new Mod.ModInfo();
        } else {
            throw new AssertionError("Unknown binary operation");
        }

        if (textOperation.length() >= 2) {
            if (!correctCharBeforeLongBinaryOperation(prevChar)) { // Почему-то тесты считают, что 5lcm5 - это ошибка
                throw new UnrecognizableOperationException("Seems like you wanted to type " + textOperation + " operation " +
                        "but forgot to place whitespaces around her");
            }
        }

        return result;
    }

    private Number parseConst(boolean negated) {
        prevElement = Element.NUMBER;
        StringBuilder number = new StringBuilder();
        if (negated) {
            number.append('-');
        }
        while (true) {
            if (numberParser.isCorrectChar(look())) {
                number.append(take());
            } else {
                break;
            }
        }

        return numberParser.parseConst(number.toString());
    }

    private AnyExpression buildExpression(List<AnyExpression> expressionList,
                                          List<BinaryOperation.BinaryOperationInfo> operationList)
            throws BinaryOperationMatchingException{

        if (operationList.size() + 1 != expressionList.size()) {
            throw new BinaryOperationMatchingException("Binary operations don't match with variables and numbers");
        }
        if (operationList.isEmpty()) {
            return expressionList.get(0);
        }

        int min_priority = Integer.MAX_VALUE;
        for (BinaryOperation.BinaryOperationInfo binaryOperationInfo : operationList) {
            if (min_priority > binaryOperationInfo.getPriority()) {
                min_priority = binaryOperationInfo.getPriority();
            }
        }

        AnyExpression result = null;
        BinaryOperation.BinaryOperationInfo prevOperation = null;
        List<AnyExpression> expressionSublist = new ArrayList<>();
        List<BinaryOperation.BinaryOperationInfo> operationSublist = new ArrayList<>();
        for (int i = 0; i < expressionList.size(); i++) {
            expressionSublist.add(expressionList.get(i));
            if (i == expressionList.size() - 1 || operationList.get(i).getPriority() == min_priority) {
                AnyExpression subExpression = buildExpression(expressionSublist, operationSublist);
                if (prevOperation == null) {
                    result = subExpression;
                } else {
                    result = prevOperation.getBinaryOperationInstance(result, subExpression, throwsExceptions);
                }
                expressionSublist.clear();
                operationSublist.clear();
                if (i == expressionList.size() - 1) {
                    break;
                }
                prevOperation = operationList.get(i);
            } else {
                operationSublist.add(operationList.get(i));
            }
        }
        return result;
    }

    private boolean operation() {
        for (String op : operationsStringList) {
            if (test(op)) {
                return true;
            }
        }
        return false;
    }

    private void skipWhitespaces() {
        while (Character.isWhitespace(look())) {
            take();
        }
    }

    private boolean correctCharBeforeLongBinaryOperation(char ch) {
        return Character.isWhitespace(ch) || ch == ')';
    }

    private boolean correctCharAfterLongUnaryOperation(char ch) {
        return eof() || Character.isWhitespace(ch) || ch == '(';
    }
}