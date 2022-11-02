package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.Vector;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

/**
 * MA = MD * MC * d + min(Z) * MX * p
 * <p>
 * Size: 8
 * <p>
 * MD = {{1, 1, ..., 1}, ..., {1, 1, ..., 1}}
 * <p>
 * MC = {{2, 2, ..., 2}, ..., {2, 2, ..., 2}}
 * <p>
 * d = 3
 * <p>
 * Z = {4, 4, ..., 4}
 * <p>
 * MX = {{5, 5, ..., 5}, ..., {5, 5, ..., 5}}
 * <p>
 * p = 6
 */
public class SingleThreadedLab1 {
    public static void main(String[] args) {
        Matrix MD = new Matrix(new double[][]{
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1}
        });
        Matrix MC = new Matrix(new double[][]{
                {2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2},
                {2, 2, 2, 2, 2, 2, 2, 2}
        });
        double d = 3;
        Vector Z = new Vector(new double[]{4, 4, 4, 4, 4, 4, 4, 4});
        Matrix MX = new Matrix(new double[][]{
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5}
        });
        double p = 6;

        Matrix MA = MatrixUtils.addMatrices(
                MatrixUtils.matrixScalarMultiplication(MatrixUtils.multiplyMatrices(MD, MC), d),
                MatrixUtils.matrixScalarMultiplication(MatrixUtils.matrixScalarMultiplication(MX, VectorUtils.min(Z)), p)
        );

        System.out.println(MA);
    }
}
