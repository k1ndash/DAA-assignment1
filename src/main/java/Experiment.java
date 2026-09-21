import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;

public final class Experiment {
    private Experiment() {}

    public static void run() throws IOException {
        Path out = Paths.get("results");
        Files.createDirectories(out);
        Path csv = out.resolve("results.csv");

        List<String> rows = new ArrayList<>();
        rows.add("algorithm,input_type,n,time_ns,recursion_depth,metric,metric_value");

        int[] sizes = {100, 500, 1000, 5000, 10000, 20000};
        String[] types = {"random", "sorted", "reverse", "duplicate-heavy"};

        for (int n : sizes) {
            for (String type : types) {
                int[] base = makeArray(n, type);
                addSortResult(rows, "MergeSort", type, n, base, a -> MergeSorter.sort(a, currentMetrics));
                addSortResult(rows, "QuickSort", type, n, base, a -> QuickSorter.sort(a, currentMetrics));
            }
        }

        for (int n : new int[]{100, 500, 1000, 5000, 10000}) {
            for (String type : types) {
                int[] base = makeArray(n, type);
                addSelectResult(rows, type, n, base);
            }
        }

        for (int n : new int[]{100, 500, 1000, 5000, 10000}) {
            Point[] pts = makePoints(n);
            addClosestResult(rows, "random", n, pts);
        }

        Files.write(csv, rows);
        System.out.println("Saved " + csv.toAbsolutePath());
    }

    private static Metrics currentMetrics;

    private static void addSortResult(List<String> rows, String alg, String type, int n,
                                      int[] base, Consumer<int[]> sorter) {
        int[] a = base.clone();
        currentMetrics = new Metrics();
        // Warm-up is intentionally separate from the measured call.
        sorter.accept(a.clone());

        a = base.clone();
        currentMetrics.reset();
        long t0 = System.nanoTime();
        sorter.accept(a);
        long elapsed = System.nanoTime() - t0;
        rows.add(String.format(Locale.US, "%s,%s,%d,%d,%d,comparisons,%d",
                alg, type, n, elapsed, currentMetrics.maxRecursionDepth, currentMetrics.comparisons));
    }

    private static void addSelectResult(List<String> rows, String type, int n, int[] base) {
        int[] a = base.clone();
        Metrics m = new Metrics();
        int k = n / 2;
        DeterministicSelector.select(a, k, m); // warm-up
        a = base.clone();
        m.reset();
        long t0 = System.nanoTime();
        DeterministicSelector.select(a, k, m);
        long elapsed = System.nanoTime() - t0;
        rows.add(String.format(Locale.US, "DeterministicSelect,%s,%d,%d,%d,comparisons,%d",
                type, n, elapsed, m.maxRecursionDepth, m.comparisons));
    }

    private static void addClosestResult(List<String> rows, String type, int n, Point[] base) {
        Metrics m = new Metrics();
        ClosestPairSolver.solve(base, m); // warm-up
        m.reset();
        long t0 = System.nanoTime();
        ClosestPairSolver.solve(base, m);
        long elapsed = System.nanoTime() - t0;
        rows.add(String.format(Locale.US, "ClosestPair,%s,%d,%d,%d,comparisons,%d",
                type, n, elapsed, m.maxRecursionDepth, m.comparisons));
    }

    private static int[] makeArray(int n, String type) {
        Random r = new Random(12345L + n + type.hashCode());
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt(Math.max(1, n * 10));
        if (type.equals("sorted")) Arrays.sort(a);
        if (type.equals("reverse")) {
            Arrays.sort(a);
            for (int i = 0; i < n / 2; i++) {
                int t = a[i]; a[i] = a[n - 1 - i]; a[n - 1 - i] = t;
            }
        }
        if (type.equals("duplicate-heavy")) {
            for (int i = 0; i < n; i++) a[i] = r.nextInt(10);
        }
        return a;
    }

    private static Point[] makePoints(int n) {
        Random r = new Random(777L + n);
        Point[] p = new Point[n];
        for (int i = 0; i < n; i++) p[i] = new Point(r.nextDouble() * 10000, r.nextDouble() * 10000);
        return p;
    }
}
