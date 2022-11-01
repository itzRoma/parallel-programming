package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.Vector;
import lombok.Getter;
import lombok.Setter;

/**
 * F (5) -> MA = MD * MC * d + min(Z) * MX * p
 */
@Getter
@Setter
public class Resources {
    private final int n; // size of vectors and matrices
    private final int p; // amount of processors (threads)
    private final int h; // size of subvectors and submatrices

    private Matrix matrixMA;
    private Matrix matrixMC;
    private Matrix matrixMX;
    private Vector vectorZ;

    // shared resources
    private Matrix sharedMatrixMD;
    private double sharedScalarD;
    private double sharedScalarP;
    private double sharedScalarM;

    public Resources(int n, int p) {
        if (n <= 0) throw new IllegalArgumentException("Invalid size of vectors and matrices");
        if (p <= 0) throw new IllegalArgumentException("Invalid amount of processors (threads)");

        this.n = n;
        this.p = p;
        h = n / p;

        matrixMA = new Matrix(new double[n][n]);
        matrixMC = new Matrix(new double[n][n]);
        matrixMX = new Matrix(new double[n][n]);
        vectorZ = new Vector(new double[n]);
        sharedMatrixMD = new Matrix(new double[n][n]);
        sharedScalarM = Double.MAX_VALUE;
    }
}
