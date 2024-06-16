package queue;

/*
Model: a[0]...a[n-1]
Invariant for i=0...n-1: a[i] != null

Let immutable(n): for i=0..n-1: a'[i] == a[i]

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
Post: R = a[n - 1] && n' = n && immutable(n)
    element()

Pred: true
Post: R = (n == 0) && n' = n && immutable(n)
    isEmpty()

Pred: true
Post: n' = 0
    clear()

Pred: N >= 1
Post: n' = n && immutable(n) && R = {a[i] : forall i=0...n-1 && (i + 1) % N == 0}
    getNth()

Pred: N >= 1
Post: n' = floor(n / N) forall i=0 && floor((i + 1) / N) < n: a'[i] = a[i + floor((i + 1) / N)]
    dropNth()

Pred: N >= 1
Post: R = getNth() && dropNth()
    removeNth()
 */

import java.util.Objects;

public class ArrayQueue extends AbstractQueue {
    private static final int DEFAULT_SIZE = 128;

    private Object[] array = new Object[DEFAULT_SIZE];
    private int leftPointer = 0;

    protected Queue getInstance() {
        return new ArrayQueue();
    }

    private int rightPointer() {
        return (leftPointer + size - 1) % array.length;
    }

    public Object get(int i) {
        assert i >= 0 && i < size;
        return array[(rightPointer() - i + array.length) % array.length];
    }

    public void set(int i, Object element) {
        Objects.requireNonNull(element);
        assert i >= 0 && i < size;
        array[(rightPointer() - i + array.length) % array.length] = element;
    }

    public void push(Object element) {
        Objects.requireNonNull(element);
        if (isEmpty()) {
            leftPointer = 0;
        } else {
            expandIfNeed();
        }
        size++;
        array[rightPointer()] = element;
    }

    public Object peek() {
        assert size > 0;
        return array[leftPointer];
    }

    public Object remove() {
        assert size > 0;

        Object result = array[leftPointer];
        leftPointer = (leftPointer + 1) % array.length;
        size--;
        return result;
    }

    protected void enqueueImpl(Object element) {
        if (isEmpty()) {
            leftPointer = 0;
        } else {
            expandIfNeed();
            leftPointer--;
            if (leftPointer < 0) {
                leftPointer = array.length - 1;
            }
        }
        array[leftPointer] = element;
    }

    protected Object dequeueImpl() {
        return array[(rightPointer() + 1) % array.length];
    }

    public Object element() {
        assert size > 0;

        return array[rightPointer()];
    }

    private void expandIfNeed() {
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

    public void clear() {
        array = new Object[DEFAULT_SIZE];
        size = 0;
        leftPointer = 0;
    }
}