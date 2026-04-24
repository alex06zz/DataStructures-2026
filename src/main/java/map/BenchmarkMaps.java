package main.java.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class BenchmarkMaps {

    private static final Random RANDOM = new Random();
    private static final int WARMUP_RUNS = 5;

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

        benchmarkTreap(data);
        benchmarkAVL(data);
        benchmarkJavaTreeMap(data);
    }

    private static void benchmarkTreap(List<Integer> data) {
        TreapMap<Integer, Integer> treap = new TreapMap<>();

        long insertTime = timeTreapInsert(treap, data);
        long singleInsertTime = timeTreapSingleInsert(treap, data.size() + 1000000);
        long successfulSearchTime = timeTreapSuccessfulSearch(treap, data);
        long unsuccessfulSearchTime = timeTreapUnsuccessfulSearch(treap, data.size());
        long traversalTime = timeTreapTraversal(treap);
        long deleteTime = timeTreapDelete(treap, data);

        printResults("Treap", insertTime, singleInsertTime, successfulSearchTime, unsuccessfulSearchTime, traversalTime, deleteTime);
    }

    private static void benchmarkAVL(List<Integer> data) {
        AVLTree<Integer, Integer> avl = new AVLTree<>();

        long insertTime = timeAVLInsert(avl, data);
        long singleInsertTime = timeAVLSingleInsert(avl, data.size() + 1000000);
        long successfulSearchTime = timeAVLSuccessfulSearch(avl, data);
        long unsuccessfulSearchTime = timeAVLUnsuccessfulSearch(avl, data.size());
        long traversalTime = timeAVLTraversal(avl);
        long deleteTime = timeAVLDelete(avl, data);

        printResults("AVL", insertTime, singleInsertTime, successfulSearchTime, unsuccessfulSearchTime, traversalTime, deleteTime);
    }

    private static void benchmarkJavaTreeMap(List<Integer> data) {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();

        long insertTime = timeJavaTreeMapInsert(treeMap, data);
        long successfulSearchTime = timeJavaTreeMapSuccessfulSearch(treeMap, data);
        long singleInsertTime = timeJavaTreeMapSingleInsert(treeMap, data.size() + 1000000);
        long unsuccessfulSearchTime = timeJavaTreeMapUnsuccessfulSearch(treeMap, data.size());
        long traversalTime = timeJavaTreeMapTraversal(treeMap);
        long deleteTime = timeJavaTreeMapDelete(treeMap, data);

        printResults("Java TreeMap", insertTime,  singleInsertTime, successfulSearchTime, unsuccessfulSearchTime, traversalTime, deleteTime);
    }

    private static void printResults(String structureName,
                                     long insertTime,
                                     long singleInsertTime,
                                     long successfulSearchTime,
                                     long unsuccessfulSearchTime,
                                     long traversalTime,
                                     long deleteTime) {
        System.out.println(structureName + ":");
        System.out.println("  Insert time              = " + insertTime + " ns");
        System.out.println("  Single insert time       = " + singleInsertTime + " ns");
        System.out.println("  Successful search time   = " + successfulSearchTime + " ns");
        System.out.println("  Unsuccessful search time = " + unsuccessfulSearchTime + " ns");
        System.out.println("  Traversal time           = " + traversalTime + " ns");
        System.out.println("  Delete time              = " + deleteTime + " ns");
    }

    // ---------- Treap timing ----------

    private static long timeTreapInsert(TreapMap<Integer, Integer> treap, List<Integer> data) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            TreapMap<Integer, Integer> warmupTreap = new TreapMap<>();
            for (Integer x : data) {
                warmupTreap.put(x, x);
            }
        }

        long start = System.nanoTime();
        for (Integer x : data) {
            treap.put(x, x);
        }
        return System.nanoTime() - start;
    }

    private static long timeTreapSingleInsert(TreapMap<Integer, Integer> treap, int key) {
        int trials = 1000;

        // warmup
        for (int i = 0; i < WARMUP_RUNS; i++) {
            for (int j = 0; j < trials; j++) {
                treap.put(key + j + 1000000, key);
                treap.remove(key + j + 1000000);
            }
        }

        long start = System.nanoTime();
        treap.put(key, key);
        long end = System.nanoTime();

        treap.remove(key); // clean up

        return end - start;
    }

    private static long timeTreapSuccessfulSearch(TreapMap<Integer, Integer> treap, List<Integer> data) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            for (Integer x : data) {
                treap.get(x);
            }
        }

        long start = System.nanoTime();
        for (Integer x : data) {
            treap.get(x);
        }
        return System.nanoTime() - start;
    }

    private static long timeTreapUnsuccessfulSearch(TreapMap<Integer, Integer> treap, int n) {
        for (int k = 0; k < WARMUP_RUNS; k++) {
            for (int i = n; i < 2 * n; i++) {
                treap.get(i + 1000000);
            }
        }

        long start = System.nanoTime();
        for (int i = n; i < 2 * n; i++) {
            treap.get(i + 1000000);
        }
        return System.nanoTime() - start;
    }

    private static long timeTreapTraversal(TreapMap<Integer, Integer> treap) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            treap.inorder();
        }

        long start = System.nanoTime();
        treap.inorder();
        return System.nanoTime() - start;
    }

    private static long timeTreapDelete(TreapMap<Integer, Integer> treap, List<Integer> data) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            TreapMap<Integer, Integer> warmupTreap = new TreapMap<>();
            for (Integer x : data) {
                warmupTreap.put(x, x);
            }
            for (Integer x : data) {
                warmupTreap.remove(x);
            }
        }

        long start = System.nanoTime();
        for (Integer x : data) {
            treap.remove(x);
        }
        return System.nanoTime() - start;
    }

    // ---------- AVL timing ----------

    private static long timeAVLInsert(AVLTree<Integer, Integer> avl, List<Integer> data) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            AVLTree<Integer, Integer> warmupAVL = new AVLTree<>();
            for (Integer x : data) {
                warmupAVL.insert(x, x);
            }
        }

        long start = System.nanoTime();
        for (Integer x : data) {
            avl.insert(x, x);
        }
        return System.nanoTime() - start;
    }

    private static long timeAVLSingleInsert(AVLTree<Integer, Integer> avl, int key) {
        int trials = 1000;

        for (int i = 0; i < WARMUP_RUNS; i++) {
            for (int j = 0; j < trials; j++) {
                avl.insert(key + j + 1000000, key);
                avl.delete(key + j + 1000000);
            }
        }

        long start = System.nanoTime();
        avl.insert(key, key);
        long end = System.nanoTime();

        avl.delete(key); // clean up

        return end - start;
    }

    private static long timeAVLSuccessfulSearch(AVLTree<Integer, Integer> avl, List<Integer> data) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            for (Integer x : data) {
                avl.search(x);
            }
        }

        long start = System.nanoTime();
        for (Integer x : data) {
            avl.search(x);
        }
        return System.nanoTime() - start;
    }

    private static long timeAVLUnsuccessfulSearch(AVLTree<Integer, Integer> avl, int n) {
        for (int k = 0; k < WARMUP_RUNS; k++) {
            for (int i = n; i < 2 * n; i++) {
                avl.search(i + 1000000);
            }
        }

        long start = System.nanoTime();
        for (int i = n; i < 2 * n; i++) {
            avl.search(i + 1000000);
        }
        return System.nanoTime() - start;
    }

    private static long timeAVLTraversal(AVLTree<Integer, Integer> avl) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            avl.inorder();
        }

        long start = System.nanoTime();
        avl.inorder();
        return System.nanoTime() - start;
    }

    private static long timeAVLDelete(AVLTree<Integer, Integer> avl, List<Integer> data) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            AVLTree<Integer, Integer> warmupAVL = new AVLTree<>();
            for (Integer x : data) {
                warmupAVL.insert(x, x);
            }
            for (Integer x : data) {
                warmupAVL.delete(x);
            }
        }

        long start = System.nanoTime();
        for (Integer x : data) {
            avl.delete(x);
        }
        return System.nanoTime() - start;
    }

    // ---------- Java TreeMap timing ----------

    private static long timeJavaTreeMapInsert(TreeMap<Integer, Integer> treeMap, List<Integer> data) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            TreeMap<Integer, Integer> warmupTreeMap = new TreeMap<>();
            for (Integer x : data) {
                warmupTreeMap.put(x, x);
            }
        }

        long start = System.nanoTime();
        for (Integer x : data) {
            treeMap.put(x, x);
        }
        return System.nanoTime() - start;
    }

    private static long timeJavaTreeMapSingleInsert(TreeMap<Integer, Integer> treeMap, int key) {
        int trials = 1000;

        for (int i = 0; i < WARMUP_RUNS; i++) {
            for (int j = 0; j < trials; j++) {
                treeMap.put(key + j + 1000000, key);
                treeMap.remove(key + j + 1000000);
            }
        }
        long start = System.nanoTime();
        treeMap.put(key, key);
        long end = System.nanoTime();

        treeMap.remove(key); // clean up

        return end - start;
    }

    private static long timeJavaTreeMapSuccessfulSearch(TreeMap<Integer, Integer> treeMap, List<Integer> data) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            for (Integer x : data) {
                treeMap.get(x);
            }
        }

        long start = System.nanoTime();
        for (Integer x : data) {
            treeMap.get(x);
        }
        return System.nanoTime() - start;
    }

    private static long timeJavaTreeMapUnsuccessfulSearch(TreeMap<Integer, Integer> treeMap, int n) {
        for (int k = 0; k < WARMUP_RUNS; k++) {
            for (int i = n; i < 2 * n; i++) {
                treeMap.get(i + 1000000);
            }
        }

        long start = System.nanoTime();
        for (int i = n; i < 2 * n; i++) {
            treeMap.get(i + 1000000);
        }
        return System.nanoTime() - start;
    }

    private static long timeJavaTreeMapTraversal(TreeMap<Integer, Integer> treeMap) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            for (var entry : treeMap.entrySet()) {
                entry.getKey();
                entry.getValue();
            }
        }

        long start = System.nanoTime();
        for (var entry : treeMap.entrySet()) {
            entry.getKey();
            entry.getValue();
        }
        return System.nanoTime() - start;
    }

    private static long timeJavaTreeMapDelete(TreeMap<Integer, Integer> treeMap, List<Integer> data) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            TreeMap<Integer, Integer> warmupTreeMap = new TreeMap<>();
            for (Integer x : data) {
                warmupTreeMap.put(x, x);
            }
            for (Integer x : data) {
                warmupTreeMap.remove(x);
            }
        }

        long start = System.nanoTime();
        for (Integer x : data) {
            treeMap.remove(x);
        }
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

        int swaps = Math.max(1, n / 10); // 10% disorder
        for (int i = 0; i < swaps; i++) {
            int a = RANDOM.nextInt(n);
            int b = RANDOM.nextInt(n);
            Collections.swap(data, a, b);
        }

        return data;
    }
}
