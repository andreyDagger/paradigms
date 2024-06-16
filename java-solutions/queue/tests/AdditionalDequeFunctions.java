package queue.tests;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class AdditionalDequeFunctions {
    public static Object get(Deque<Object> deque, int i) {
        List<Object> temp = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            temp.add(deque.removeLast());
        }
        Object result = deque.getLast();
        for (int j = 0; j < i; j++) {
            deque.addLast(temp.get(temp.size() - j - 1));
        }
        return result;
    }

    public static void set(Deque<Object> deque, int i, Object element) {
        List<Object> temp = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            temp.add(deque.removeLast());
        }
        deque.removeLast();
        deque.addLast(element);
        for (int j = 0; j < i; j++) {
            deque.addLast(temp.get(temp.size() - j - 1));
        }
    }
}
