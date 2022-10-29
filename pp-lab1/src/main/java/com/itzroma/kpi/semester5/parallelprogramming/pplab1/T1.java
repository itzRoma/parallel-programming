package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;

public class T1 extends Thread {
    private final Resources resources;

    public T1(Resources resources) {
        this.resources = resources;
        setName("T1");
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        try {
            // 1. Введення: d.
            Lab1.INPUT_LOCK.lock();
            System.out.printf("%n%s - Provide the scalar 'd': ", getName());
            resources.setSharedD(Lab1.sc.nextDouble());
            Lab1.INPUT_LOCK.unlock();

            // 2. Сигнал задачам Т2, Т3, Т4 про введення d.
            Lab1.sem1.release(resources.getP() - 1);

            // 3. Чекати сигнал про введення даних у задачах T2, T3, T4.
            Lab1.sem2.acquire();
            Lab1.sem3.acquire();
            Lab1.sem4.acquire();

            // 4. Обчислення 1: m1 = min(ZH).
            double mi1 = resources.findMi(1);

            // 5. Обчислення 2: m = min(m, m1).
            resources.setSharedM(Math.min(resources.getSharedM(), mi1));

            // 6. Сигнал задачам T2, T3, T4 про обчислення m.
            Lab1.sem5.release(resources.getP() - 1);

            // 7. Чекати сигнал про обчислення m у задачах T2, T3, T4.
            Lab1.sem6.acquire();
            Lab1.sem7.acquire();
            Lab1.sem8.acquire();

            // 8. Копіювання: MD1 = MD.
            Lab1.mut1.lock();
            Matrix MD1 = resources.getSharedMD().copy();
            Lab1.mut1.unlock();

            // 9. Копіювання: d1 = d.
            Lab1.mut2.lock();
            double d1 = resources.getSharedD();
            Lab1.mut2.unlock();

            // 10. Копіювання: m1 = m.
            Lab1.mut3.lock();
            double m1 = resources.getSharedM();
            Lab1.mut3.unlock();

            // 11. Копіювання: p1 = p.
            Lab1.mut4.lock();
            double p1 = resources.getSharedP();
            Lab1.mut4.unlock();

            // 12. Обчислення 3: MAH = MD1 * MCH * d1 + m1 * MXH * p1.
            resources.calculateMAH(1, MD1, d1, m1, p1);

            // 13. Чекати сигнал про обчислення MAH у задачах T2, T3, T4.
            Lab1.sem9.acquire(resources.getP() - 1);

            // 14. Виведення результату MA.
            System.out.printf("%n%s - Answer MA%n%s%n", getName(), resources.getMA());
        } catch (InterruptedException ex) {
            System.out.printf("%n%s - Error occurred: %s%n", getName(), ex.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
