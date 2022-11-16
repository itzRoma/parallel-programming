package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.MatrixUtils;
import com.itzroma.kpi.semester5.parallelprogramming.pputils.vector.VectorUtils;

public class T4 extends Thread {
    private final Resources resources;

    private final int start;
    private final int end;

    public T4(Resources resources) {
        this.resources = resources;
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
        resources.signalOthersAboutCompletionOfInput();

        // 3. Чекати сигнал від задач Т1, Т2, Т3 про завершення введення даних.
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

        // 7. Копіювання x4 = x.
        double scalarX4 = resources.copyScalarX();

        // 8. Обчислення 4: Gh = Fh * Eh * x4.
        for (int i = start; i < end; i++) {
            double value = resources.getVectorF().getElement(i) * resources.getVectorE().getElement(i) * scalarX4;
            resources.getVectorG().setElement(i, value);
        }

        // 9. Обчислення 5: Ah = sort(Ah).
        resources.sortA(start, end);

        // 10. Чекати сигнал від задачі Т3 про завершення сортування.
        resources.waitForT3ToCompleteSorting();

        // 11. Обчислення 6: A2h = merge(Ah, Ah).
        resources.mergeA((3 - 1) * resources.getH(), end - 1);

        // 12. Чекати сигнал від задачі Т2 про завершення (часткового) злиття.
        resources.waitForT2ToCompleteMerging();

        // 13. Обчислення 7: A4h = merge(A2h, A2h).
        resources.mergeA((1 - 1) * resources.getH(), end - 1);

        // 14. Сигнал задачам Т1, Т2, Т3 про завершення (повного) злиття.
        resources.signalOthersAboutCompletionOfMergingInT4();

        // 15. Обчислення 8: Zh = Ah + Gh.
        for (int i = start; i < end; i++) {
            double value = resources.getVectorA().getElement(i) + resources.getVectorG().getElement(i);
            resources.getVectorZ().setElement(i, value);
        }

        // 16. Чекати сигнал від задач Т1, Т2, Т3 про завершення обчислень.
        resources.waitForOthersToCompleteCalculations();

        // 17. Виведення результату Z.
        System.out.printf("%n%s - Answer Z%n%s%n", getName(), resources.getVectorZ());

        System.out.printf("%nThread '%s' finished%n", getName());
    }
}
