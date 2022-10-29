package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;

public class T2 extends Thread {
    private final Resources resources;

    public T2(Resources resources) {
        this.resources = resources;
        setName("T2");
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        try {
            // 1. Введення: MX.
            Lab1.INPUT_LOCK.lock();
            System.out.printf("%n%s - Provide the matrix 'MX'%n", getName());
            resources.setMX(MatrixUtils.scanMatrix(resources.getN()));
            Lab1.INPUT_LOCK.unlock();

            // 2. Сигнал задачам Т1, Т3, Т4 про введення MX.
            Lab1.sem2.release(resources.getP() - 1);

            // 3. Чекати сигнал про введення даних у задачах T1, T3, T4.
            Lab1.sem1.acquire();
            Lab1.sem3.acquire();
            Lab1.sem4.acquire();

            // 4. Обчислення 1: m2 = min(ZH).
            double mi2 = resources.findMi(2);

            // 5. Обчислення 2: m = min(m, m2).
            resources.setSharedM(Math.min(resources.getSharedM(), mi2));

            // 6. Сигнал задачам T1, T3, T4 про обчислення m.
            Lab1.sem6.release(resources.getP() - 1);

            // 7. Чекати сигнал про обчислення m у задачах T1, T3, T4.
            Lab1.sem5.acquire();
            Lab1.sem7.acquire();
            Lab1.sem8.acquire();

            // 8. Копіювання: MD2 = MD.
            Lab1.mut1.lock();
            Matrix MD2 = resources.getSharedMD().copy();
            Lab1.mut1.unlock();

            // 9. Копіювання: d2 = d.
            Lab1.mut2.lock();
            double d2 = resources.getSharedD();
            Lab1.mut2.unlock();

            // 10. Копіювання: m2 = m.
            Lab1.mut3.lock();
            double m2 = resources.getSharedM();
            Lab1.mut3.unlock();

            // 11. Копіювання: p2 = p.
            Lab1.mut4.lock();
            double p2 = resources.getSharedP();
            Lab1.mut4.unlock();

            // 12. Обчислення 3: MAH = MD2 * MCH * d2 + m2 * MXH * p2.
            resources.calculateMAH(2, MD2, d2, m2, p2);

            // 13. Сигнал задачі T1 про обчислення MAH.
            Lab1.sem9.release();
        } catch (InterruptedException ex) {
            System.out.printf("%n%s - Error occurred: %s%n", getName(), ex.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
