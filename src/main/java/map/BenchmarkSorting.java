package main.java.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BenchmarkSorting {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        int[] sizes = {100, 500, 1000, 2000, 5000, 10000};

        for (int n : sizes) {
            System.out.println("\n==========================");
            System.out.println("Input size " + n);
            System.out.println("==========================");

            runBenchmarksForPattern("Random", generateRandomData(n));
            runBenchmarksForPattern("Ascending", generateAscendingData(n));
            runBenchmarksForPattern("Descending", generateDescendingData(n));
            runBenchmarksForPattern("Partially Sorted", generatePartiallySortedData(n));
        }
    }

    private static void runBenchmarksForPattern(String pattern, List<Integer> data) {
        System.out.println("\nPattern: " + pattern);

        benchmarkTreapSort(data);
        benchmarkPQSort(data);
        benchmarkJavaSort(data);
        benchmarkMergeSort(data);
        benchmarkQuickSort(data);
    }

    // ---------- Benchmark methods ----------

    private static void benchmarkTreapSort(List<Integer> data) {
        long sortTime = timeTreapSort(data);
        printResult("TreapSort", sortTime);
    }

    private static void benchmarkPQSort(List<Integer> data) {
        long sortTime = timePQSort(data);
        printResult("PQSort", sortTime);
    }

    private static void benchmarkJavaSort(List<Integer> data) {
        long sortTime = timeJavaSort(data);
        printResult("JavaSort", sortTime);
    }

    private static void benchmarkMergeSort(List<Integer> data) {
        long sortTime = timeMergeSort(data);
        printResult("MergeSort", sortTime);
    }

    private static void benchmarkQuickSort(List<Integer> data) {
        long sortTime = timeQuickSort(data);
        printResult("QuickSort", sortTime);
    }

    private static void printResult(String name, long time) {
        System.out.println(name + ":");
        System.out.println("  Sort time = " + time + " ns");
    }

    // ---------- Timing ----------

    private static long timeTreapSort(List<Integer> data) {
        List<Integer> copy = new ArrayList<>(data);
        long start = System.nanoTime();
        SortingAlgorithms.treapSort(copy);
        return System.nanoTime() - start;
    }

    private static long timePQSort(List<Integer> data) {
        List<Integer> copy = new ArrayList<>(data);
        long start = System.nanoTime();
        SortingAlgorithms.pqSort(copy);
        return System.nanoTime() - start;
    }

    private static long timeJavaSort(List<Integer> data) {
        List<Integer> copy = new ArrayList<>(data);
        long start = System.nanoTime();
        SortingAlgorithms.javaSort(copy);
        return System.nanoTime() - start;
    }

    private static long timeMergeSort(List<Integer> data) {
        List<Integer> copy = new ArrayList<>(data);
        long start = System.nanoTime();
        SortingAlgorithms.mergeSort(copy);
        return System.nanoTime() - start;
    }

    private static long timeQuickSort(List<Integer> data) {
        List<Integer> copy = new ArrayList<>(data);
        long start = System.nanoTime();
        SortingAlgorithms.quickSort(copy);
        return System.nanoTime() - start;
    }

    // ---------- Data generation ----------

    private static List<Integer> generateRandomData(int n) {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            data.add(RANDOM.nextInt(n * 10));
        }
        return data;
    }

    private static List<Integer> generateAscendingData(int n) {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            data.add(i);
        }
        return data;
    }

    private static List<Integer> generateDescendingData(int n) {
        List<Integer> data = new ArrayList<>();
        for (int i = n - 1; i >= 0; i--) {
            data.add(i);
        }
        return data;
    }

    private static List<Integer> generatePartiallySortedData(int n) {
        List<Integer> data = generateAscendingData(n);

        int swaps = Math.max(1, n / 10);
        for (int i = 0; i < swaps; i++) {
            int a = RANDOM.nextInt(n);
            int b = RANDOM.nextInt(n);
            Collections.swap(data, a, b);
        }

        return data;
    }
}