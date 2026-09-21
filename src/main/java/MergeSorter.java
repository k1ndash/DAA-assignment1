public final class MergeSorter {
    private static final int INSERTION_CUTOFF = 16;

    private MergeSorter() {}

    public static void sort(int[] a, Metrics metrics) {
        if (a == null || a.length < 2) return;
        int[] aux = new int[a.length];
        sort(a, aux, 0, a.length - 1, 1, metrics);
    }

    private static void sort(int[] a, int[] aux, int lo, int hi, int depth, Metrics m) {
        m.recursiveCalls++;
        m.maxRecursionDepth = Math.max(m.maxRecursionDepth, depth);
        if (hi - lo + 1 <= INSERTION_CUTOFF) {
            insertionSort(a, lo, hi, m);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid, depth + 1, m);
        sort(a, aux, mid + 1, hi, depth + 1, m);
        if (a[mid] <= a[mid + 1]) {
            m.comparisons++;
            return;
        }
        merge(a, aux, lo, mid, hi, m);
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

    private static void merge(int[] a, int[] aux, int lo, int mid, int hi, Metrics m) {
        for (int i = lo; i <= hi; i++) aux[i] = a[i];
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else {
                m.comparisons++;
                if (aux[i] <= aux[j]) a[k] = aux[i++];
                else { a[k] = aux[j++]; m.swaps++; }
            }
        }
    }
}
