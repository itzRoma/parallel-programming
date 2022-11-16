package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import java.util.Scanner;

/**
 * Паралельне програмування: Лабораторна робота №3 (ЛР3)
 * <p>
 * Варіант: 30
 * <p>
 * Функція: Z = sort(D * (ME * MM)) + (B * C) * E * x
 * <p>
 * Автор: Бондаренко Роман Ігорович, група ІО-03
 * <p>
 * Дата: 16/11/2022
 */
public class Lab3 {
    private static final int AMOUNT_OF_PROCESSORS = 4;

    public static void main(String[] args) {
        System.out.printf("%nProvide N (the size of vectors and matrices): ");
        int n = new Scanner(System.in).nextInt();

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
            System.out.printf("%n%s - An error occurred: %s%n", Thread.currentThread().getName(), ex.getMessage());
            Thread.currentThread().interrupt();
        }

        long end = System.currentTimeMillis();

        System.out.printf("%nAll threads finished, execution time is %d ms%n", end - start);
    }
}
