package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.Vector;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

import java.util.Arrays;

/**
 * Z = sort(D * (ME * MM)) + (B * C) * E * x
 * <p>
 * All the scalars, vector's and matrix elements are 1's.
 */
public class SingleThreadedLab3 {
    public static void main(String[] args) {
        int size = 16;

        double[] vectorElements = new double[size];
        Arrays.fill(vectorElements, 1.0);

        double[][] matrixElements = new double[size][size];
        Arrays.stream(matrixElements).forEach(row -> Arrays.fill(row, 1.0));

        double scalar = 1.0;
        Vector vector = new Vector(vectorElements);
        Matrix matrix = new Matrix(matrixElements);

        long start = System.currentTimeMillis();

        Vector vectorZ = VectorUtils.addVectors(
                VectorUtils.sorted(VectorUtils.matrixVectorMultiplication(MatrixUtils.multiplyMatrices(matrix, matrix), vector)),
                VectorUtils.vectorScalarMultiplication(VectorUtils.multiplyVectors(VectorUtils.multiplyVectors(vector, vector), vector), scalar)
        );

        long end = System.currentTimeMillis();

        System.out.printf("Answer Z%n%s%n", vectorZ);
        System.out.printf("%nExecution time: %d ms%n", end - start);
    }
}
