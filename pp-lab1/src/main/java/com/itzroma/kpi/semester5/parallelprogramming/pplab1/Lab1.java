package com.itzroma.kpi.semester5.parallelprogramming.pplab1;

import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Паралельне програмування: Лабораторна робота №1 (ЛР1)
 * <p>
 * Варіант: 5
 * <p>
 * Функція: MA = MD * MC * d + min(Z) * MX * p
 * <p>
 * Автор: Бондаренко Роман Ігорович, група ІО-03
 * <p>
 * Дата: 03/11/2022
 */
public class Lab1 {
    private static final int AMOUNT_OF_PROCESSORS = 4;

    /**
     * Barrier for controlling input from all processors (threads)
     */
    public static final CyclicBarrier B = new CyclicBarrier(
            AMOUNT_OF_PROCESSORS,
            () -> System.out.printf("%n---> All the required data was provided, calculation started <---%n")
    );

    /**
     * Critical section for controlling access to the shared resource 'd'
     */
    public static final Lock CS2 = new ReentrantLock();

    /**
     * Semaphore for controlling access to the shared resource 'm'
     */
    public static final Semaphore S01 = new Semaphore(1);

    /**
     * Semaphore for controlling access to the shared resource 'p'
     */
    public static final Semaphore S02 = new Semaphore(1);

    /**
     * Semaphore to synchronize T2, T3, T4 with the completion of the calculation of 'm' in T1
     */
    public static final Semaphore S1 = new Semaphore(0);

    /**
     * Semaphore to synchronize T1, T3, T4 with the completion of the calculation of 'm' in T2
     */
    public static final Semaphore S2 = new Semaphore(0);

    /**
     * Semaphore to synchronize T1, T2, T4 with the completion of the calculation of 'm' in T3
     */
    public static final Semaphore S3 = new Semaphore(0);

    /**
     * Semaphore to synchronize T1, T2, T3 with the completion of the calculation of 'm' in T4
     */
    public static final Semaphore S4 = new Semaphore(0);

    /**
     * Semaphore to synchronize T1 with completion of 'MAH' calculation in T2, T3, T4
     */
    public static final Semaphore S5 = new Semaphore(0);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.printf("%nProvide N (the size of vectors and matrices): ");
        int n = scanner.nextInt();

        Resources resources = new Resources(n, AMOUNT_OF_PROCESSORS);

        Thread t1 = new T1(resources);
        Thread t2 = new T2(resources);
        Thread t3 = new T3(resources);
        Thread t4 = new T4(resources);

        System.out.printf("%nStarting threads...%n");

        long start = System.currentTimeMillis();

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
        } finally {
            scanner.close();
        }

        long end = System.currentTimeMillis();

        System.out.printf("%nAll threads finished, execution time is %d ms%n", end - start);
    }
}
