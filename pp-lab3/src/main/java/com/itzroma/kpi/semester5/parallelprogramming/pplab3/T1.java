package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.MergeSort;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

public class T1 extends Thread {
    private final Resources resources;
    private final ResourcesMonitor resourcesMonitor;
    private final SynchroMonitor synchroMonitor;

    private final int start;
    private final int end;

    public T1(Resources resources, ResourcesMonitor resourcesMonitor, SynchroMonitor synchroMonitor) {
        this.resources = resources;
        this.resourcesMonitor = resourcesMonitor;
        this.synchroMonitor = synchroMonitor;
        setName("T1");

        start = (1 - 1) * resources.getH(); // (threadNumber - 1) * H
        end = start + resources.getH();
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        // 1. Введення: B, E.
        resources.setVectorB(VectorUtils.createVectorAndFillWithValue(resources.getN(), 1));
        resources.setVectorE(VectorUtils.createVectorAndFillWithValue(resources.getN(), 1));

        // 2. Сигнал задачам Т2, Т3, Т4 про завершення введення даних.
        synchroMonitor.signalOthersAboutCompletionOfInput();

        // 3. Чекати сигнал від задач Т2, Т3, Т4 про завершення введення даних.
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

        // 6. Обчислення 3: r1 = Bh * Ch.
        double scalarRi1 = 0;
        for (int i = start; i < end; i++) {
            scalarRi1 += resources.getVectorB().getElement(i) * resources.getVectorC().getElement(i);
        }

        // 7. Обчислення 4: r = r + r1.
        resourcesMonitor.addToScalarR(scalarRi1);

        // 8. Сигнал задачам Т2, Т3, Т4 про завершення обчислення r.
        synchroMonitor.signalOthersAboutCompletionOfCalculationOfScalarR();

        // 9. Чекати сигнал від задач Т2, Т3, Т4 про завершення обчислення r.
        synchroMonitor.waitForOthersToCompleteCalculationOfScalarR();

        // 10. Копіювання r1 = r.
        double scalarR1 = resourcesMonitor.copyScalarR();

        // 11. Копіювання x1 = x.
        double scalarX1 = resourcesMonitor.copyScalarX();

        // 12. Обчислення 5: Gh = r1 * Eh * x1.
        for (int i = start; i < end; i++) {
            resources.getVectorG().setElement(i, scalarR1 * resources.getVectorE().getElement(i) * scalarX1);
        }

        // 13. Обчислення 6: Ah = sort(Ah).
        MergeSort.sortDoubles(resources.getVectorA().elements(), start, end);

        // 14. Сигнал задачі Т2 про завершення сортування.
        synchroMonitor.signalT2AboutCompletionOfSortingInT1();

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
