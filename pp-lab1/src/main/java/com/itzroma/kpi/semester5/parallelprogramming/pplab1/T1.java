package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;

public class T1 extends Thread {
    private final Resources resources;

    private final int begin;
    private final int end;

    public T1(Resources resources) {
        this.resources = resources;
        setName("T1");

        begin = (1 - 1) * resources.getH(); // (threadNumber - 1) * H
        end = begin + resources.getH();
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        try {
            // 1. Введення: d.
            Lab1.INPUT_LOCK.lock();
            System.out.printf("%n%s - Provide scalar 'd': ", getName());
            resources.setSharedScalarD(Lab1.SC.nextDouble());
            Lab1.INPUT_LOCK.unlock();

            // 2. Сигнал задачам Т2, Т3, Т4 про введення d.
            Lab1.SEM_1.release(resources.getP() - 1);

            // 3. Чекати сигнал про введення даних у задачах T2, T3, T4.
            Lab1.SEM_2.acquire();
            Lab1.SEM_3.acquire();
            Lab1.SEM_4.acquire();

            // 4. Обчислення 1: m1 = min(ZH).
            double scalarMi1 = resources.getVectorZ().getElement(begin);
            for (int i = begin; i < end; i++) {
                scalarMi1 = Math.min(resources.getVectorZ().getElement(i), scalarMi1);
            }

            // 5. Обчислення 2: m = min(m, m1).
            Lab1.M_LOCK.lock();
            resources.setSharedScalarM(Math.min(resources.getSharedScalarM(), scalarMi1));
            Lab1.M_LOCK.unlock();

            // 6. Сигнал задачам T2, T3, T4 про обчислення m.
            Lab1.SEM_5.release(resources.getP() - 1);

            // 7. Чекати сигнал про обчислення m у задачах T2, T3, T4.
            Lab1.SEM_6.acquire();
            Lab1.SEM_7.acquire();
            Lab1.SEM_8.acquire();

            // 8. Копіювання: MD1 = MD.
            Matrix matrixMD1 = resources.getSharedMatrixMD().copy();

            // 9. Копіювання: d1 = d.
            double scalarD1 = resources.getSharedScalarD();

            // 10. Копіювання: m1 = m.
            Lab1.M_LOCK.lock();
            double scalarM1 = resources.getSharedScalarM();
            Lab1.M_LOCK.unlock();

            // 11. Копіювання: p1 = p.
            double scalarP1 = resources.getSharedScalarP();

            // 12. Обчислення 3: MAH = MD1 * MCH * d1 + m1 * MXH * p1.
            for (int i = 0; i < resources.getN(); i++) {
                for (int j = begin; j < end; j++) {
                    double newElement = MatrixUtils.multiplyMatricesCell(matrixMD1, resources.getMatrixMC(), i, j) * scalarD1
                            + scalarM1 * resources.getMatrixMX().getElement(i, j) * scalarP1;
                    resources.getMatrixMA().setElement(i, j, newElement);
                }
            }

            // 13. Чекати сигнал про обчислення MAH у задачах T2, T3, T4.
            Lab1.SEM_9.acquire(resources.getP() - 1);

            // 14. Виведення результату MA.
            System.out.printf("%n%s - Answer MA%n%s%n", getName(), resources.getMatrixMA());
        } catch (InterruptedException ex) {
            System.out.printf("%n%s - Error occurred: %s%n", getName(), ex.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
