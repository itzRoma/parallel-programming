package com.itzroma.kpi.semester5.parallelprogramming.pplab0;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.Vector;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

// F1 (1.4) -> C = A + SORT(B) * (MA * ME)
public class T1 extends Thread {
    private final String threadName;
    private final int N;

    public T1(String threadName, int priority, int N) {
        this.threadName = threadName;
        this.setPriority(priority);
        this.N = N;
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", threadName);

        Vector A, B;
        Matrix MA, ME;
        synchronized (System.in) {

            // Calling sleep() method to allow the next thread to print starting message
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.printf("'%s' - an error occurred: %s%n", threadName, ex.getMessage());
            }

            System.out.printf("%n%s - F1 (1.4) -> C = A + SORT(B) * (MA * ME)%n", threadName);

            // Providing vectors and matrices
            System.out.printf("%n%s - Provide the vector A%n", threadName);
            A = VectorUtils.scanVector(N);
            System.out.printf("%n%s - Provide the vector B%n", threadName);
            B = VectorUtils.scanVector(N);
            System.out.printf("%n%s - Provide the matrix MA%n", threadName);
            MA = MatrixUtils.scanMatrix(N);
            System.out.printf("%n%s - Provide the matrix ME%n", threadName);
            ME = MatrixUtils.scanMatrix(N);
        }

        // Calculating and printing a result
        Vector C = VectorUtils.addVectors(
                A,
                VectorUtils.matrixVectorMultiplication(MatrixUtils.multiplyMatrices(MA, ME), VectorUtils.sorted(B))
        );
        System.out.printf("%n%s - Answer C%n" + C, threadName);

        System.out.printf("%n%nThread '%s' finished%n", threadName);
    }
}
