public final class DeterministicSelector {
    private DeterministicSelector() {}

    // Returns the k-th smallest value (0-based).
    public static int select(int[] a, int k, Metrics metrics) {
        if (a == null || a.length == 0) throw new IllegalArgumentException("Empty array");
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("Invalid k");
        return select(a, 0, a.length - 1, k, 1, metrics);
    }

    private static int select(int[] a, int lo, int hi, int k, int depth, Metrics m) {
        m.recursiveCalls++;
        m.maxRecursionDepth = Math.max(m.maxRecursionDepth, depth);
        while (true) {
            if (lo == hi) return a[lo];

            int pivot = medianOfMedians(a, lo, hi, depth + 1, m);
            int[] bounds = partition3(a, lo, hi, pivot, m);
            int leftEnd = bounds[0], rightStart = bounds[1];

            if (k < leftEnd - lo + 1) {
                hi = leftEnd;
                depth++;
                m.maxRecursionDepth = Math.max(m.maxRecursionDepth, depth);
                m.recursiveCalls++;
            } else if (k <= rightStart) {
                return pivot;
            } else {
                k -= (rightStart - lo + 1);
                lo = rightStart + 1;
                depth++;
                m.maxRecursionDepth = Math.max(m.maxRecursionDepth, depth);
                m.recursiveCalls++;
            }
        }
    }

    private static int medianOfMedians(int[] a, int lo, int hi, int depth, Metrics m) {
        int n = hi - lo + 1;
        if (n <= 5) {
            insertionSort(a, lo, hi, m);
            return a[lo + n / 2];
        }

        int count = 0;
        for (int start = lo; start <= hi; start += 5) {
            int end = Math.min(start + 4, hi);
            insertionSort(a, start, end, m);
            int median = start + (end - start) / 2;
            swap(a, lo + count, median, m);
            count++;
        }
        // Select the median of the medians. This is itself a smaller problem.
        return select(a, lo, lo + count - 1, count / 2, depth + 1, m);
    }

    private static int[] partition3(int[] a, int lo, int hi, int pivot, Metrics m) {
        int lt = lo, i = lo, gt = hi;
        while (i <= gt) {
            m.comparisons++;
            if (a[i] < pivot) swap(a, lt++, i++, m);
            else if (a[i] > pivot) swap(a, i, gt--, m);
            else i++;
        }
        return new int[]{lt - 1, gt};
    }

    private static void insertionSort(int[] a, int lo, int hi, Metrics m) {
        for (int i = lo + 1; i <= hi; i++) {
            int key = a[i], j = i - 1;
            while (j >= lo) {
                m.comparisons++;
                if (a[j] <= key) break;
                a[j + 1] = a[j];
                m.swaps++;
                j--;
            }
            a[j + 1] = key;
        }
    }

    private static void swap(int[] a, int i, int j, Metrics m) {
        if (i == j) return;
        int t = a[i]; a[i] = a[j]; a[j] = t;
        m.swaps++;
    }
}
