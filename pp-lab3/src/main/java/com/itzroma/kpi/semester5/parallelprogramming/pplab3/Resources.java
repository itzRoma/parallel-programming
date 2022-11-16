package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.Vector;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * Z = sort(D * (ME * MM)) + (B * C) * E * x
 */
@Getter
@Setter
public class Resources {
    private static final double INITIAL_SCALAR_VALUE = Double.MIN_VALUE;
    private static final String ERROR_MESSAGE_FORMAT = "%n%s - An error occurred: %s%n";

    private final int n; // size of vectors and matrices
    private final int p; // amount of processors (threads)
    private final int h; // size of subvectors and submatrices

    private Vector vectorZ;
    private Matrix matrixMM;
    private Vector vectorB;
    private Vector vectorC;
    private Vector vectorE;
    private double scalarX = INITIAL_SCALAR_VALUE;

    // shared resources
    private Vector vectorD;
    private Matrix matrixME;

    // intermediate resources
    private final Matrix matrixMA;
    private final Vector vectorA;
    private final Vector vectorF;
    private final Vector vectorG;

    private boolean t1SortingCompleted;
    private boolean t3SortingCompleted;

    private boolean t2MergeCompleted;
    private boolean t4MergeCompleted;

    private int calculationsCompleteCounter;

    public Resources(int n, int p) {
        if (n <= 0) throw new IllegalArgumentException("Invalid size of vectors and matrices");
        if (p <= 0) throw new IllegalArgumentException("Invalid amount of processors (threads)");

        if (n % p != 0) throw new IllegalStateException("N should be exactly divisible by P");

        this.n = n;
        this.p = p;
        h = n / p;

        vectorZ = new Vector(n);

        matrixMA = new Matrix(n, n);
        vectorA = new Vector(n);
        vectorF = new Vector(n);
        vectorG = new Vector(n);
    }

    private boolean isFilled() {
        return vectorB != null && vectorE != null                       // from T1
                && vectorD != null && matrixMM != null                  // from T2
                && matrixME != null                                     // from T3
                && vectorC != null && scalarX != INITIAL_SCALAR_VALUE;  // from T4
    }

    public synchronized void signalOthersAboutCompletionOfInput() {
        if (isFilled()) {
            System.out.printf("%n---> All the required data was provided, starting calculation... <---%n");
            notifyAll();
        }
    }

    public synchronized void waitForOthersToCompleteInput() {
        while (!isFilled()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalT2AboutCompletionOfSortingInT1() {
        t1SortingCompleted = true;
        notifyAll();
    }

    public synchronized void waitForT1ToCompleteSorting() {
        while (!t1SortingCompleted) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalT4AboutCompletionOfMergingInT2() {
        t2MergeCompleted = true;
        notifyAll();
    }

    public synchronized void waitForT2ToCompleteMerging() {
        while (!t2MergeCompleted) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalT4AboutCompletionOfSortingInT3() {
        t3SortingCompleted = true;
        notifyAll();
    }

    public synchronized void waitForT3ToCompleteSorting() {
        while (!t3SortingCompleted) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalOthersAboutCompletionOfMergingInT4() {
        t4MergeCompleted = true;
        notifyAll();
    }

    public synchronized void waitForT4ToCompleteMerging() {
        while (!t4MergeCompleted) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalT4AboutCompletionOfCalculations() {
        calculationsCompleteCounter++;
        if (calculationsCompleteCounter == p - 1) {
            notifyAll();
        }
    }

    public synchronized void waitForOthersToCompleteCalculations() {
        while (calculationsCompleteCounter != p - 1) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized double copyScalarX() {
        return scalarX;
    }

    public void sortA(int from, int to) {
        Arrays.sort(vectorA.elements(), from, to);
    }

    public void mergeA(int from, int to) {
        double[] arr = vectorA.elements();
        int m = from + (to - from) / 2;

        int n1 = m - from + 1;
        int n2 = to - m;

        double[] left = new double[n1];
        double[] right = new double[n2];

        System.arraycopy(arr, from, left, 0, n1);
        System.arraycopy(arr, m + 1, right, 0, n2);

        int i = 0;
        int j = 0;

        int k = from;
        while (i < n1 && j < n2) {
            if (left[i] <= right[j]) {
                arr[k] = left[i];
                i++;
            } else {
                arr[k] = right[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = left[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = right[j];
            j++;
            k++;
        }
    }
}
