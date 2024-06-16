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

public class ArrayQueueADT {
    private static final int DEFAULT_SIZE = 128;

    private int size = 0;
    private Object[] array = new Object[DEFAULT_SIZE];
    private int leftPointer = 0;

    private static int rightPointer(ArrayQueueADT queue) {
        return (queue.leftPointer + queue.size - 1) % queue.array.length;
    }

    public static Object get(ArrayQueueADT queue, int i) {
        assert i >= 0 && i < queue.size;
        return queue.array[(rightPointer(queue) - i + queue.array.length) % queue.array.length];
    }

    public static void set(ArrayQueueADT queue, int i, Object element) {
        Objects.requireNonNull(element);
        assert i >= 0 && i < queue.size;
        queue.array[(rightPointer(queue) - i + queue.array.length) % queue.array.length] = element;
    }

    public static void push(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        if (isEmpty(queue)) {
            queue.leftPointer = 0;
        } else {
            expandIfNeed(queue);
        }
        queue.size++;
        queue.array[rightPointer(queue)] = element;
    }

    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.array[queue.leftPointer];
    }

    public static Object remove(ArrayQueueADT queue) {
        assert queue.size > 0;

        Object result = queue.array[queue.leftPointer];
        queue.leftPointer = (queue.leftPointer + 1) % queue.array.length;
        queue.size--;
        return result;
    }

    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        if (isEmpty(queue)) {
            queue.leftPointer = 0;
        } else {
            expandIfNeed(queue);
            queue.leftPointer--;
            if (queue.leftPointer < 0) {
                queue.leftPointer = queue.array.length - 1;
            }
        }
        queue.size++;
        queue.array[queue.leftPointer] = element;
    }

    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;

        Object temp = queue.array[rightPointer(queue)];
        queue.size--;
        return temp;
    }

    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.array[rightPointer(queue)];
    }

    //:note: too much massive
    private static void expandIfNeed(ArrayQueueADT queue) {
        if (queue.size == queue.array.length) {
            int prevRightPointer = rightPointer(queue);
            Object[] temp = queue.array;
            queue.array = new Object[2 * queue.size];
            if (prevRightPointer >= queue.leftPointer) {
                System.arraycopy(temp, queue.leftPointer, queue.array, 0, queue.size);
            } else {
                System.arraycopy(temp, queue.leftPointer, queue.array, 0, queue.size - queue.leftPointer);
                System.arraycopy(temp, 0, queue.array, queue.size - queue.leftPointer, prevRightPointer + 1);
            }
            queue.leftPointer = 0;
        }
    }

    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    public static void clear(ArrayQueueADT queue) {
        queue.array = new Object[DEFAULT_SIZE];
        queue.size = 0;
        queue.leftPointer = 0;
    }
}