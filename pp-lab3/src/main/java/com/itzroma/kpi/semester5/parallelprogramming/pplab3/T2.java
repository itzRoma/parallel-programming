package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.MergeSort;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

public class T2 extends Thread {
    private final Resources resources;
    private final ResourcesMonitor resourcesMonitor;
    private final SynchroMonitor synchroMonitor;

    private final int start;
    private final int end;

    public T2(Resources resources, ResourcesMonitor resourcesMonitor, SynchroMonitor synchroMonitor) {
        this.resources = resources;
        this.resourcesMonitor = resourcesMonitor;
        this.synchroMonitor = synchroMonitor;
        setName("T2");

        start = (2 - 1) * resources.getH(); // (threadNumber - 1) * H
        end = start + resources.getH();
    }

    @Override
    public void run() {
        System.out.printf("%nThread '%s' started%n", getName());

        // 1. Введення: D, MM.
        resources.setVectorD(VectorUtils.createVectorAndFillWithValue(resources.getN(), 1));
        resources.setMatrixMM(MatrixUtils.createMatrixAndFillWithValue(resources.getN(), 1));

        // 2. Сигнал задачам Т1, Т3, Т4 про завершення введення даних.
        synchroMonitor.signalOthersAboutCompletionOfInput();

        // 3. Чекати сигнал від задач Т1, Т3, Т4 про завершення введення даних.
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

        // 6. Обчислення 3: r2 = Bh * Ch.
        double scalarRi2 = 0;
        for (int i = start; i < end; i++) {
            scalarRi2 += resources.getVectorB().getElement(i) * resources.getVectorC().getElement(i);
        }

        // 7. Обчислення 4: r = r + r2.
        resourcesMonitor.addToScalarR(scalarRi2);

        // 8. Сигнал задачам Т1, Т3, Т4 про завершення обчислення r.
        synchroMonitor.signalOthersAboutCompletionOfCalculationOfScalarR();

        // 9. Чекати сигнал від задач Т1, Т3, Т4 про завершення обчислення r.
        synchroMonitor.waitForOthersToCompleteCalculationOfScalarR();

        // 10. Копіювання r2 = r.
        double scalarR2 = resourcesMonitor.copyScalarR();

        // 11. Копіювання x2 = x.
        double scalarX2 = resourcesMonitor.copyScalarX();

        // 12. Обчислення 5: Gh = r2 * Eh * x2.
        for (int i = start; i < end; i++) {
            resources.getVectorG().setElement(i, scalarR2 * resources.getVectorE().getElement(i) * scalarX2);
        }

        // 13. Обчислення 6: Ah = sort(Ah).
        MergeSort.sortDoubles(resources.getVectorA().elements(), start, end);

        // 14. Чекати сигнал від задачі Т1 про завершення сортування.
        synchroMonitor.waitForT1ToCompleteSorting();

        // 15. Обчислення 7: A2h = merge(Ah, Ah).
        MergeSort.mergeDoubles(resources.getVectorA().elements(), (1 - 1) * resources.getH(), end - 1);

        // 16. Сигнал задачі Т4 про завершення (часткового) злиття.
        synchroMonitor.signalT4AboutCompletionOfMergingInT2();

        // 17. Чекати сигнал від задачі Т4 про завершення (повного) злиття.
        synchroMonitor.waitForT4ToCompleteMerging();

        // 18. Обчислення 9: Zh = Ah + Gh.
        for (int i = start; i < end; i++) {
            double value = resources.getVectorA().getElement(i) + resources.getVectorG().getElement(i);
            resources.getVectorZ().setElement(i, value);
        }

        // 19. Сигнал задачі Т4 про завершення обчислень.
        synchroMonitor.signalT4AboutCompletionOfCalculations();

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
