package com.itzroma.kpi.semester5.parallelprogramming.pplab0;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.Vector;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

// F3 (3.4) -> O = SORT(P) * SORT(MR * MS)
public class T3 extends Thread {
    private final String threadName;
    private final int N;

    public T3(String threadName, int priority, int N) {
        this.threadName = threadName;
        this.setPriority(priority);
        this.N = N;
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", threadName);

        Vector P;
        Matrix MR, MS;
        synchronized (System.in) {

            // Calling sleep() method to allow the previous thread to print the result
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.printf("'%s' - an error occurred: %s%n", threadName, ex.getMessage());
            }

            System.out.printf("%n%s - F3 (3.4) -> O = SORT(P) * SORT(MR * MS)%n", threadName);

            // Providing vectors and matrices
            System.out.printf("%n%s - Provide the vector P%n", threadName);
            P = VectorUtils.scanVector(N);
            System.out.printf("%n%s - Provide the matrix MR%n", threadName);
            MR = MatrixUtils.scanMatrix(N);
            System.out.printf("%n%s - Provide the matrix MS%n", threadName);
            MS = MatrixUtils.scanMatrix(N);
        }

        // Calculating and printing a result
        Vector O = VectorUtils.matrixVectorMultiplication(
                MatrixUtils.sorted(MatrixUtils.multiplyMatrices(MR, MS)),
                VectorUtils.sorted(P)
        );
        System.out.printf("%n%s - Answer O%n" + O, threadName);

        System.out.printf("%n%nThread '%s' finished%n", threadName);
    }
}
