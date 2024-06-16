let cnst = (value) => () => value;

let variableOrder = {
    "x" : 0,
    "y" : 1,
    "z" : 2
};

let variable = (name) => (...variables) => variables[variableOrder[name]];

// :NOTE: -> function
let operation = function(op, argsCount) {
    let result = (...exprs) => (...args) => op(...exprs.map(expr => expr(...args)));
    result.argsCount = argsCount || op.length;
    return result;
};

// :NOTE: .length

let add = operation((a, b) => (a + b), 2);

let subtract = operation((a, b) => (a - b), 2);

let multiply = operation((a, b) => a * b, 2);

let divide = operation((a, b) => (a / b), 2);

let negate = operation((a) => (-a), 1);

// :NOTE: copy-paste
let argMin = (argsCount) => operation((...args) => args.indexOf(Math.min(...args)), argsCount);
let argMin5 = argMin(5);
let argMin3 = argMin(3);

let argMax = (argsCount) => operation((...args) => args.indexOf(Math.max(...args)), argsCount);
let argMax5 = argMax(5);
let argMax3 = argMax(3);

let one = cnst(1);
let two = cnst(2);

const CONSTANTS = {
    "one" : one,
    "two" : two
};

let getOperation = {
    "negate" : negate,
    "+" : add,
    "-" : subtract,
    "*" : multiply,
    "/" : divide,
    "argMin3" : argMin3,
    "argMin5" : argMin5,
    "argMax3" : argMax3,
    "argMax5" : argMax5
};

function isVariable(v) {
    return v in variableOrder;
}

function parse(expression) {
    const stack = [];
    for (const token of expression.trim().split(/\s+/)) {
        if (isVariable(token)) {
            stack.push(variable(token));
        } else {
            let parsedToken = parseFloat(token);
            // :NOTE: Infinity
            if (!isNaN(parsedToken)) {
                stack.push(cnst(parsedToken));
            } else if (token in CONSTANTS) {
                stack.push(CONSTANTS[token]);
            } else {
                let curOperation = getOperation[token];
                stack.push(curOperation(...stack.splice(-curOperation.argsCount)));
                // stack.splice(stack.length - curOperation.argsCount,
                //  curOperation.argsCount,
                //   curOperation(...stack.slice(stack.length - curOperation.argsCount)));
            }
        }
    }
    return stack[0];
};

let exprr = add(subtract(multiply(variable('x'), variable('x')), multiply(variable('x'), cnst(2))), cnst(1));
for (let x = 0; x <= 10; x++) {
    println(exprr(x, 0, 0));
}
