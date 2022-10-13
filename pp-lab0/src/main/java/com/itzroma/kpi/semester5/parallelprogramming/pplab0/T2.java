package com.itzroma.kpi.semester5.parallelprogramming.pplab0;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;

// F2 (2.10) -> MK = MA * (MG * MZ) + TRANS(ML)
public class T2 extends Thread {
    private final String threadName;
    private final int N;

    public T2(String threadName, int priority, int N) {
        this.threadName = threadName;
        this.setPriority(priority);
        this.N = N;
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", threadName);

        Matrix MA, MG, MZ, ML;
        synchronized (System.in) {

            // Calling sleep() method to allow the previous thread to print the result
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.printf("'%s' - an error occurred: %s%n", threadName, ex.getMessage());
            }

            System.out.printf("%n%s - F2 (2.10) -> MK = MA * (MG * MZ) + TRANS(ML)%n", threadName);

            // Providing matrices
            System.out.printf("%n%s - Provide the matrix MA%n", threadName);
            MA = MatrixUtils.scanMatrix(N);
            System.out.printf("%n%s - Provide the matrix MG%n", threadName);
            MG = MatrixUtils.scanMatrix(N);
            System.out.printf("%n%s - Provide the matrix MZ%n", threadName);
            MZ = MatrixUtils.scanMatrix(N);
            System.out.printf("%n%s - Provide the matrix ML%n", threadName);
            ML = MatrixUtils.scanMatrix(N);
        }

        // Calculating and printing a result
        Matrix MK = MatrixUtils.addMatrices(
                MatrixUtils.multiplyMatrices(MA, MatrixUtils.multiplyMatrices(MG, MZ)),
                MatrixUtils.transposeMatrix(ML)
        );
        System.out.printf("%n%s - Answer MK%n" + MK, threadName);

        System.out.printf("%n%nThread '%s' finished%n", threadName);
    }
}
