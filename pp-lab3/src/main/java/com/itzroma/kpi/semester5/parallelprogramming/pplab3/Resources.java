package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.Vector;
import lombok.Getter;
import lombok.Setter;

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
    private double scalarR;
    private final Vector vectorG;

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
        vectorG = new Vector(n);
    }
}
