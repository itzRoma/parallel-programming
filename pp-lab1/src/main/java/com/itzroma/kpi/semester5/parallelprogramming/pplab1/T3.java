package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;

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
            double[][] elementsMC = new double[resources.getN()][resources.getN()];
            Arrays.stream(elementsMC).forEach(row -> Arrays.fill(row, 1.0));
            resources.setMatrixMC(new Matrix(elementsMC));

            resources.setScalarP(1.0);

            // 2. Очікувати на закінчення введення даних у інших задачах.
            Lab1.B.await();

            // 3. Обчислення 1: m3 = min(ZH).
            double scalarMi3 = resources.getVectorZ().getElement(begin);
            for (int i = begin; i < end; i++) {
                scalarMi3 = Math.min(resources.getVectorZ().getElement(i), scalarMi3);
            }

            // 4. Обчислення 2: m = min(m, m3).
            Lab1.S01.acquire();
            resources.getScalarM().compareMinAndSet(scalarMi3);
            Lab1.S01.release();

            // 5. Сигнал задачам T1, T2, T4 про обчислення m.
            Lab1.S3.release(resources.getP() - 1);

            // 6. Чекати сигнал про обчислення m у задачах T1, T2, T4.
            Lab1.S1.acquire();
            Lab1.S2.acquire();
            Lab1.S4.acquire();

            // 7. Копіювання: d3 = d.
            Lab1.CS2.lock();
            double scalarD3 = resources.getScalarD();
            Lab1.CS2.unlock();

            // 8. Копіювання: m3 = m.
            Lab1.S01.acquire();
            double scalarM3 = resources.getScalarM().get();
            Lab1.S01.release();

            // 9. Копіювання: p3 = p.
            Lab1.S02.acquire();
            double scalarP3 = resources.getScalarP();
            Lab1.S02.release();

            // 10. Обчислення 3: MAH = MD * MCH * d3 + m3 * MXH * p3.
            for (int i = 0; i < resources.getN(); i++) {
                for (int j = begin; j < end; j++) {
                    double newElement = MatrixUtils.multiplyMatricesCell(resources.getMatrixMD(), resources.getMatrixMC(), i, j) * scalarD3
                            + scalarM3 * resources.getMatrixMX().getElement(i, j) * scalarP3;
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
