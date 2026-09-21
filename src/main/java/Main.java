import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Assignment 1: Divide-and-Conquer Algorithm Analysis");
        runCorrectnessDemo();
        if (args.length > 0 && args[0].equalsIgnoreCase("experiment")) {
            Experiment.run();
        } else {
            System.out.println("Run `mvn exec:java -Dexec.args=experiment` to generate results/results.csv");
        }
    }

    private static void runCorrectnessDemo() {
        int[] original = {7, 2, 9, 2, 1, 8, 3};
        int[] expected = original.clone();
        Arrays.sort(expected);

        int[] a = original.clone();
        Metrics m1 = new Metrics();
        MergeSorter.sort(a, m1);
        System.out.println("MergeSort correct: " + Arrays.equals(a, expected));

        a = original.clone();
        Metrics m2 = new Metrics();
        QuickSorter.sort(a, m2);
        System.out.println("QuickSort correct: " + Arrays.equals(a, expected));

        a = original.clone();
        Metrics m3 = new Metrics();
        int selected = DeterministicSelector.select(a, 3, m3);
        System.out.println("Deterministic Select k=3 correct: " + (selected == expected[3]));

        Point[] points = {
            new Point(0, 0), new Point(5, 5), new Point(1, 1), new Point(8, 2)
        };
        Metrics m4 = new Metrics();
        ClosestPairSolver.Result result = ClosestPairSolver.solve(points, m4);
        System.out.printf(Locale.US, "Closest Pair distance: %.6f%n", result.distance());
    }
}
