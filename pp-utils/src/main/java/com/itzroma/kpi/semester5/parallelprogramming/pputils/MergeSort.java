package com.itzroma.kpi.semester5.parallelprogramming.pputils;

import java.util.Arrays;

public class MergeSort {
    private MergeSort() {
    }

    public static void sortDoubles(double[] array, int from, int to) {
        Arrays.sort(array, from, to); // using dual-pivot quick sort
    }

    public static void mergeDoubles(double[] array, int from, int to) {
        int m = from + (to - from) / 2;

        int n1 = m - from + 1;
        int n2 = to - m;

        double[] left = new double[n1];
        double[] right = new double[n2];

        System.arraycopy(array, from, left, 0, n1);
        System.arraycopy(array, m + 1, right, 0, n2);

        int i = 0;
        int j = 0;

        int k = from;
        while (i < n1 && j < n2) {
            if (left[i] <= right[j]) {
                array[k] = left[i];
                i++;
            } else {
                array[k] = right[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = left[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = right[j];
            j++;
            k++;
        }
    }
}
