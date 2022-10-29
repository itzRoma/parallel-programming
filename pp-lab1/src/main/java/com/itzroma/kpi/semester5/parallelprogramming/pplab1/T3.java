package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;

public class T3 extends Thread {
    private final Resources resources;

    public T3(Resources resources) {
        this.resources = resources;
        setName("T3");
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        try {
            // 1. Введення: MC, p.
            Lab1.INPUT_LOCK.lock();
            System.out.printf("%n%s - Provide the matrix 'MC'%n", getName());
            resources.setMC(MatrixUtils.scanMatrix(resources.getN()));

            System.out.printf("%n%s - Provide the scalar 'p': ", getName());
            resources.setSharedP(Lab1.sc.nextDouble());
            Lab1.INPUT_LOCK.unlock();

            // 2. Сигнал задачам Т1, Т2, Т4 про введення MC і p.
            Lab1.sem3.release(resources.getP() - 1);

            // 3. Чекати сигнал про введення даних у задачах T1, T2, T4.
            Lab1.sem1.acquire();
            Lab1.sem2.acquire();
            Lab1.sem4.acquire();

            // 4. Обчислення 1: m3 = min(ZH).
            double mi3 = resources.findMi(3);

            // 5. Обчислення 2: m = min(m, m3).
            resources.setSharedM(Math.min(resources.getSharedM(), mi3));

            // 6. Сигнал задачам T1, T2, T4 про обчислення m.
            Lab1.sem7.release(resources.getP() - 1);

            // 7. Чекати сигнал про обчислення m у задачах T1, T2, T4.
            Lab1.sem5.acquire();
            Lab1.sem6.acquire();
            Lab1.sem8.acquire();

            // 8. Копіювання: MD3 = MD.
            Lab1.mut1.lock();
            Matrix MD3 = resources.getSharedMD().copy();
            Lab1.mut1.unlock();

            // 9. Копіювання: d3 = d.
            Lab1.mut2.lock();
            double d3 = resources.getSharedD();
            Lab1.mut2.unlock();

            // 10. Копіювання: m3 = m.
            Lab1.mut3.lock();
            double m3 = resources.getSharedM();
            Lab1.mut3.unlock();

            // 11. Копіювання: p3 = p.
            Lab1.mut4.lock();
            double p3 = resources.getSharedP();
            Lab1.mut4.unlock();

            // 12. Обчислення 3: MAH = MD3 * MCH * d3 + m3 * MXH * p3.
            resources.calculateMAH(3, MD3, d3, m3, p3);

            // 13. Сигнал задачі T1 про обчислення MAH.
            Lab1.sem9.release();
        } catch (InterruptedException ex) {
            System.out.printf("%n%s - Error occurred: %s%n", getName(), ex.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
