package expression;

public class MyMath {
    public static int sign(int a) {
        return Integer.compare(a, 0);
    }

    public static int square(int a) {
        return a * a;
    }

    public static int absExact(int a) {
        if (a >= 0) {
            return a;
        } else {
            return negateExact(a);
        }
    }

    public static int binpow(int a, int p, int mod) {
        if (p == 0) {
            return 1;
        } else if (p % 2 == 1) {
            return a * binpow(a, p - 1, mod) % mod;
        } else {
            int temp = binpow(a, p >> 1, mod);
            return temp * temp % mod;
        }
    }

    public static int calculateGcd(int a, int b) {
        long longA = Math.abs((long)a);
        long longB = Math.abs((long)b);
        while (longA > 0 && longB > 0) {
            if (longA >= longB) {
                longA %= longB;
            } else {
                longB %= longA;
            }
        }
        return (int)Math.max(longA, longB);
    }

    public static int calculateLcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
            //throw new ArithmeticException("LCM is not supported when exactly one zero value");
        }
        return multiplyExact(divideExact(a, calculateGcd(a, b)), b);
    }

    public static int multiplyExact(int a, int b) {
        /*
        a == 0 || b == 0 => 0
        res = a * b
        a != res / b => exception
        a == MIN_VALUE, b == -1 => exception
         */
        if (a == 0 || b == 0) {
            return 0;
        } else if (a == Integer.MIN_VALUE && b == -1) {
            throw new ArithmeticException("overflow");
        }
        int res = a * b;
        if (a != res / b) {
            throw new ArithmeticException("overflow");
        }
        return res;
    }

    public static int addExact(int a, int b) {
        int r = a + b;
        if (((a ^ r) & (b ^ r)) < 0) {
            throw new ArithmeticException("overflow");
        }
        return r;
    }

    public static int divideExact(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("division by zero");
        }
        if (a == Integer.MIN_VALUE && b == -1) {
            throw new ArithmeticException("overflow");
        }
        return a / b;
    }

    public static int subtractExact(int a, int b) {
        int r = a - b;
        if (((a ^ b) & (a ^ r)) < 0) {
            throw new ArithmeticException("overflow");
        }
        return r;
    }

    public static int negateExact(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
        return -a;
    }

    public static int pow10(int x) {
        if (x < 0) {
            throw new ArithmeticException("Argument of pow10 must be not less than zero");
        }
        int res = 1;
        while (x > 0) {
            res = multiplyExact(res, 10);
            x--;
        }
        return res;
    }

    public static int pow10(double x) {
        throw new AssertionError("NO TESTS WITH DOUBLES");
    }

    public static int logi10(int x) {
        if (x <= 0) {
            throw new ArithmeticException("Argument of logarithm must be positive");
        }
        return (int)Math.log10(x);
    }

    public static double log10(double x) {
        throw new AssertionError("NO TESTS WITH DOUBLE");
    }
}
