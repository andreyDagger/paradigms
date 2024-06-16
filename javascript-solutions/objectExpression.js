const NOT_FIXED_ARGS_COUNT = -1;

let add = (a, b) => (a + b);
let addDiff = (diffVar, a, b) => new Add(a.diff(diffVar), b.diff(diffVar));

let subtract = (a, b) => (a - b);
let subtractDiff = (diffVar, a, b) => new Subtract(a.diff(diffVar), b.diff(diffVar));

let multiply = (a, b) => (a * b);
let multiplyDiff = (diffVar, a, b) => new Add(new Multiply(a.diff(diffVar), b), new Multiply(a, b.diff(diffVar)));

let divide = (a, b) => (a / b);
let divideDiff = (diffVar, a, b) => new Divide(new Subtract(
                                                    new Multiply(a.diff(diffVar), b),
                                                    new Multiply(a, b.diff(diffVar))),
                                               new Multiply(b, b));

let negate = (a) => (-a);
let negateDiff = (diffVar, a) => new Negate(a.diff(diffVar));

let sumrec = (...args) => args.map(a => 1 / a).reduce(add, 0);
let sumrecDiff = (diffVar, ...args) => args.map(x => (new Divide(new Const(1), x))).reduce((a, b) => (new Add(a, b)), new Const(0)).diff(diffVar);

let hMean = (...args) => (args.length / sumrec(...args));
let hMeanDiff = (diffVar, ...args) => new Divide(new Const(args.length), new (SumrecCreator(args.length))(...args)).diff(diffVar);

let sqrtDiff = (diffVar, a) => (new Divide(a.diff(diffVar), new Multiply(new Const(2), new Sqrt(a))));

let meansq = (...args) => args.map(a => a * a).reduce(add, 0) / args.length;
let meansqDiff = (diffVar, ...args) => new Divide(args.map(x => new Multiply(x, x)).reduce((a, b) => (new Add(a, b)), new Const(0)), new Const(args.length)).diff(diffVar);

let rms = (...args) => Math.sqrt(meansq(...args));
let rmsDiff = (diffVar, ...args) => new Sqrt(new Meansq(...args)).diff(diffVar);

function AbstractOperation(...args) {
    this.args = args;
}
AbstractOperation.prototype.evaluate = function(...variableValues) {
    return this.operation(...this.args.map(expr => expr.evaluate(...variableValues)));
}
AbstractOperation.prototype.toString = function() {
    return this.args.map(v => v.toString()).join(" ") + " " + this.stringRepr;
}
AbstractOperation.prototype.diff = function(diffVar) {
    return this.diffRule(diffVar, ...this.args);
}
AbstractOperation.prototype.prefix = function() {
    return "(" + this.stringRepr + " " + this.args.map(v => v.prefix()).join(" ") + ")";
}
AbstractOperation.prototype.postfix = function() {
    return "(" + this.args.map(v => v.postfix()).join(" ") + " " + this.stringRepr + ")";
}
function operationCreator(operationConstructor, operation, argsCount, stringRepr, diffRule) {
    operationConstructor.prototype = Object.create(AbstractOperation.prototype);
    operationConstructor.prototype.operation = operation;
    operationConstructor.prototype.argsCount = argsCount;
    operationConstructor.prototype.stringRepr = stringRepr;
    operationConstructor.prototype.diffRule = diffRule;
}

function Add(left, right) {
     AbstractOperation.call(this, left, right);
}
operationCreator(Add, add, 2, "+", addDiff);

function Subtract(left, right) {
     AbstractOperation.call(this, left, right);
}
operationCreator(Subtract, subtract, 2, "-", subtractDiff);

function Multiply(left, right) {
     AbstractOperation.call(this, left, right);
}
operationCreator(Multiply, multiply, 2, "*", multiplyDiff);

function Divide(left, right) {
     AbstractOperation.call(this, left, right);
}
operationCreator(Divide, divide, 2, "/", divideDiff);

function Negate(expr) {
    AbstractOperation.call(this, expr);
}
operationCreator(Negate, negate, 1, "negate", negateDiff);

function Sqrt(expr) {
    AbstractOperation.call(this, expr);
}
operationCreator(Sqrt, Math.sqrt, 1, "sqrt", sqrtDiff);

function Meansq(...args) {
    AbstractOperation.call(this, ...args);
}
operationCreator(Meansq, meansq, NOT_FIXED_ARGS_COUNT, "meansq", meansqDiff);

function RMS(...args) {
    AbstractOperation.call(this, ...args);
}
operationCreator(RMS, rms, NOT_FIXED_ARGS_COUNT, "rms", rmsDiff);

let SumrecCreator = function(argsCount) {
    let result = function(...args) {
        AbstractOperation.call(this, ...args);
    };
    operationCreator(result, sumrec, argsCount, "sumrec" + argsCount, sumrecDiff);
    return result;
}

let Sumrec2 = SumrecCreator(2);
let Sumrec3 = SumrecCreator(3);
let Sumrec4 = SumrecCreator(4);
let Sumrec5 = SumrecCreator(5);

let HMeanCreator = function(argsCount) {
    let result = function(...args) {
        AbstractOperation.call(this, ...args);
    };
    operationCreator(result, hMean, argsCount, "hmean" + argsCount, hMeanDiff);
    return result;
}

let HMean2 = HMeanCreator(2);
let HMean3 = HMeanCreator(3);
let HMean4 = HMeanCreator(4);
let HMean5 = HMeanCreator(5);

function Const(val) {
    this.val = val;
}
Const.prototype.evaluate = function() {
    return this.val;
}
Const.prototype.toString = function() {
    return this.val.toString();
}
Const.prototype.diff = function(diffVar) {
    return new Const(0);
}
Const.prototype.prefix = function() {
    return this.val.toString();
}
Const.prototype.postfix = function() {
    return this.val.toString();
}

let variableOrder = {
    "x" : 0,
    "y" : 1,
    "z" : 2
};

function Variable(varName) {
    this.varName = varName;
}
Variable.prototype.evaluate = function(...variables) {
    return variables[variableOrder[this.varName]];
}
Variable.prototype.toString = function() {
    return this.varName;
}
Variable.prototype.diff = function(diffVar) {
    return diffVar === this.varName ? new Const(1) : new Const(0);
}
Variable.prototype.prefix = function() {
    return this.varName;
}
Variable.prototype.postfix = function() {
    return this.varName;
}

let getOperation = {
    "negate" : Negate,
    "+" : Add,
    "-" : Subtract,
    "*" : Multiply,
    "/" : Divide,
    "sumrec2" : Sumrec2,
    "sumrec3" : Sumrec3,
    "sumrec4" : Sumrec4,
    "sumrec5" : Sumrec5,
    "hmean2" : HMean2,
    "hmean3" : HMean3,
    "hmean4" : HMean4,
    "hmean5" : HMean5,
    "meansq" : Meansq,
    "sqrt" : Sqrt,
    "rms" : RMS
};

function isVariable(v) {
    return v in variableOrder;
}

function ErrorCreator(messageCreator) {
    let result = function(...args) {
        let message = messageCreator(...args);
        Error.call(this);
        this.message = message;
    }
    result.prototype = Object.create(Error.prototype);
    return result;
}

let UnknownTokenError = ErrorCreator((symbol) => ("Error: unknown symbol: " + symbol));
let BracketsMatchingError = ErrorCreator(() => ("Brackets don't match"));
let WrongNumberOfArgsError = ErrorCreator((stringOperation, argumentsCount) => (stringOperation + " has wrong number of arguments: " + argumentsCount));
let EmptyExpressionError = ErrorCreator(() => ("Can't parse empty expression"));

let parse = function(expression) {
    let tokens = expression.trim().split(/\s+/);
    let stack = [];
    for (const token of tokens) {
        if (isVariable(token)) {
            stack.push(new Variable(token));
        } else if (!isNaN(parseInt(token))) {
            stack.push(new Const(parseInt(token)));
        } else {
            let curOperation = getOperation[token];
            stack.push(new curOperation(...stack.splice(-curOperation.prototype.argsCount)));
        }
    }
    return stack[0];
};

function isConst(str) {
    return !isNaN(str);
}

function parsePostfix(expression) {
    return parsePrefix(expression, true);
}

function parsePrefix(expression, isReversed) {
    if (expression.trim() === "") {
        throw new EmptyExpressionError();
    }

    let pos = 0;
    let tokens = expression.replaceAll("(", " ( ").replaceAll(")", " ) ").trim().split(/\s+/);

    if (isReversed) {
        let newTokens = [];
        for (const token of tokens) {
            if (token === "(") {
                newTokens.push(")");
            } else if (token === ")") {
                newTokens.push("(");
            } else {
                newTokens.push(token);
            }
        }
        tokens = newTokens.reverse();
    }

    function realParsePrefix() {
        if (tokens.length === 1) {
            if (isConst(tokens[pos])) {
                return new Const(parseFloat(tokens[pos]));
            } else if (isVariable(tokens[pos])) {
                return new Variable(tokens[pos]);
            } else {
                throw new UnknownTokenError(tokens[pos]);
            }
        }
        let op = getOperation[tokens[pos + 1]];
        let opArgs = [];
        pos += 2;
        for (; pos <= tokens.length; pos += 1) {
            if (pos === tokens.length || pos === -1) {
                throw new BracketsMatchingError();
            }
            let token = tokens[pos];
            if (token === "(") {
                opArgs.push(realParsePrefix(expression));
            } else if (token === ")") {
                break;
            } else if (isVariable(token)) {
                opArgs.push(new Variable(token));
            } else if (isConst(token)) {
                opArgs.push(new Const(parseFloat(token)));
            } else {
                throw new UnknownTokenError(token);
            }
        }
        if (op.prototype.argsCount !== NOT_FIXED_ARGS_COUNT && opArgs.length !== op.prototype.argsCount) {
            throw new WrongNumberOfArgsError(op.prototype.stringRepr, opArgs.length);
        }

        return isReversed ? new op(...opArgs.reverse()) : new op(...opArgs);
    }

    let res = realParsePrefix();
    if (pos !== tokens.length - 1) {
        throw new BracketsMatchingError();
    }
    return res;
}

println(1)

//
//let exprr = new Meansq(new Variable('x'), new Variable('y'), new Variable('z'));
//println(exprr.evaluate(0, 0, 1));
//let exprr = new Add(
//new Subtract(
//    new Multiply(new Variable("x"), new Variable("x")),
//    new Multiply(new Const(2), new Variable("x"))
//),
//new Const(1)
//)
//for (let x = 0; x <= 10; x++) {
//    println(exprr.evaluate(x, 0, 0));
//}