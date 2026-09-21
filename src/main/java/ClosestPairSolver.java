import java.util.Arrays;

public final class ClosestPairSolver {
    public record Result(Point p1, Point p2, double distance) {}

    private ClosestPairSolver() {}

    public static Result solve(Point[] points, Metrics m) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("At least two points required");
        }
        Point[] byX = points.clone();
        Point[] byY = points.clone();
        Arrays.sort(byX, (a, b) -> Double.compare(a.x(), b.x()));
        Arrays.sort(byY, (a, b) -> Double.compare(a.y(), b.y()));
        return solveRec(byX, byY, 1, m);
    }

    private static Result solveRec(Point[] px, Point[] py, int depth, Metrics m) {
        m.recursiveCalls++;
        m.maxRecursionDepth = Math.max(m.maxRecursionDepth, depth);
        int n = px.length;
        if (n <= 3) return bruteForce(px, m);

        int mid = n / 2;
        double midX = px[mid].x();
        Point[] leftX = Arrays.copyOfRange(px, 0, mid);
        Point[] rightX = Arrays.copyOfRange(px, mid, n);

        Point[] leftY = new Point[leftX.length];
        Point[] rightY = new Point[rightX.length];
        int li = 0, ri = 0;
        for (Point p : py) {
            if (li < leftY.length && (p.x() < midX || (p.x() == midX && containsIdentity(leftX, p)))) {
                leftY[li++] = p;
            } else {
                rightY[ri++] = p;
            }
        }

        Result left = solveRec(leftX, leftY, depth + 1, m);
        Result right = solveRec(rightX, rightY, depth + 1, m);
        Result best = left.distance() <= right.distance() ? left : right;
        double d = best.distance();

        Point[] strip = new Point[n];
        int s = 0;
        for (Point p : py) {
            if (Math.abs(p.x() - midX) < d) strip[s++] = p;
        }

        for (int i = 0; i < s; i++) {
            for (int j = i + 1; j < s && j <= i + 7; j++) {
                m.comparisons++;
                double dist = distance(strip[i], strip[j]);
                if (dist < d) {
                    d = dist;
                    best = new Result(strip[i], strip[j], d);
                }
            }
        }
        return best;
    }

    private static boolean containsIdentity(Point[] arr, Point target) {
        for (Point p : arr) if (p == target) return true;
        return false;
    }

    private static Result bruteForce(Point[] p, Metrics m) {
        Result best = null;
        double d = Double.POSITIVE_INFINITY;
        for (int i = 0; i < p.length; i++) {
            for (int j = i + 1; j < p.length; j++) {
                m.comparisons++;
                double cur = distance(p[i], p[j]);
                if (cur < d) {
                    d = cur;
                    best = new Result(p[i], p[j], d);
                }
            }
        }
        return best;
    }

    public static double distance(Point a, Point b) {
        return Math.hypot(a.x() - b.x(), a.y() - b.y());
    }
}
