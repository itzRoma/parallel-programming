package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.MergeSort;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;

public class T3 extends Thread {
    private final Resources resources;
    private final ResourcesMonitor resourcesMonitor;
    private final SynchroMonitor synchroMonitor;

    private final int start;
    private final int end;

    public T3(Resources resources, ResourcesMonitor resourcesMonitor, SynchroMonitor synchroMonitor) {
        this.resources = resources;
        this.resourcesMonitor = resourcesMonitor;
        this.synchroMonitor = synchroMonitor;
        setName("T3");

        start = (3 - 1) * resources.getH(); // (threadNumber - 1) * H
        end = start + resources.getH();
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        // 1. Введення: ME.
        resources.setMatrixME(MatrixUtils.createMatrixAndFillWithValue(resources.getN(), 1));

        // 2. Сигнал задачам Т1, Т2, Т4 про завершення введення даних.
        synchroMonitor.signalOthersAboutCompletionOfInput();

        // 3. Чекати сигнал від задач Т1, Т2, Т4 про завершення введення даних.
        synchroMonitor.waitForOthersToCompleteInput();

        // 4. Обчислення 1: MAh = ME * MMh.
        for (int i = 0; i < resources.getN(); i++) {
            for (int j = start; j < end; j++) {
                double value = MatrixUtils.multiplyMatricesCell(resources.getMatrixME(), resources.getMatrixMM(), i, j);
                resources.getMatrixMA().setElement(i, j, value);
            }
        }

        // 5. Обчислення 2: Ah = D * MAh.
        for (int i = start; i < end; i++) {
            for (int j = 0; j < resources.getN(); j++) {
                double value = resources.getVectorD().getElement(j) * resources.getMatrixMA().getElement(j, i);
                resources.getVectorA().elements()[i] += value;
            }
        }

        // 6. Обчислення 3: r3 = Bh * Ch.
        double scalarRi3 = 0;
        for (int i = start; i < end; i++) {
            scalarRi3 += resources.getVectorB().getElement(i) * resources.getVectorC().getElement(i);
        }

        // 7. Обчислення 4: r = r + r3.
        resourcesMonitor.addToScalarR(scalarRi3);

        // 8. Сигнал задачам Т1, Т2, Т4 про завершення обчислення r.
        synchroMonitor.signalOthersAboutCompletionOfCalculationOfScalarR();

        // 9. Чекати сигнал від задач Т1, Т2, Т4 про завершення обчислення r.
        synchroMonitor.waitForOthersToCompleteCalculationOfScalarR();

        // 10. Копіювання r3 = r.
        double scalarR3 = resourcesMonitor.copyScalarR();

        // 11. Копіювання x3 = x.
        double scalarX3 = resourcesMonitor.copyScalarX();

        // 12. Обчислення 5: Gh = r3 * Eh * x3.
        for (int i = start; i < end; i++) {
            resources.getVectorG().setElement(i, scalarR3 * resources.getVectorE().getElement(i) * scalarX3);
        }

        // 13. Обчислення 6: Ah = sort(Ah).
        MergeSort.sortDoubles(resources.getVectorA().elements(), start, end);

        // 14. Сигнал задачі Т4 про завершення сортування.
        synchroMonitor.signalT4AboutCompletionOfSortingInT3();

        // 15. Чекати сигнал від задачі Т4 про завершення (повного) злиття.
        synchroMonitor.waitForT4ToCompleteMerging();

        // 16. Обчислення 9: Zh = Ah + Gh.
        for (int i = start; i < end; i++) {
            double value = resources.getVectorA().getElement(i) + resources.getVectorG().getElement(i);
            resources.getVectorZ().setElement(i, value);
        }

        // 17. Сигнал задачі Т4 про завершення обчислень.
        synchroMonitor.signalT4AboutCompletionOfCalculations();

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
