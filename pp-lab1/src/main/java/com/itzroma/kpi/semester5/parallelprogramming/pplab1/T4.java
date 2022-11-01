package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

public class T4 extends Thread {
    private final Resources resources;

    private final int begin;
    private final int end;

    public T4(Resources resources) {
        this.resources = resources;
        setName("T4");

        begin = (4 - 1) * resources.getH(); // (threadNumber - 1) * H
        end = begin + resources.getH();
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        try {
            // 1. Введення: Z, MD.
            Lab1.INPUT_LOCK.lock();
            System.out.printf("%n%s - Provide the vector 'Z'%n", getName());
            resources.setVectorZ(VectorUtils.scanVector(resources.getN()));

            System.out.printf("%n%s - Provide the matrix 'MD'%n", getName());
            resources.setSharedMatrixMD(MatrixUtils.scanMatrix(resources.getN()));
            Lab1.INPUT_LOCK.unlock();

            // 2. Сигнал задачам Т1, Т2, Т3 про введення Z і MD.
            Lab1.SEM_4.release(resources.getP() - 1);

            // 3. Чекати сигнал про введення даних у задачах T1, T2, T3.
            Lab1.SEM_1.acquire();
            Lab1.SEM_2.acquire();
            Lab1.SEM_3.acquire();

            // 4. Обчислення 1: m4 = min(ZH).
            double scalarMi4 = resources.getVectorZ().getElement(begin);
            for (int i = begin; i < end; i++) {
                scalarMi4 = Math.min(resources.getVectorZ().getElement(i), scalarMi4);
            }

            // 5. Обчислення 2: m = min(m, m4).
            Lab1.M_LOCK.lock();
            resources.setSharedScalarM(Math.min(resources.getSharedScalarM(), scalarMi4));
            Lab1.M_LOCK.unlock();

            // 6. Сигнал задачам T1, T2, T3 про обчислення m.
            Lab1.SEM_8.release(resources.getP() - 1);

            // 7. Чекати сигнал про обчислення m у задачах T1, T2, T3.
            Lab1.SEM_5.acquire();
            Lab1.SEM_6.acquire();
            Lab1.SEM_7.acquire();

            // 8. Копіювання: MD4 = MD.
            Matrix matrixMD4 = resources.getSharedMatrixMD().copy();

            // 9. Копіювання: d4 = d.
            double scalarD4 = resources.getSharedScalarD();

            // 10. Копіювання: m4 = m.
            Lab1.M_LOCK.lock();
            double scalarM4 = resources.getSharedScalarM();
            Lab1.M_LOCK.unlock();

            // 11. Копіювання: p4 = p.
            double scalarP4 = resources.getSharedScalarP();

            // 12. Обчислення 3: MAH = MD4 * MCH * d4 + m4 * MXH * p4.
            for (int i = 0; i < resources.getN(); i++) {
                for (int j = begin; j < end; j++) {
                    double newElement = MatrixUtils.multiplyMatricesCell(matrixMD4, resources.getMatrixMC(), i, j) * scalarD4
                            + scalarM4 * resources.getMatrixMX().getElement(i, j) * scalarP4;
                    resources.getMatrixMA().setElement(i, j, newElement);
                }
            }

            // 13. Сигнал задачі T1 про обчислення MAH.
            Lab1.SEM_9.release();
        } catch (InterruptedException ex) {
            System.out.printf("%n%s - Error occurred: %s%n", getName(), ex.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
