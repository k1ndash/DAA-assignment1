import java.util.concurrent.ThreadLocalRandom;

public final class QuickSorter {
    private QuickSorter() {}

    public static void sort(int[] a, Metrics metrics) {
        if (a == null || a.length < 2) return;
        quickSort(a, 0, a.length - 1, 1, metrics);
    }

    private static void quickSort(int[] a, int lo, int hi, int depth, Metrics m) {
        while (lo < hi) {
            m.recursiveCalls++;
            m.maxRecursionDepth = Math.max(m.maxRecursionDepth, depth);

            int pivotIndex = ThreadLocalRandom.current().nextInt(lo, hi + 1);
            swap(a, pivotIndex, hi, m);
            int p = partition(a, lo, hi, m);

            // Recurse on the smaller partition; iterate over the larger.
            if (p - lo < hi - p) {
                quickSort(a, lo, p - 1, depth + 1, m);
                lo = p + 1;
            } else {
                quickSort(a, p + 1, hi, depth + 1, m);
                hi = p - 1;
            }
        }
    }

    private static int partition(int[] a, int lo, int hi, Metrics m) {
        int pivot = a[hi];
        int i = lo;
        for (int j = lo; j < hi; j++) {
            m.comparisons++;
            if (a[j] <= pivot) {
                swap(a, i++, j, m);
            }
        }
        swap(a, i, hi, m);
        return i;
    }

    private static void swap(int[] a, int i, int j, Metrics m) {
        if (i == j) return;
        int t = a[i]; a[i] = a[j]; a[j] = t;
        m.swaps++;
    }
}
