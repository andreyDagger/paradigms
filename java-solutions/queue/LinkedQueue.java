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

public class LinkedQueue extends AbstractQueue {
    private Node leftPointer;
    private Node rightPointer;

    protected Queue getInstance() {
        return new LinkedQueue();
    }

    private static class Node {
        private final Object value;
        private Node left;

        public Node(Object value, Node left) {
            this.value = value;
            this.left = left;
        }
    }

    private void dump(String pref) { // для дебага
        System.out.println(pref);
        LinkedQueue.Node curNode = rightPointer;
        List<Object> list = new ArrayList<>();
        while (curNode != null) {
            list.add(curNode.value);
            curNode = curNode.left;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();
    }

    public Queue getNth(int n) {
        Node curNode = rightPointer;
        LinkedQueue result = new LinkedQueue();
        for (int i = 1; i <= size; i++) {
            if (i % n == 0) {
                result.enqueue(curNode.value);
            }
            curNode = curNode.left;
        }
        return result;
    }

    public void dropNth(int n) {
        if (n == 1) {
            clear();
            return;
        }
        int prevSize = size;
        Node curNode = rightPointer;
        for (int i = 2; i <= prevSize; i++) {
            if (i % n == 0) {
                if (i == prevSize) {
                    leftPointer = curNode;
                } else {
                    curNode.left = curNode.left.left;
                }
                size--;
            } else {
                curNode = curNode.left;
            }
        }
    }

    protected void enqueueImpl(Object element) {
        Node prevLeft = leftPointer;
        leftPointer = new Node(element, null);
        if (isEmpty()) {
            rightPointer = leftPointer;
        } else {
            prevLeft.left = leftPointer;
        }
    }

    protected Object dequeueImpl() {
        Object result = rightPointer.value;
        rightPointer = rightPointer.left;
        if (isEmpty()) {
            leftPointer = null;
        }
        return result;
    }

    public Object element() {
        return rightPointer.value;
    }

    public void clear() {
        leftPointer = null;
        rightPointer = null;
        size = 0;
    }
}
