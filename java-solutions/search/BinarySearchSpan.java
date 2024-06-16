package search;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchSpan {

    // Let leftEnter(array) := min_arg(int(args[i]) >= x) forall i=0...array.size()-1 or i = array.size(), if no such index
    // Let rightEnter(array) := min_arg(int(args[i]) > x) forall i=0...array.size()-1 or i = array.size(), if no such index

    // Let Proof := Let: m = floor((l + r) / 2) && r - l > 1 ==>
    // m - l = floor((l + r) / 2) - l < (l + r) / 2 - l < (r + r) / 2 - l = r - l
    // ==> m - l < r - l ==> segment becomes smaller
    // m > l (because r - l > 1) ==> -m < -l + 1 ==> r - m < r - l + 1
    // ==> segment becomes smaller

    // Let Proof_2nd := Let: m = floor((l + r) / 2) && r - l >= 1 ==>
    // m - l = floor((l + r) / 2) - l < (l + r) / 2 - l < (r + r) / 2 - l = r - l
    // ==> m - l < r - l ==> segment becomes smaller
    // m + 1 > l (because r - l >= 1) ==> -m - 1 < -l ==> r - (m + 1) < r - l
    // ==> segment becomes smaller

    // Contract
    // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
    // Post: R = leftEnter(array)
    public static int leftEnterIterative(int x, List<Integer> array) {
        // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        int l = -1;
        // Post: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && l == -1
        // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && l == -1
        int r = array.size() - 1;
        // Post: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && l == -1
        // && r == array.size() - 1

        // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && l == -1
        // && r == array.size() - 1
        if (array.get(r) < x) {
            // Post: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && l == -1
            // && r == array.size() - 1 && array[r] < x
            return array.size();
            // Post: R = array.size()
        }
        // Post: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && l == -1
        // && r == array.size() - 1 && array[r] >= x

        // Let: array[-1] = -inf
        // I: array[l] < x && array[r] >= x
        while (r - l > 1) {
            // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
            // && r - l > 1
            int m = (l + r) / 2;
            // Post: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
            // && r - l > 1 && m = floor((l + r) / 2)

            // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
            // && r - l > 1 && m = floor((l + r) / 2)
            if (array.get(m) < x) {
                // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
                // && r - l > 1 && m = floor((l + r) / 2) && array[m] < x
                l = m;
                // Post: array[m] < x && array[l] < x && array[r] >= x && r - l > 1 &&
                // && m = floor((l + r) / 2) && l = m
            } else {
                // Pred: array[m] >= x && array[l] < x && array[r] >= x && r - l > 1 &&
                // && m = floor((l + r) / 2) && array[r] >= x
                r = m;
                // Post: array[m] >= x && array[l] < x && array[r] >= x && r - l > 1 &&
                // && m = floor((l + r) / 2) && r = m
            }
            // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
            // && I && r - l -> became smaller ("Proof")
        }
        // Post: array[l] < x && array[r] >= x && r - l <= 1


        // Pred: array[l] < x && array[r] >= x && r - l <= 1
        return r;
        // Post: R == r
    }

    // Contract
    // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
    // Post: R == rightEnter(array)
    public static int rightEnterIterative(int x, List<Integer> array) {
        // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        int l = -1;
        // Post: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && l == -1
        // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && l == -1
        int r = array.size() - 1;
        // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && l == -1
        // && r == array.size() - 1

        // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && l == -1
        // && r == array.size() - 1
        if (array.get(r) <= x) {
            // Pred: array - непустой и отсортированный по неубыванию && l == -1
            // && r == array.size() - 1 && array[r] < x
            return array.size();
            // Post: R == array.size()
        }
        // Let: array[-1] := -inf
        // I: array[l] <= x && array[r] > x
        while (r - l > 1) {
            // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] &&
            // array[l] <= x && array[r] > x && r - l > 1
            int m = (l + r) / 2;
            // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] &&
            // array[l] <= x && array[r] > x && r - l > 1 && m == floor((l + r) / 2)

            // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] &&
            // array[l] <= x && array[r] > x && r - l > 1 && m == floor((l + r) / 2)
            if (array.get(m) <= x) {
                // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] &&
                // array[l] <= x && array[r] > x && r - l > 1 && m == floor((l + r) / 2) && array[m] <= x
                l = m;
                // Post: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] &&
                // l == m && array[l] <= x && array[r] > x
            } else {
                // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] &&
                // array[l] <= x && array[r] > x && r - l > 1 && m == floor((l + r) / 2) && array[m] > x
                r = m;
                // Post: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] &&
                // r == m && array[l] <= x && array[r] > x
            }
            // Post: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] &&
            // array[l] <= x && array[r] > x && r - l -> became smaller ("Proof"0
        }
        // Post: array[l] <= x && array[r] > x && r - l <= 1


        // Pred: array[l] <= x && array[r] > x && r - l <= 1
        return r;
        // Post: R == r
    }

    // Contract
    // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && 0 <= l <= r < array.size()
    // Post: R == leftEnter(array[l:r])
    static int leftEnterRecursive(int x, int l, int r, List<Integer> array) {
        // Pred: array.size() > 0
        // && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        // && 0 <= l <= r < array.size()
        if (l == r) {
            // Pred: array.size() > 0
            // && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
            // && 0 <= l == r < array.size()
            if (array.get(l) >= x) {
                // Pred: array.size() > 0
                // && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
                // && 0 <= l == r < array.size() && array[l] >= x
                return l;
                // Post: R == l
            } else {
                // Pred: array.size() > 0
                // && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
                // && 0 <= l == r < array.size() && array[l] < x
                return array.size();
                // Post: R == array.size()
            }
            // Post: R == leftEnter(array[l:r])
        }
        // Post: array.size() > 0
        // && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        // && 0 <= l < r < array.size()

        // Pred: array.size() > 0
        // && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        // && 0 <= l < r < array.size()
        int m = (l + r) / 2;
        // Post: array.size() > 0
        // && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        // && 0 <= l < r < array.size() && m == floor((l + r) / 2)

        // Pred: array.size() > 0
        // && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        // && 0 <= l < r < array.size() && m == floor((l + r) / 2)
        if (array.get(m) < x) {
            // Pred: array.size() > 0
            // && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
            // && 0 <= l < r < array.size() && m = floor((l + r) / 2) && array[m] < x
            // ==>
            // leftEnter in range [m + 1, r]
            return leftEnterRecursive(x, m + 1, r, array);
            // Pred: R == leftEnter(array[l:r]) == leftEnter(array[m + 1:r])
        } else {
            // Pred: array.size() > 0
            // && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
            // && 0 <= l < r < array.size() && m == floor((l + r) / 2) && array[m] >= x
            // ==>
            // leftEnter in l..m
            return leftEnterRecursive(x, l, m, array);
            // Pred: R == leftEnter(array[l:r]) == leftEnter(array[l:m])
        }
        // Post: R == leftEnter(array[l:r])
    }

    // Contract
    // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1] && 0 <= l <= r < array.size()
    // Post: R == rightEnter(array[l:r])
    static int rightEnterRecursive(int x, int l, int r, List<Integer> array) {
        // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        // && 0 <= l <= r < array.size()
        if (r == l) {
            // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
            // && 0 <= l == r < array.size()
            if (array.get(l) > x) {
                // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
                // && 0 <= l == r < array.size() && array[l] > x
                return l;
                // Post: R == l
            } else {
                // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
                // && 0 <= l == r < array.size() && array[l] <= x
                return array.size();
                // Post: R == array.size()
            }
            // Post: R == rightEnter(array[l:r])
        }
        // Post: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        // && 0 <= l < r < array.size()

        // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        // && 0 <= l < r < array.size()
        int m = (l + r) / 2;
        // Post: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        // && 0 <= l < r < array.size() && m = floor((l + r) / 2)

        // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
        // && 0 <= l < r < array.size() && m = floor((l + r) / 2)
        if (array.get(m) <= x) {
            // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
            // && 0 <= l < r < array.size() && m == floor((l + r) / 2) && array[m] <= x
            // ==>
            // rightEnter lies in range [m + 1, r]
            return rightEnterRecursive(x, m + 1, r, array);
            // Post: R == rightEnter(array[l:r]) == rightEnter(array[m + 1:r])
        } else {
            // Pred: array.size() > 0 && forall i=0...array.size()-2 -> array[i] <= array[i + 1]
            // && 0 <= l < r < array.size() && m == floor((l + r) / 2) && array[m] > x
            // ==>
            // rightEnter lies in range [l, m]
            return rightEnterRecursive(x, l, m, array);
            // Post: R == rightEnter(l, r) == rightEnter(l, m)
        }
        // Post:R == rightEnter(array[l:r])

        // recursion is not infinity because of "Proof_2nd"
    }

    // Contract
    // Let: n := args.size()
    // Let: answer(array) := {leftEnter(array), count(array, x)}
    // Pred: forall i=0...n-1 -> isInteger(args[i]) && forall i=1...n-2 -> int(args[i]) <= int(args[i + 1])
    // Post: stdout contains answer([int(args[i]) forall i=1...n-1])
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
        if (array.isEmpty()) {
            // Pred: array.size() == 0
            System.out.println("0 0");
            // Post: stdout contains answer(array)
        } else {
            // Pred: I && array.size() > 0
            boolean wantIterative = true;
            // Post: I && array.size() > 0 && wantIterative == true
            // Pred: I && array.size() > 0 && wantIterative == true
            int left;
            if (wantIterative) {
                left = leftEnterIterative(x, array);
            } else {
                left = leftEnterRecursive(x, 0, array.size() - 1, array);
            }
            // Post: I && array.size() > 0 && wantIterative == true
            // && left == leftEnter(array)

            // Pred: I && array.size() > 0 && wantIterative == true
            // && left == leftEnter(array)
            int right;
            // Post: I && array.size() > 0 && wantIterative == true
            // && left == leftEnter(array) && "right" exists

            // Pred: I && array.size() > 0 && wantIterative == true
            // && left == leftEnter(array) && "right" exists
            if (wantIterative) {
                // Pred: I && array.size() > 0 && wantIterative == true
                // && left == leftEnter(array) && "right" exists
                right = rightEnterIterative(x, array);
                // Post: I && array.size() > 0 && wantIterative == true
                // && left == leftEnter(array) && right == rightEnter(array)
            } else {
                // Pred: I && array.size() > 0 && wantIterative == false
                // && left = leftEnter(array) && "right" exists
                right = rightEnterRecursive(x, 0, array.size() - 1, array);
                // Pred: I && array.size() > 0 && wantIterative == false
                // && left == leftEnter(array) && right == rightEnter(array)
            }
            // Post: I && array.size() > 0 && wantIterative == true
            // && left == leftEnter(array) && right == rightEnter(array)

            // Pred: I && array.size() > 0 && wantIterative == true
            // && left == leftEnter(array) && right == rightEnter(array)
            if (left == array.size()) {
                // Pred: I && array.size() > 0 && wantIterative == true
                // && left == array.size() && right == rightEnter(array)
                // ==> answer == {array.size(), 0}
                System.out.println(array.size() + " 0");
                // Post: stdout contains answer(array)
            } else {
                // Pred: I && array.size() > 0 && wantIterative == true
                // && left == leftEnter(array) && right == rightEnter(array) && array[left] >= x
                // ==> answer(array) = {left, right - left}

                // Proof: forall i < left -> array[i] < x
                // forall i >= right -> array[i] > x
                // forall left <= i < right -> array[i] >= x && array[i] <= x ==> array[i] == x

                System.out.println(left + " " + (right - left));
                // Post: stdout contains answer(array)
            }
            // Post: stdout contains answer
        }
        // Post: stdout contains answer
    }
}
