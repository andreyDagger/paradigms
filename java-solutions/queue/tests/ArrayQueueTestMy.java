package queue.tests;

import queue.ArrayQueue;

import java.util.*;

public class ArrayQueueTestMy {

    private static final List<ActionWithArgument<Object>> addFunctions = new ArrayList<>();
    private static final List<ActionWithoutArguments> removeFunctions = new ArrayList<>();
    private static final List<ActionWithoutArguments> otherFunctions = new ArrayList<>();

    private static final ArrayQueue queue = new ArrayQueue();
    private static final Deque<Object> deque = new ArrayDeque<>();

    private static final ActionWithArgument<Object> enqueue = (element) -> {
        queue.enqueue(element);
        deque.addFirst(element);
    };

    private static final ActionWithArgument<Object> push = (element) -> {
        queue.push(element);
        deque.addLast(element);
    };

    private static final ActionWithoutArguments dequeue = () -> {
        if (!deque.isEmpty()) {
            assertEquals(queue.dequeue(), deque.removeLast());
        }
    };

    private static final ActionWithoutArguments remove = () -> {
        if (!deque.isEmpty()) {
            assertEquals(queue.remove(), deque.removeFirst());
        }
    };

    private static final ActionWithoutArguments element = () -> {
        if (!deque.isEmpty()) {
            assertEquals(queue.element(), deque.getLast());
        }
    };

    private static final ActionWithoutArguments peek = () -> {
        if (!deque.isEmpty()) {
            assertEquals(queue.peek(), deque.getFirst());
        }
    };

    private static final ActionWithoutArguments size = () -> {
        assertEquals(queue.size(), deque.size());
    };

    private static final ActionWithoutArguments isEmpty = () -> {
        assertEquals(queue.isEmpty(), deque.isEmpty());
    };

    private static final ActionWithoutArguments clear = () -> {
        queue.clear();
        deque.clear();
    };

    private static void fillFunctionArrays() {
        addFunctions.add(enqueue);
        addFunctions.add(push);

        removeFunctions.add(dequeue);
        removeFunctions.add(remove);

        otherFunctions.add(element);
        otherFunctions.add(peek);
        otherFunctions.add(size);
        otherFunctions.add(isEmpty);
    }

    private static void assertEquals(Object a, Object b) {
        if (!a.equals(b)) {
            throw new AssertionError(a + " is not equals to " + b);
        }
    }

    private static void doActionOneArgument(List<ActionWithArgument<Object>> actions) {
        Random random = new Random();
        int actionId = random.nextInt(actions.size());
        int elementToAdd = random.nextInt();
        actions.get(actionId).doAction(elementToAdd);
    }

    private static void doActionWithoutArguments(List<ActionWithoutArguments> actions) {
        Random random = new Random();
        int actionId = random.nextInt(actions.size());
        actions.get(actionId).doAction();
    }

    private static void doGet() {
        if (deque.isEmpty()) {
            return;
        }
        Random random = new Random();
        int i = random.nextInt(deque.size());
        assertEquals(queue.get(i), AdditionalDequeFunctions.get(deque, i));
    }

    private static void doSet() {
        if (deque.isEmpty()) {
            return;
        }
        Random random = new Random();
        int i = random.nextInt(deque.size());
        Object element = random.nextInt();
        queue.set(i, element);
        AdditionalDequeFunctions.set(deque, i, element);
        assertEquals(queue.get(i), AdditionalDequeFunctions.get(deque, i));
    }

    private static void doClear() {
        queue.clear();
        deque.clear();
        assertEquals(queue.size(), 0);
    }

    private static void doRandomAction(double[] distribution) {
        double randomValue = Math.random();
        if (randomValue <= distribution[0]) {
            doActionOneArgument(addFunctions);
        } else if (randomValue <= distribution[1]) {
            doActionWithoutArguments(removeFunctions);
        } else if (randomValue <= distribution[2]) {
            doActionWithoutArguments(otherFunctions);
        } else if (randomValue <= distribution[3]) {
            doSet();
        } else if (randomValue <= distribution[4]) {
            doGet();
        } else {
            doClear();
        }
    }

    public static void test() {
        final int TIMES = 10000;
        fillFunctionArrays();
        for (int i = 0; i < TIMES; i++) {
            doRandomAction(new double[]{0.5, 0.6, 0.7, 0.8, 0.9});
        }
    }
}