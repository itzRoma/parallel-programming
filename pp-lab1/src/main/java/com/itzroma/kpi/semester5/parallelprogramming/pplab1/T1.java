package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;

import java.util.concurrent.BrokenBarrierException;

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
            resources.setScalarD(1.0);

            // 2. Очікувати на закінчення введення даних у інших задачах.
            Lab1.B.await();

            // 3. Обчислення 1: m1 = min(ZH).
            double scalarMi1 = resources.getVectorZ().getElement(begin);
            for (int i = begin; i < end; i++) {
                scalarMi1 = Math.min(resources.getVectorZ().getElement(i), scalarMi1);
            }

            // 4. Обчислення 2: m = min(m, m1).
            Lab1.S01.acquire();
            resources.getScalarM().compareMinAndSet(scalarMi1);
            Lab1.S01.release();

            // 5. Сигнал задачам T2, T3, T4 про обчислення m.
            Lab1.S1.release(resources.getP() - 1);

            // 6. Чекати сигнал про обчислення m у задачах T2, T3, T4.
            Lab1.S2.acquire();
            Lab1.S3.acquire();
            Lab1.S4.acquire();

            // 7. Копіювання: d1 = d.
            Lab1.CS2.lock();
            double scalarD1 = resources.getScalarD();
            Lab1.CS2.unlock();

            // 8. Копіювання: m1 = m.
            Lab1.S01.acquire();
            double scalarM1 = resources.getScalarM().get();
            Lab1.S01.release();

            // 9. Копіювання: p1 = p.
            Lab1.S02.acquire();
            double scalarP1 = resources.getScalarP();
            Lab1.S02.release();

            // 10. Обчислення 3: MAH = MD * MCH * d1 + m1 * MXH * p1.
            for (int i = 0; i < resources.getN(); i++) {
                for (int j = begin; j < end; j++) {
                    double newElement = MatrixUtils.multiplyMatricesCell(resources.getMatrixMD(), resources.getMatrixMC(), i, j) * scalarD1
                            + scalarM1 * resources.getMatrixMX().getElement(i, j) * scalarP1;
                    resources.getMatrixMA().setElement(i, j, newElement);
                }
            }

            // 11. Чекати сигнал про обчислення MAH у задачах T2, T3, T4.
            Lab1.S5.acquire(resources.getP() - 1);

            // 12. Виведення результату MA.
            System.out.printf("%n%s - Answer MA%n%s%n", getName(), resources.getMatrixMA());
        } catch (InterruptedException ex) {
            System.out.printf("%n%s - Error occurred: %s%n", getName(), ex.getMessage());
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException ex) {
            System.out.printf("%n%s - Error occurred: %s%n", getName(), ex.getMessage());
        }

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
