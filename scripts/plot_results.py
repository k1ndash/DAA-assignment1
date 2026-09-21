import csv
from pathlib import Path
import matplotlib.pyplot as plt

rows = list(csv.DictReader(open("results/results.csv", newline="")))

def plot_algorithm(algorithm, yfield, ylabel, filename):
    subset = [r for r in rows if r["algorithm"] == algorithm]
    if not subset:
        return
    plt.figure(figsize=(8, 5))
    for typ in sorted(set(r["input_type"] for r in subset)):
        data = sorted((int(r["n"]), int(r[yfield])) for r in subset if r["input_type"] == typ)
        plt.plot([x for x, _ in data], [y for _, y in data], marker="o", label=typ)
    plt.xlabel("n")
    plt.ylabel(ylabel)
    plt.title(f"{algorithm}: {ylabel} vs n")
    plt.legend()
    plt.tight_layout()
    out = Path("docs/plots")
    out.mkdir(parents=True, exist_ok=True)
    plt.savefig(out / filename, dpi=160)
    plt.close()

plot_algorithm("MergeSort", "time_ns", "Time (ns)", "mergesort_time.png")
plot_algorithm("QuickSort", "time_ns", "Time (ns)", "quicksort_time.png")
plot_algorithm("QuickSort", "recursion_depth", "Maximum recursion depth", "quicksort_depth.png")
plot_algorithm("MergeSort", "recursion_depth", "Maximum recursion depth", "mergesort_depth.png")
