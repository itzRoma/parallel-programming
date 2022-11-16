package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

public class T2 extends Thread {
    private final Resources resources;

    private final int start;
    private final int end;

    public T2(Resources resources) {
        this.resources = resources;
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
        resources.signalOthersAboutCompletionOfInput();

        // 3. Чекати сигнал від задач Т1, Т3, Т4 про завершення введення даних.
        resources.waitForOthersToCompleteInput();

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

        // 6. Обчислення 3: Fh = Bh * Ch.
        for (int i = start; i < end; i++) {
            double value = resources.getVectorB().getElement(i) * resources.getVectorC().getElement(i);
            resources.getVectorF().setElement(i, value);
        }

        // 7. Копіювання x2 = x.
        double scalarX2 = resources.copyScalarX();

        // 8. Обчислення 4: Gh = Fh * Eh * x2.
        for (int i = start; i < end; i++) {
            double value = resources.getVectorF().getElement(i) * resources.getVectorE().getElement(i) * scalarX2;
            resources.getVectorG().setElement(i, value);
        }

        // 9. Обчислення 5: Ah = sort(Ah).
        resources.sortA(start, end);

        // 10. Чекати сигнал від задачі Т1 про завершення сортування.
        resources.waitForT1ToCompleteSorting();

        // 11. Обчислення 6: A2h = merge(Ah, Ah).
        resources.mergeA((1 - 1) * resources.getH(), end - 1);

        // 12. Сигнал задачі Т4 про завершення (часткового) злиття.
        resources.signalT4AboutCompletionOfMergingInT2();

        // 13. Чекати сигнал від задачі Т4 про завершення (повного) злиття.
        resources.waitForT4ToCompleteMerging();

        // 14. Обчислення 8: Zh = Ah + Gh.
        for (int i = start; i < end; i++) {
            double value = resources.getVectorA().getElement(i) + resources.getVectorG().getElement(i);
            resources.getVectorZ().setElement(i, value);
        }

        // 15. Сигнал задачі Т4 про завершення обчислень.
        resources.signalT4AboutCompletionOfCalculations();

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
