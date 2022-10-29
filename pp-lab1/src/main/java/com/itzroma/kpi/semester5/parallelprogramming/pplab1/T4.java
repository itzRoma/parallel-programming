package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

public class T4 extends Thread {
    private final Resources resources;

    public T4(Resources resources) {
        this.resources = resources;
        setName("T4");
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        try {
            // 1. Введення: Z, MD.
            Lab1.INPUT_LOCK.lock();
            System.out.printf("%n%s - Provide the vector 'Z'%n", getName());
            resources.setZ(VectorUtils.scanVector(resources.getN()));

            System.out.printf("%n%s - Provide the matrix 'MD'%n", getName());
            resources.setSharedMD(MatrixUtils.scanMatrix(resources.getN()));
            Lab1.INPUT_LOCK.unlock();

            // 2. Сигнал задачам Т1, Т2, Т3 про введення Z і MD.
            Lab1.sem4.release(resources.getP() - 1);

            // 3. Чекати сигнал про введення даних у задачах T1, T2, T3.
            Lab1.sem1.acquire();
            Lab1.sem2.acquire();
            Lab1.sem3.acquire();

            // 4. Обчислення 1: m4 = min(ZH).
            double mi4 = resources.findMi(4);

            // 5. Обчислення 2: m = min(m, m4).
            resources.setSharedM(Math.min(resources.getSharedM(), mi4));

            // 6. Сигнал задачам T1, T2, T3 про обчислення m.
            Lab1.sem8.release(resources.getP() - 1);

            // 7. Чекати сигнал про обчислення m у задачах T1, T2, T3.
            Lab1.sem5.acquire();
            Lab1.sem6.acquire();
            Lab1.sem7.acquire();

            // 8. Копіювання: MD4 = MD.
            Lab1.mut1.lock();
            Matrix MD4 = resources.getSharedMD().copy();
            Lab1.mut1.unlock();

            // 9. Копіювання: d4 = d.
            Lab1.mut2.lock();
            double d4 = resources.getSharedD();
            Lab1.mut2.unlock();

            // 10. Копіювання: m4 = m.
            Lab1.mut3.lock();
            double m4 = resources.getSharedM();
            Lab1.mut3.unlock();

            // 11. Копіювання: p4 = p.
            Lab1.mut4.lock();
            double p4 = resources.getSharedP();
            Lab1.mut4.unlock();

            // 12. Обчислення 3: MAH = MD4 * MCH * d4 + m4 * MXH * p4.
            resources.calculateMAH(4, MD4, d4, m4, p4);

            // 13. Сигнал задачі T1 про обчислення MAH.
            Lab1.sem9.release();
        } catch (InterruptedException ex) {
            System.out.printf("%n%s - Error occurred: %s%n", getName(), ex.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
