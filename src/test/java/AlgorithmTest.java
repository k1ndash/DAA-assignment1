import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class AlgorithmTest {
    @Test
    void sortingHandlesEdgeCasesAndTypes() {
        int[][] cases = {
            {}, {1}, {2, 1}, {3, 3, 1, 2, 1},
            {1, 2, 3, 4, 5}, {5, 4, 3, 2, 1}
        };
        for (int[] input : cases) {
            int[] expected = input.clone();
            Arrays.sort(expected);

            int[] a = input.clone();
            MergeSorter.sort(a, new Metrics());
            assertArrayEquals(expected, a);

            a = input.clone();
            QuickSorter.sort(a, new Metrics());
            assertArrayEquals(expected, a);
        }
    }

    @Test
    void deterministicSelectPasses100RandomTests() {
        Random r = new Random(42);
        for (int test = 0; test < 100; test++) {
            int n = 1 + r.nextInt(200);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = r.nextInt(50);
            int k = r.nextInt(n);
            int[] expected = a.clone();
            Arrays.sort(expected);
            assertEquals(expected[k], DeterministicSelector.select(a, k, new Metrics()));
        }
    }

    @Test
    void closestPairMatchesBruteForce() {
        Random r = new Random(99);
        for (int test = 0; test < 50; test++) {
            int n = 2 + r.nextInt(30);
            Point[] p = new Point[n];
            for (int i = 0; i < n; i++) p[i] = new Point(r.nextDouble() * 100, r.nextDouble() * 100);
            double expected = brute(p);
            double actual = ClosestPairSolver.solve(p, new Metrics()).distance();
            assertEquals(expected, actual, 1e-9);
        }
    }

    private static double brute(Point[] p) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < p.length; i++)
            for (int j = i + 1; j < p.length; j++)
                best = Math.min(best, ClosestPairSolver.distance(p[i], p[j]));
        return best;
    }
}
