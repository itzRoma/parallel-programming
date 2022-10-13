package com.itzroma.kpi.semester5.parallelprogramming.pplab0;

/* ---------------------------------------------------
-- Паралельне програмування                         --
--                                                  --
-- Лабораторна робота ЛР0                           --
--                                                  --
-- Функції:                                         --
-- F1 (1.4)  ->  C = A + SORT(B) * (MA * ME)        --
-- F2 (2.10) ->  MK = MA * (MG * MZ) + TRANS(ML)    --
-- F3 (3.4)  ->  O = SORT(P) * SORT(MR * MS)        --
--                                                  --
-- Виконав: Бондаренко Роман Ігорович, група ІО-03  --
-- Дата: 14/09/2022 - 15/09/2022                    --
--------------------------------------------------- */
public class Lab0 {
    public static void main(String[] args) {
        System.out.print("Enter the size of the matrices and vectors: ");
        final int N = new java.util.Scanner(System.in).nextInt();

        Thread T1 = new T1("thread-1", 10, N);
        Thread T2 = new T2("thread-2", 9, N);
        Thread T3 = new T3("thread-3", 8, N);

        System.out.printf("%nStarting threads...%n");

        T1.start();
        T2.start();
        T3.start();

        try {
            T1.join();
            T2.join();
            T3.join();
        } catch (InterruptedException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }

        System.out.printf("%nAll threads finished%n");
    }
}
