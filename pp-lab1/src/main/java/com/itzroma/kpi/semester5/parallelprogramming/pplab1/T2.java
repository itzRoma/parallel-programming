package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;

public class T2 extends Thread {
    private final Resources resources;

    private final int begin;
    private final int end;

    public T2(Resources resources) {
        this.resources = resources;
        setName("T2");

        begin = (2 - 1) * resources.getH(); // (threadNumber - 1) * H
        end = begin + resources.getH();
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        try {
            // 1. Введення: MX.
            Lab1.INPUT_LOCK.lock();
            System.out.printf("%n%s - Provide the matrix 'MX'%n", getName());
            resources.setMatrixMX(MatrixUtils.scanMatrix(resources.getN()));
            Lab1.INPUT_LOCK.unlock();

            // 2. Сигнал задачам Т1, Т3, Т4 про введення MX.
            Lab1.SEM_2.release(resources.getP() - 1);

            // 3. Чекати сигнал про введення даних у задачах T1, T3, T4.
            Lab1.SEM_1.acquire();
            Lab1.SEM_3.acquire();
            Lab1.SEM_4.acquire();

            // 4. Обчислення 1: m2 = min(ZH).
            double scalarMi2 = resources.getVectorZ().getElement(begin);
            for (int i = begin; i < end; i++) {
                scalarMi2 = Math.min(resources.getVectorZ().getElement(i), scalarMi2);
            }

            // 5. Обчислення 2: m = min(m, m2).
            Lab1.M_LOCK.lock();
            resources.setSharedScalarM(Math.min(resources.getSharedScalarM(), scalarMi2));
            Lab1.M_LOCK.unlock();

            // 6. Сигнал задачам T1, T3, T4 про обчислення m.
            Lab1.SEM_6.release(resources.getP() - 1);

            // 7. Чекати сигнал про обчислення m у задачах T1, T3, T4.
            Lab1.SEM_5.acquire();
            Lab1.SEM_7.acquire();
            Lab1.SEM_8.acquire();

            // 8. Копіювання: MD2 = MD.
            Matrix matrixMD2 = resources.getSharedMatrixMD().copy();

            // 9. Копіювання: d2 = d.
            double scalarD2 = resources.getSharedScalarD();

            // 10. Копіювання: m2 = m.
            Lab1.M_LOCK.lock();
            double scalarM2 = resources.getSharedScalarM();
            Lab1.M_LOCK.unlock();

            // 11. Копіювання: p2 = p.
            double scalarP2 = resources.getSharedScalarP();

            // 12. Обчислення 3: MAH = MD2 * MCH * d2 + m2 * MXH * p2.
            for (int i = 0; i < resources.getN(); i++) {
                for (int j = begin; j < end; j++) {
                    double newElement = MatrixUtils.multiplyMatricesCell(matrixMD2, resources.getMatrixMC(), i, j) * scalarD2
                            + scalarM2 * resources.getMatrixMX().getElement(i, j) * scalarP2;
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
