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
    public static final Lock mut1 = new ReentrantLock();
    public static final Lock mut2 = new ReentrantLock();
    public static final Lock mut3 = new ReentrantLock();
    public static final Lock mut4 = new ReentrantLock();

    public static final Lock INPUT_LOCK = new ReentrantLock();

    public static final Semaphore sem1 = new Semaphore(0);
    public static final Semaphore sem2 = new Semaphore(0);
    public static final Semaphore sem3 = new Semaphore(0);
    public static final Semaphore sem4 = new Semaphore(0);
    public static final Semaphore sem5 = new Semaphore(0);
    public static final Semaphore sem6 = new Semaphore(0);
    public static final Semaphore sem7 = new Semaphore(0);
    public static final Semaphore sem8 = new Semaphore(0);
    public static final Semaphore sem9 = new Semaphore(0);

    public static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter the size of the matrices and vectors: ");
        final int N = sc.nextInt();

        Resources resources = new Resources(N, 4);

        Thread T1 = new T1(resources);
        Thread T2 = new T2(resources);
        Thread T3 = new T3(resources);
        Thread T4 = new T4(resources);

        System.out.printf("%nStarting threads...%n");

        T1.start();
        T2.start();
        T3.start();
        T4.start();

        try {
            T1.join();
            T2.join();
            T3.join();
            T4.join();
        } catch (InterruptedException ex) {
            System.out.printf("%nAn error occurred: %s%n", ex.getMessage());
        }

        System.out.printf("%nAll threads finished%n");
    }
}
