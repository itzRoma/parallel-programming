package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.Vector;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

import java.util.Arrays;

/**
 * MA = MD * MC * d + min(Z) * MX * p
 * <p>
 * All the scalars, vector's and matrix elements are 1's.
 */
public class SingleThreadedLab1 {
    public static void main(String[] args) {
        int size = 1000;

        double[] vectorElements = new double[size];
        Arrays.fill(vectorElements, 1.0);

        double[][] matrixElements = new double[size][size];
        Arrays.stream(matrixElements).forEach(row -> Arrays.fill(row, 1.0));

        double scalar = 1.0;
        Vector vector = new Vector(vectorElements);
        Matrix matrix = new Matrix(matrixElements);

        long start = System.currentTimeMillis();

        Matrix matrixMA = MatrixUtils.addMatrices(
                MatrixUtils.matrixScalarMultiplication(MatrixUtils.multiplyMatrices(matrix, matrix), scalar),
                MatrixUtils.matrixScalarMultiplication(MatrixUtils.matrixScalarMultiplication(matrix, VectorUtils.min(vector)), scalar)
        );

        long end = System.currentTimeMillis();

        System.out.printf("Answer MA%n%s%n", matrixMA);
        System.out.printf("%nExecution time: %d ms%n", end - start);
    }
}
