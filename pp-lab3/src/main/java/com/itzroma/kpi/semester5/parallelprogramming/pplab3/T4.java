package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.MergeSort;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

public class T4 extends Thread {
    private final Resources resources;
    private final ResourcesMonitor resourcesMonitor;
    private final SynchroMonitor synchroMonitor;

    private final int start;
    private final int end;

    public T4(Resources resources, ResourcesMonitor resourcesMonitor, SynchroMonitor synchroMonitor) {
        this.resources = resources;
        this.resourcesMonitor = resourcesMonitor;
        this.synchroMonitor = synchroMonitor;
        setName("T4");

        start = (4 - 1) * resources.getH(); // (threadNumber - 1) * H
        end = start + resources.getH();
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        // 1. Введення: C, x.
        resources.setVectorC(VectorUtils.createVectorAndFillWithValue(resources.getN(), 1));
        resources.setScalarX(1);

        // 2. Сигнал задачам Т1, Т2, Т3 про завершення введення даних.
        synchroMonitor.signalOthersAboutCompletionOfInput();

        // 3. Чекати сигнал від задач Т1, Т2, Т3 про завершення введення даних.
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

        // 6. Обчислення 3: r4 = Bh * Ch.
        double scalarRi4 = 0;
        for (int i = start; i < end; i++) {
            scalarRi4 += resources.getVectorB().getElement(i) * resources.getVectorC().getElement(i);
        }

        // 7. Обчислення 4: r = r + r4.
        resourcesMonitor.addToScalarR(scalarRi4);

        // 8. Сигнал задачам Т1, Т2, Т3 про завершення обчислення r.
        synchroMonitor.signalOthersAboutCompletionOfCalculationOfScalarR();

        // 9. Чекати сигнал від задач Т1, Т2, Т3 про завершення обчислення r.
        synchroMonitor.waitForOthersToCompleteCalculationOfScalarR();

        // 10. Копіювання r4 = r.
        double scalarR4 = resourcesMonitor.copyScalarR();

        // 11. Копіювання x4 = x.
        double scalarX4 = resourcesMonitor.copyScalarX();

        // 12. Обчислення 5: Gh = r4 * Eh * x4.
        for (int i = start; i < end; i++) {
            resources.getVectorG().setElement(i, scalarR4 * resources.getVectorE().getElement(i) * scalarX4);
        }

        // 13. Обчислення 6: Ah = sort(Ah).
        MergeSort.sortDoubles(resources.getVectorA().elements(), start, end);

        // 14. Чекати сигнал від задачі Т3 про завершення сортування.
        synchroMonitor.waitForT3ToCompleteSorting();

        // 15. Обчислення 7: A2h = merge(Ah, Ah).
        MergeSort.mergeDoubles(resources.getVectorA().elements(), (3 - 1) * resources.getH(), end - 1);

        // 16. Чекати сигнал від задачі Т2 про завершення (часткового) злиття.
        synchroMonitor.waitForT2ToCompleteMerging();

        // 17. Обчислення 8: A4h = merge(A2h, A2h).
        MergeSort.mergeDoubles(resources.getVectorA().elements(), (1 - 1) * resources.getH(), end - 1);

        // 18. Сигнал задачам Т1, Т2, Т3 про завершення (повного) злиття.
        synchroMonitor.signalOthersAboutCompletionOfMergingInT4();

        // 19. Обчислення 9: Zh = Ah + Gh.
        for (int i = start; i < end; i++) {
            double value = resources.getVectorA().getElement(i) + resources.getVectorG().getElement(i);
            resources.getVectorZ().setElement(i, value);
        }

        // 20. Чекати сигнал від задач Т1, Т2, Т3 про завершення обчислень.
        synchroMonitor.waitForOthersToCompleteCalculations();

        // 21. Виведення результату Z.
        System.out.printf("%n%s - Answer Z%n%s%n", getName(), resources.getVectorZ());

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
