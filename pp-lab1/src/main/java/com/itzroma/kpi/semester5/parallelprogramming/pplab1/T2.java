package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;

import java.util.concurrent.BrokenBarrierException;

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
            Lab1.CS_INPUT.lock();
            System.out.printf("%n%s - Provide the matrix 'MX'%n", getName());
            resources.setMatrixMX(MatrixUtils.scanMatrix(resources.getN()));
            Lab1.CS_INPUT.unlock();

            // 2. Очікувати на закінчення введення даних у інших задачах.
            Lab1.B.await();

            // 3. Обчислення 1: m2 = min(ZH).
            double scalarMi2 = resources.getVectorZ().getElement(begin);
            for (int i = begin; i < end; i++) {
                scalarMi2 = Math.min(resources.getVectorZ().getElement(i), scalarMi2);
            }

            // 4. Обчислення 2: m = min(m, m2).
            Lab1.S01.acquire();
            resources.getScalarM().compareMinAndSet(scalarMi2);
            Lab1.S01.release();

            // 5. Сигнал задачам T1, T3, T4 про обчислення m.
            Lab1.S2.release(resources.getP() - 1);

            // 6. Чекати сигнал про обчислення m у задачах T1, T3, T4.
            Lab1.S1.acquire();
            Lab1.S3.acquire();
            Lab1.S4.acquire();

            // 7. Копіювання: d2 = d.
            Lab1.CS2.lock();
            double scalarD2 = resources.getScalarD();
            Lab1.CS2.unlock();

            // 8. Копіювання: m2 = m.
            Lab1.S01.acquire();
            double scalarM2 = resources.getScalarM().get();
            Lab1.S01.release();

            // 9. Копіювання: p2 = p.
            Lab1.S02.acquire();
            double scalarP2 = resources.getScalarP();
            Lab1.S02.release();

            // 10. Обчислення 3: MAH = MD * MCH * d2 + m2 * MXH * p2.
            for (int i = 0; i < resources.getN(); i++) {
                for (int j = begin; j < end; j++) {
                    double newElement = MatrixUtils.multiplyMatricesCell(resources.getMatrixMD(), resources.getMatrixMC(), i, j) * scalarD2
                            + scalarM2 * resources.getMatrixMX().getElement(i, j) * scalarP2;
                    resources.getMatrixMA().setElement(i, j, newElement);
                }
            }

            // 11. Сигнал задачі T1 про обчислення MAH.
            Lab1.S5.release();
        } catch (InterruptedException ex) {
            System.out.printf("%n%s - Error occurred: %s%n", getName(), ex.getMessage());
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException ex) {
            System.out.printf("%n%s - Error occurred: %s%n", getName(), ex.getMessage());
        }

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
