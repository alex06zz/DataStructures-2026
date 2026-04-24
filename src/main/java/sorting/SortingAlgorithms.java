package main.java.sorting;

import main.java.map.TreapMap;

import java.util.*;

public class SortingAlgorithms {

    //retrieves in order traversal of our treap structure, this will return sorted data as treap maintains BST property
    public static List<Integer> treapSort(List<Integer> data) {
        TreapMap<Long, Integer> treap = new TreapMap<>();

        int index = 0;
        for (Integer x : data) {
            //Key based on Value and Index to cater for duplicates while maintaining order of keys
            //Handles n <= 10000 (benchmark expectations)
            long key = (long)x * 100000 + index;
            treap.put(key, x);
            index++;
        }
        List<Integer> sorted = new ArrayList<>();
        for (TreapMap.KeyValuePair<Long, Integer> pair : treap.inorder()) {
            sorted.add(pair.getValue());
        }
        return sorted;
    }

    //repeatedly returns min of a min heap (priority queue)
    public static List<Integer> pqSort(List<Integer> data) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        pq.addAll(data);

        List<Integer> sorted = new ArrayList<>();
        while (!pq.isEmpty()) {
            sorted.add(pq.poll());
        }
        return sorted;
    }

    //java built in sort (tim sort)
    public static List<Integer> javaSort(List<Integer> data) {
        List<Integer> copy = new ArrayList<>(data);
        Collections.sort(copy);
        return copy;
    }

    //MergeSort
    public static List<Integer> mergeSort(List<Integer> data) {
        List<Integer> copy = new ArrayList<>(data);

        mergeSortHelper(copy, 0, copy.size() - 1);
        return copy;
    }

    private static void mergeSortHelper(List<Integer> list, int leftIndex, int rightIndex) {

        if (leftIndex >= rightIndex) return;

        int middleIndex = (leftIndex + rightIndex) / 2;

        mergeSortHelper(list, leftIndex, middleIndex);
        mergeSortHelper(list, middleIndex + 1, rightIndex);

        merge(list, leftIndex, middleIndex, rightIndex);
    }

    private static void merge(List<Integer> list, int leftIndex, int middleIndex, int rightIndex) {

        int leftSize = middleIndex - leftIndex + 1;
        int rightSize = rightIndex - middleIndex;

        List<Integer> leftHalf = new ArrayList<>();
        List<Integer> rightHalf = new ArrayList<>();


        for (int i = 0; i < leftSize; i++) {
            leftHalf.add(list.get(leftIndex + i));
        }


        for (int j = 0; j < rightSize; j++) {
            rightHalf.add(list.get(middleIndex + 1 + j));
        }

        int leftPointer = 0;
        int rightPointer = 0;
        int mergedIndex = leftIndex;


        while (leftPointer < leftSize && rightPointer < rightSize) {

            if (leftHalf.get(leftPointer) <= rightHalf.get(rightPointer)) {
                list.set(mergedIndex, leftHalf.get(leftPointer));
                leftPointer++;
            } else {
                list.set(mergedIndex, rightHalf.get(rightPointer));
                rightPointer++;
            }

            mergedIndex++;
        }

        while (leftPointer < leftSize) {
            list.set(mergedIndex, leftHalf.get(leftPointer));
            leftPointer++;
            mergedIndex++;
        }

        while (rightPointer < rightSize) {
            list.set(mergedIndex, rightHalf.get(rightPointer));
            rightPointer++;
            mergedIndex++;
        }
    }


    //QuickSort (using randomised pivot optimisation)
    private static final Random rand = new Random();

    public static List<Integer> quickSort(List<Integer> data) {
        List<Integer> copy = new ArrayList<>(data);
        quickSortHelper(copy, 0, copy.size() - 1);
        return copy;
    }

    private static void quickSortHelper(List<Integer> list, int low, int high) {
        while (low < high) {
            int pivotIndex = partition(list, low, high);

            if (pivotIndex - low < high - pivotIndex) {
                quickSortHelper(list, low, pivotIndex - 1);
                low = pivotIndex + 1;
            } else {
                quickSortHelper(list, pivotIndex + 1, high);
                high = pivotIndex - 1;
            }
        }
    }

    private static int partition(List<Integer> list, int low, int high) {
        int randomPivotIndex = low + rand.nextInt(high - low + 1);
        swap(list, randomPivotIndex, high);

        int pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j) <= pivot) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list, i + 1, high);
        return i + 1;
    }

    private static void swap(List<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
