package search;

import java.util.ArrayList;
import java.util.List;

public class BinarySearch {

    // Let: Proof := Let: m = floor((l + r) / 2) ==>
    // m - l = floor((l + r) / 2) - l < (l + r) / 2 - l < (r + r) / 2 - l = r - l
    // ==> m - l < r - l ==> segment becomes smaller
    // m > (l + r) / 2 - 1 > (l + l) / 2 - 1 = l - 1
    // ==> m > l - 1 ==> -m < -l + 1 ==> r - m < r - l + 1
    // ==> segment becomes smaller

    // Let: Proof_2nd := Let: m = floor((l + r) / 2) && r - l >= 1 ==>
    // m - l = floor((l + r) / 2) - l < (l + r) / 2 - l < (r + r) / 2 - l = r - l
    // ==> m - l < r - l ==> segment becomes smaller
    // m + 1 > l (because r - l >= 1) ==> -m - 1 < -l ==> r - (m + 1) < r - l
    // ==> segment becomes smaller

    // Contract
    // Let: n := array.size()
    // Let: answer := min_arg(int(args[i]) <= x) forall i=0...n-1 or i = n, if no such index
    // Pred: array.size() > 0 && forall i=0...n-1 -> array[i] > array[i + 1]
    // Post: R = answer
    public static int iterativeBinarySearch(int x, List<Integer> array) {
        // Pred: array.size() > 0 && forall i=0...n-1 -> array[i] > array[i + 1]
        int l = -1;
        // Post: array.size() > 0 && forall i=0...n-1 -> array[i] > array[i + 1] && l == -1
        // Pred: array.size() > 0 && forall i=0...n-1 -> array[i] > array[i + 1] && l == -1
        int r = array.size() - 1;
        // Post: array.size() > 0 && forall i=0...n-1 -> array[i] > array[i + 1] && l == -1
        // && r == array.size() - 1
        if (array.get(r) > x) {
            // Pred: array.size() > 0 && forall i=0...n-1 -> array[i] > array[i + 1]
            // && array[n - 1] > x
            return array.size();
            // Post: R = array.size()
        }
        // I: array[l] > x && array[r] <= x
        while (r - l > 1) {
            // Pred: array.size() > 0 && forall i=0...n-1 -> array[i] > array[i + 1] &&
            // array[l] > x && array[r] <= x && r - l > 1
            int m = (l + r) / 2;
            // Post: array.size() > 0 && forall i=0...n-1 -> array[i] > array[i + 1] &&
            // array[l] > x && array[r] <= x && r - l > 1 &&
            // && m = floor((l + r) / 2)

            // Pred: array.size() > 0 && forall i=0...n-1 -> array[i] > array[i + 1] &&
            // array[l] > x && array[r] <= x && r - l > 1 &&
            // && m = floor((l + r) / 2)
            if (array.get(m) > x) {
                // Pred: array.size() > 0 && forall i=0...n-1 -> array[i] > array[i + 1] &&
                // array[m] > x && array[l] > x && array[r] <= x && r - l > 1 &&
                // && m = floor((l + r) / 2)
                l = m;
                // Post: array[m] > x && array[l] > x && array[r] <= x && r - l > 1 &&
                // && m = floor((l + r) / 2) && l = m
            } else {
                // Post: array.size() > 0 && forall i=0...n-1 -> array[i] > array[i + 1] &&
                // array[m] <= x && array[l] > x && array[r] <= x && r - l > 1 &&
                // && m = floor((l + r) / 2)
                r = m;
                // Post: array[m] <= x && array[l] > x && array[r] <= x && r - l > 1 &&
                // && m = floor((l + r) / 2) && r = m
            }
            // Post: r' - l' < r - l, because of "Proof"
        }
        // Post: array[l] > x && array[r] <= x && r - l == 1

        // "while" is not infinity because of "Proof"
        return r;
    }

    // Contract
    // Let: n := array.size()
    // Let: answer(l, r) := min_arg(int(args[i]) <= x) forall i=l...r or i = n, if no such index
    // Pred: array.size() > 0 && forall i=l...r-1 -> array[i] > array[i + 1] && 0 <= l <= r < n
    // Post: R = answer(l, r)
    public static int recursiveBinarySearch(int x, List<Integer> array, int l, int r) {
        if (l == r) {
            // Pred: array.size() > 0 && forall i=l...r-1 -> array[i] > array[i + 1] && 0 <= l == r < n
            if (array.get(l) <= x) {
                // Pred: array.size() > 0 && forall i=l...r-1 -> array[i] > array[i + 1]
                // && 0 <= l == r < n && array[l] <= x
                return l;
                // Post: R = l
            } else {
                // Pred: array.size() > 0 && forall i=l...r-1 -> array[i] > array[i + 1]
                // && 0 <= l == r < n && array[l] > x
                return array.size();
                // R = array.size()
            }
            // Post: R = answer(l, r)
        }
        // Pred: array.size() > 0 && forall i=l...r-1 -> array[i] > array[i + 1] && 0 <= l < r < n
        int m = (l + r) / 2;
        // Post: array.size() > 0 && forall i=l...r-1 -> array[i] > array[i + 1]
        // && 0 <= l < r < n && m = floor((l + r) / 2)

        // Pred: array.size() > 0 && forall i=l...r-1 -> array[i] > array[i + 1]
        // && 0 <= l < r < n && m = floor((l + r) / 2)
        if (array.get(m) > x) {
            // Pred: array.size() > 0 && forall i=l...r-1 -> array[i] > array[i + 1]
            // && 0 <= l < r < n && m = floor((l + r) / 2) && array[m] > x
            return recursiveBinarySearch(x, array, m + 1, r);
            // Post: R = answer(l, r) = answer(m + 1, r)
        } else {
            // Pred: array.size() > 0 && forall i=l...r-1 -> array[i] > array[i + 1]
            // && 0 <= l < r < n && m = floor((l + r) / 2) && array[m] <= x
            return recursiveBinarySearch(x, array, l, m);
            // Post: R = answer(l, r) = answer(l, m)
        }
        // Post: R = answer(l, r)

        // "recursion" is not infinity because of "Proof"
    }

    // Contract
    // Let: n := args.size()
    // Let: answer := min_arg(int(args[i]) <= x) forall i=1...n-1 or i = n, if no such index
    // Pred: forall i=0...n-1 -> isInteger(args[i]) && forall i=1...n-2 -> int(args[i]) > int(args[i + 1])
    // Post: stdout contains answer
    public static void main(String[] args) {
        // Pred: args.size() > 0
        int x = Integer.parseInt(args[0]);
        // Post: x = int(args[0])
        // Pred: true
        ArrayList<Integer> array = new ArrayList<>();
        // Post: array exists
        // I: forall i=0...array.size()-1 -> array[i] = int(args[i + 1])
        for (int i = 1; i < args.length; i++) {
            // Pred: array exists && I
            array.add(Integer.parseInt(args[i]));
            // Post: array[i - 1] == int(args[i]) -> I
        }
        // Post: I
        // Pred: I
        if (array.size() == 0) {
            // Pred: array.size() == 0
            System.out.println("0");
            // Post: stdout contains answer
        } else {
            // Pred: array.size() > 0
            int res = recursiveBinarySearch(x, array, 0, array.size() - 1);
            // Post: res is answer
            // Pred: array.size() > 0
            res = iterativeBinarySearch(x, array);
            // Post: res is answer
            // Pred: res is answer
            System.out.println(res);
            // Post: stdout contains answer
        }
        // Post: stdout contains answer
    }
}
