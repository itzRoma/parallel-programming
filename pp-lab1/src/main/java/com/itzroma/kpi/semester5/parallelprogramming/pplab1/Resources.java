package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.Vector;
import lombok.Getter;
import lombok.Setter;

/**
 * F (5) -> MA = MD * MC * d + min(Z) * MX * p
 */
@Getter
@Setter
public class Resources {
    private final int N;
    private final int P;
    private final int H;

    private Matrix MA;
    private Matrix MC;
    private Matrix MX;
    private Vector Z;

    private Matrix sharedMD;
    private double sharedD;
    private double sharedP;
    private volatile double sharedM;

    public Resources(int N, int P) {
        if (N <= 0) throw new IllegalArgumentException("Invalid size");
        if (P <= 0) throw new IllegalArgumentException("Invalid amount of processors");

        this.N = N;
        this.P = P;
        H = N / P;

        MA = new Matrix(new double[N][N]);
        MC = new Matrix(new double[N][N]);
        MX = new Matrix(new double[N][N]);
        Z = new Vector(new double[N]);
        sharedMD = new Matrix(new double[N][N]);
        sharedM = Double.MAX_VALUE;
    }

    public synchronized double findMi(int threadNumber) {
        int pos = (threadNumber - 1) * H;
        double mi = Z.getElement(pos);
        for (int i = pos; i < pos + H; i++) {
            mi = Math.min(Z.getElement(i), mi);
        }
        return mi;
    }

    public synchronized void calculateMAH(int threadNumber, Matrix MDi, double di, double mi, double pi) {
        int pos = (threadNumber - 1) * H;
        for (int i = 0; i < N; i++) {
            for (int j = pos; j < pos + H; j++) {
                double newElement = MatrixUtils.multiplyMatricesCell(MDi, MC, i, j) * di + mi * MX.getElement(i, j) * pi;
                MA.setElement(i, j, newElement);
            }
        }
    }
}
