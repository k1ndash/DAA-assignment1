public class Metrics {
    public long comparisons;
    public long swaps;
    public long recursiveCalls;
    public int maxRecursionDepth;

    public void reset() {
        comparisons = 0;
        swaps = 0;
        recursiveCalls = 0;
        maxRecursionDepth = 0;
    }
}
