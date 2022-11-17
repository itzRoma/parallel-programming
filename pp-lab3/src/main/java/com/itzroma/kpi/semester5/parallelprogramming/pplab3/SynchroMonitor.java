package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SynchroMonitor {
    private static final String ERROR_MESSAGE_FORMAT = "%n%s - An error occurred: %s%n";

    private final Resources resources;

    private int inputCompletedCounter;

    private int scalarRCalculationsCompletedCounter;

    private boolean t1SortingCompleted;
    private boolean t3SortingCompleted;

    private boolean t2MergeCompleted;
    private boolean t4MergeCompleted;

    private int calculationsCompletedCounter;

    public synchronized void signalOthersAboutCompletionOfInput() {
        inputCompletedCounter++;
        if (inputCompletedCounter == resources.getP()) {
            System.out.printf("%n---> All the required data was provided, starting calculation... <---%n");
            notifyAll();
        }
    }

    public synchronized void waitForOthersToCompleteInput() {
        while (inputCompletedCounter != resources.getP()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalOthersAboutCompletionOfCalculationOfScalarR() {
        scalarRCalculationsCompletedCounter++;
        if (scalarRCalculationsCompletedCounter == resources.getP()) {
            notifyAll();
        }
    }

    public synchronized void waitForOthersToCompleteCalculationOfScalarR() {
        while (scalarRCalculationsCompletedCounter != resources.getP()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalT2AboutCompletionOfSortingInT1() {
        t1SortingCompleted = true;
        notifyAll();
    }

    public synchronized void waitForT1ToCompleteSorting() {
        while (!t1SortingCompleted) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalT4AboutCompletionOfMergingInT2() {
        t2MergeCompleted = true;
        notifyAll();
    }

    public synchronized void waitForT2ToCompleteMerging() {
        while (!t2MergeCompleted) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalT4AboutCompletionOfSortingInT3() {
        t3SortingCompleted = true;
        notifyAll();
    }

    public synchronized void waitForT3ToCompleteSorting() {
        while (!t3SortingCompleted) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalOthersAboutCompletionOfMergingInT4() {
        t4MergeCompleted = true;
        notifyAll();
    }

    public synchronized void waitForT4ToCompleteMerging() {
        while (!t4MergeCompleted) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void signalT4AboutCompletionOfCalculations() {
        calculationsCompletedCounter++;
        if (calculationsCompletedCounter == resources.getP() - 1) {
            notifyAll();
        }
    }

    public synchronized void waitForOthersToCompleteCalculations() {
        while (calculationsCompletedCounter != resources.getP() - 1) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.printf(ERROR_MESSAGE_FORMAT, Thread.currentThread().getName(), ex.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}
