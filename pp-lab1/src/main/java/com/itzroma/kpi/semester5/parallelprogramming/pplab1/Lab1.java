package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Паралельне програмування - Лабораторна робота №1 (ЛР1)
 * Функція - F (5) -> MA = MD * MC * d + min(Z) * MX * p
 *
 * @author Бондаренко Роман Ігорович, група ІО-03
 * @Date: 28/10/2022
 */
public class Lab1 {
    public static final Lock INPUT_LOCK = new ReentrantLock();
    public static final Lock M_LOCK = new ReentrantLock();

    public static final Semaphore SEM_1 = new Semaphore(0);
    public static final Semaphore SEM_2 = new Semaphore(0);
    public static final Semaphore SEM_3 = new Semaphore(0);
    public static final Semaphore SEM_4 = new Semaphore(0);
    public static final Semaphore SEM_5 = new Semaphore(0);
    public static final Semaphore SEM_6 = new Semaphore(0);
    public static final Semaphore SEM_7 = new Semaphore(0);
    public static final Semaphore SEM_8 = new Semaphore(0);
    public static final Semaphore SEM_9 = new Semaphore(0);

    public static final Scanner SC = new Scanner(System.in);

    private static final int AMOUNT_OF_PROCESSORS = 4;

    public static void main(String[] args) {
        int n;
        do {
            System.out.printf("%nProvide the size of vectors and matrices: ");
            n = SC.nextInt();
        } while (!checkN(n));

        Resources resources = new Resources(n, AMOUNT_OF_PROCESSORS);

        Thread t1 = new T1(resources);
        Thread t2 = new T2(resources);
        Thread t3 = new T3(resources);
        Thread t4 = new T4(resources);

        System.out.printf("%nStarting threads...%n");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException ex) {
            System.out.printf("%nAn error occurred: %s%n", ex.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.printf("%nAll threads finished%n");
    }

    private static boolean checkN(int n) {
        if (n % AMOUNT_OF_PROCESSORS != 0) {
            System.out.printf("N should be exactly divisible by %d%n", AMOUNT_OF_PROCESSORS);
            return false;
        }
        return true;
    }
}
