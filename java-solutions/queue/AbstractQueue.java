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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;
    public static int cnt = 0;

    protected abstract Queue getInstance();

    private void enqueueAll(List<Object> elementList) {
        for (Object element : elementList) {
            enqueue(element);
        }
    }

    public Queue getNth(int n) {
        Queue result = getInstance();
        ArrayList<Object> tempList = new ArrayList<>();
        for (int i = 1; !isEmpty(); i++) {
            Object element = dequeue();
            if (i % n == 0) {
                result.enqueue(element);
            }
            tempList.add(element);
        }
        enqueueAll(tempList);
        return result;
    }

    public void dropNth(int n) {
        int size = size();
        for (int i = 1; i <= size; i++) {
            Object element = dequeue();
            if (i % n != 0) {
                enqueue(element);
            }
        }
    }

    public Queue removeNth(int n) {
        Queue result = getNth(n);
        dropNth(n);
        return result;
    }

    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        enqueueImpl(element);
        size++;
    }

    public Object dequeue() {
        assert size > 0;
        size--;
        return dequeueImpl();
    }

    protected abstract void enqueueImpl(Object element);
    protected abstract Object dequeueImpl();

    public int size() {
        //dump("size:");
        return size;
    }

    public boolean isEmpty() {
        //dump("isEmpty:");
        return size == 0;
    }
}
