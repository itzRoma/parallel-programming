package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

import java.util.concurrent.BrokenBarrierException;

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
            Lab1.CS_INPUT.lock();
            System.out.printf("%n%s - Provide the vector 'Z'%n", getName());
            resources.setVectorZ(VectorUtils.scanVector(resources.getN()));

            System.out.printf("%n%s - Provide the matrix 'MD'%n", getName());
            resources.setMatrixMD(MatrixUtils.scanMatrix(resources.getN()));
            Lab1.CS_INPUT.unlock();

            // 2. Очікувати на закінчення введення даних у інших задачах.
            Lab1.B.await();

            // 3. Обчислення 1: m4 = min(ZH).
            double scalarMi4 = resources.getVectorZ().getElement(begin);
            for (int i = begin; i < end; i++) {
                scalarMi4 = Math.min(resources.getVectorZ().getElement(i), scalarMi4);
            }

            // 4. Обчислення 2: m = min(m, m4).
            Lab1.S01.acquire();
            resources.getScalarM().compareMinAndSet(scalarMi4);
            Lab1.S01.release();

            // 5. Сигнал задачам T1, T2, T3 про обчислення m.
            Lab1.S4.release(resources.getP() - 1);

            // 6. Чекати сигнал про обчислення m у задачах T1, T2, T3.
            Lab1.S1.acquire();
            Lab1.S2.acquire();
            Lab1.S3.acquire();

            // 7. Копіювання: d4 = d.
            Lab1.CS2.lock();
            double scalarD4 = resources.getScalarD();
            Lab1.CS2.unlock();

            // 8. Копіювання: m4 = m.
            Lab1.S01.acquire();
            double scalarM4 = resources.getScalarM().get();
            Lab1.S01.release();

            // 9. Копіювання: p4 = p.
            Lab1.S02.acquire();
            double scalarP4 = resources.getScalarP();
            Lab1.S02.release();

            // 10. Обчислення 3: MAH = MD * MCH * d4 + m4 * MXH * p4.
            for (int i = 0; i < resources.getN(); i++) {
                for (int j = begin; j < end; j++) {
                    double newElement = MatrixUtils.multiplyMatricesCell(resources.getMatrixMD(), resources.getMatrixMC(), i, j) * scalarD4
                            + scalarM4 * resources.getMatrixMX().getElement(i, j) * scalarP4;
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
