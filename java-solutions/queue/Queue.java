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

public interface Queue {
    void enqueue(Object element);
    Object dequeue();
    Object element();
    Queue getNth(int n);
    Queue removeNth(int n);
    void dropNth(int n);
    boolean isEmpty();
    int size();
    void clear();
}
