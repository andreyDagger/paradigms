package queue.tests;

import queue.ArrayQueueADT;
import queue.ArrayQueueModule;

import java.util.*;

public class ArrayQueueModuleTest {

    private static final List<ActionWithArgument<Object>> addFunctions = new ArrayList<>();
    private static final List<ActionWithoutArguments> removeFunctions = new ArrayList<>();
    private static final List<ActionWithoutArguments> otherFunctions = new ArrayList<>();

    private static final Deque<Object> deque = new ArrayDeque<>();

    private static final ActionWithArgument<Object> enqueue = (element) -> {
        ArrayQueueModule.enqueue(element);
        deque.addFirst(element);
    };

    private static final ActionWithArgument<Object> push = (element) -> {
        ArrayQueueModule.push(element);
        deque.addLast(element);
    };

    private static final ActionWithoutArguments dequeue = () -> {
        if (!deque.isEmpty()) {
            assertEquals(ArrayQueueModule.dequeue(), deque.removeLast());
        }
    };

    private static final ActionWithoutArguments remove = () -> {
        if (!deque.isEmpty()) {
            assertEquals(ArrayQueueModule.remove(), deque.removeFirst());
        }
    };

    private static final ActionWithoutArguments element = () -> {
        if (!deque.isEmpty()) {
            assertEquals(ArrayQueueModule.element(), deque.getLast());
        }
    };

    private static final ActionWithoutArguments peek = () -> {
        if (!deque.isEmpty()) {
            assertEquals(ArrayQueueModule.peek(), deque.getFirst());
        }
    };

    private static final ActionWithoutArguments size = () -> {
        assertEquals(ArrayQueueModule.size(), deque.size());
    };

    private static final ActionWithoutArguments isEmpty = () -> {
        assertEquals(ArrayQueueModule.isEmpty(), deque.isEmpty());
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
        assertEquals(ArrayQueueModule.get(i), AdditionalDequeFunctions.get(deque, i));
    }

    private static void doSet() {
        if (deque.isEmpty()) {
            return;
        }
        Random random = new Random();
        int i = random.nextInt(deque.size());
        Object element = random.nextInt();
        ArrayQueueModule.set(i, element);
        AdditionalDequeFunctions.set(deque, i, element);
        assertEquals(ArrayQueueModule.get(i), AdditionalDequeFunctions.get(deque, i));
    }

    private static void doClear() {
        ArrayQueueModule.clear();
        deque.clear();
        assertEquals(ArrayQueueModule.size(), 0);
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