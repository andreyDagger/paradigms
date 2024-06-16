package queue;

/*
Model: a[0]...a[n-1]
Invariant for i=0...n-1: a[i] != null

Let immutable(n): for i=0..n-1: a'[i] == a[i]
Let immutableExceptFor(n, k): for i=0...k-1,k+1...n-1: a'[i] == a[i]

Pred: true
Post: R = n
    size()

Pred: n > 0
Post: R = a[n-1] && n' = n - 1 && for i=0...n-2: a'[i] == a[i]
    deque()

Pred: element != null
Post: n' = n + 1 && for i=1...n: a'[i] == a[i - 1] && a[0] == element
    enqueue(element)

Pred: n > 0:
Post: R == a[n - 1] && n' = n && immutable(n)
    element()

Pred: true
Post: R = (n == 0) && n' = n && immutable(n)
    isEmpty()

Pred: true
Post: n' == 0
    clear()

Pred: element != null
Post: a[n] = element && immutable(n) && n' = n + 1
    push(element)

Pred: n > 0
Post: R = a[0]
    peek()

Pred: n > 0
Post: R = a[0] && n' = n - 1 && for i=0...n'-1: a'[i] == a[i + 1]
    remove()

Pred: 0 <= i < n
Post: R = a[n - i - 1] && n' = n && immutable(n)
    get(i, x)

Pred: 0 <= i < n && element != null
Post: n' = n && immutableExceptFor(n, n - i - 1) && a[n - i - 1] == element
    set(i, element)
 */

import java.util.Objects;

public class ArrayQueueModule {
    private static final int DEFAULT_SIZE = 128;

    private static int size = 0;
    private static Object[] array = new Object[DEFAULT_SIZE];
    private static int leftPointer = 0;

    private static int rightPointer() {
        return (leftPointer + size - 1) % array.length;
    }

    public static Object get(int i) {
        assert i >= 0 && i < size;
        return array[(rightPointer() - i + array.length) % array.length];
    }

    public static void set(int i, Object element) {
        Objects.requireNonNull(element);
        assert i >= 0 && i < size;
        array[(rightPointer() - i + array.length) % array.length] = element;
    }

    public static void push(Object element) {
        Objects.requireNonNull(element);
        if (isEmpty()) {
            leftPointer = 0;
        } else {
            expandIfNeed();
        }
        size++;
        array[rightPointer()] = element;
    }

    public static Object peek() {
        assert size > 0;
        return array[leftPointer];
    }

    public static Object remove() {
        assert size > 0;

        Object result = array[leftPointer];
        leftPointer = (leftPointer + 1) % array.length;
        size--;
        return result;
    }

    public static int size() {
        return size;
    }

    public static void enqueue(Object element) {
        Objects.requireNonNull(element);
        if (isEmpty()) {
            leftPointer = 0;
        } else {
            expandIfNeed();
            leftPointer--;
            if (leftPointer < 0) {
                leftPointer = array.length - 1;
            }
        }
        size++;
        array[leftPointer] = element;
    }

    public static Object dequeue() {
        assert size > 0;

        Object temp = array[rightPointer()];
        size--;
        return temp;
    }

    public static Object element() {
        assert size > 0;

        return array[rightPointer()];
    }

    private static void expandIfNeed() {
        if (size == array.length) {
            int prevRightPointer = rightPointer();
            Object[] temp = array;
            array = new Object[2 * size];
            if (prevRightPointer >= leftPointer) {
                System.arraycopy(temp, leftPointer, array, 0, size);
            } else {
                System.arraycopy(temp, leftPointer, array, 0, size - leftPointer);
                System.arraycopy(temp, 0, array, size - leftPointer, prevRightPointer + 1);
            }
            leftPointer = 0;
        }
    }

    public static boolean isEmpty() {
        return size == 0;
    }

    public static void clear() {
        array = new Object[DEFAULT_SIZE];
        size = 0;
        leftPointer = 0;
    }
}