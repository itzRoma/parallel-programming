package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;

public class T3 extends Thread {
    private final Resources resources;

    private final int begin;
    private final int end;

    public T3(Resources resources) {
        this.resources = resources;
        setName("T3");

        begin = (3 - 1) * resources.getH(); // (threadNumber - 1) * H
        end = begin + resources.getH();
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        try {
            // 1. Введення: MC, p.
            Lab1.INPUT_LOCK.lock();
            System.out.printf("%n%s - Provide the matrix 'MC'%n", getName());
            resources.setMatrixMC(MatrixUtils.scanMatrix(resources.getN()));

            System.out.printf("%n%s - Provide scalar 'p': ", getName());
            resources.setSharedScalarP(Lab1.SC.nextDouble());
            Lab1.INPUT_LOCK.unlock();

            // 2. Сигнал задачам Т1, Т2, Т4 про введення MC і p.
            Lab1.SEM_3.release(resources.getP() - 1);

            // 3. Чекати сигнал про введення даних у задачах T1, T2, T4.
            Lab1.SEM_1.acquire();
            Lab1.SEM_2.acquire();
            Lab1.SEM_4.acquire();

            // 4. Обчислення 1: m3 = min(ZH).
            double scalarMi3 = resources.getVectorZ().getElement(begin);
            for (int i = begin; i < end; i++) {
                scalarMi3 = Math.min(resources.getVectorZ().getElement(i), scalarMi3);
            }

            // 5. Обчислення 2: m = min(m, m3).
            Lab1.M_LOCK.lock();
            resources.setSharedScalarM(Math.min(resources.getSharedScalarM(), scalarMi3));
            Lab1.M_LOCK.unlock();

            // 6. Сигнал задачам T1, T2, T4 про обчислення m.
            Lab1.SEM_7.release(resources.getP() - 1);

            // 7. Чекати сигнал про обчислення m у задачах T1, T2, T4.
            Lab1.SEM_5.acquire();
            Lab1.SEM_6.acquire();
            Lab1.SEM_8.acquire();

            // 8. Копіювання: MD3 = MD.
            Matrix matrixMD3 = resources.getSharedMatrixMD().copy();

            // 9. Копіювання: d3 = d.
            double scalarD3 = resources.getSharedScalarD();

            // 10. Копіювання: m3 = m.
            Lab1.M_LOCK.lock();
            double scalarM3 = resources.getSharedScalarM();
            Lab1.M_LOCK.unlock();

            // 11. Копіювання: p3 = p.
            double scalarP3 = resources.getSharedScalarP();

            // 12. Обчислення 3: MAH = MD3 * MCH * d3 + m3 * MXH * p3.
            for (int i = 0; i < resources.getN(); i++) {
                for (int j = begin; j < end; j++) {
                    double newElement = MatrixUtils.multiplyMatricesCell(matrixMD3, resources.getMatrixMC(), i, j) * scalarD3
                            + scalarM3 * resources.getMatrixMX().getElement(i, j) * scalarP3;
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
