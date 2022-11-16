package com.itzroma.kpi.semester5.parallelprogramming.pputils.vector;

import com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix.Matrix;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class VectorUtils {
    private static final Scanner SCANNER;

    static {
        SCANNER = new Scanner(System.in);
    }

    private VectorUtils() {
    }

    public static Vector scanVector(int vectorSize) {
        if (vectorSize > 0 && vectorSize <= 4) return scanVectorFromUser(vectorSize);
        if (vectorSize > 4) return chooseFillOption(vectorSize);
        throw new IllegalArgumentException("Invalid vector size provided");
    }

    private static Vector chooseFillOption(int vectorSize) {
        System.out.print("Choose the vector filling option (1 - value, 2 - random): ");
        switch (SCANNER.nextInt()) {
            case 1 -> {
                return createVectorAndFillWithValue(vectorSize);
            }
            case 2 -> {
                return createVectorAndFillWithRandomValue(vectorSize);
            }
            default -> throw new IllegalArgumentException("Invalid vector filling option provided");
        }
    }

    private static Vector scanVectorFromUser(int vectorSize) {
        double[] vector = new double[vectorSize];
        for (int i = 0; i < vectorSize; i++) {
            System.out.printf("Element %d: ", i + 1);
            vector[i] = SCANNER.nextDouble();
        }
        return new Vector(vector);
    }

    public static Vector createVectorAndFillWithValue(int vectorSize, double value) {
        double[] elements = new double[vectorSize];
        Arrays.fill(elements, value);
        return new Vector(elements);
    }

    private static Vector createVectorAndFillWithValue(int vectorSize) {
        System.out.print("Enter the value you want to fill the vector with: ");
        double value = SCANNER.nextDouble();

        double[] elements = new double[vectorSize];
        Arrays.fill(elements, value);
        Vector vector = new Vector(elements);
        System.out.println(vector);
        return vector;
    }

    private static Vector createVectorAndFillWithRandomValue(int vectorSize) {
        double[] elements = new double[vectorSize];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = Math.random() * 10;
        }
        Vector vector = new Vector(elements);
        System.out.println(vector);
        return vector;
    }

    public static Vector sorted(Vector vector) {
        double[] elements = new double[vector.elements().length];
        System.arraycopy(vector.elements(), 0, elements, 0, vector.elements().length);
        Arrays.sort(elements);
        return new Vector(elements);
    }

    public static Vector addVectors(Vector firstVector, Vector secondVector) {
        double[] elements = new double[firstVector.elements().length];
        for (int i = 0; i < firstVector.elements().length; i++) {
            elements[i] = firstVector.elements()[i] + secondVector.elements()[i];
        }
        return new Vector(elements);
    }

    public static Vector matrixVectorMultiplication(Matrix matrix, Vector vector) {
        double[] elements = Arrays.stream(matrix.elements())
                .mapToDouble(row -> IntStream
                        .range(0, row.length)
                        .mapToDouble(col -> row[col] * vector.elements()[col])
                        .sum()).toArray();
        return new Vector(elements);
    }

    public static double min(Vector vector) {
        return Arrays.stream(vector.elements()).min().getAsDouble();
    }

    public static Vector multiplyVectors(Vector firstVector, Vector secondVector) {
        Vector result = new Vector(firstVector.elements().length);
        for (int i = 0; i < firstVector.elements().length; i++) {
            result.setElement(i, firstVector.getElement(i) * secondVector.getElement(i));
        }
        return result;
    }

    public static Vector vectorScalarMultiplication(Vector vector, double scalar) {
        Vector result = new Vector(vector.elements().length);
        for (int i = 0; i < vector.elements().length; i++) {
            result.setElement(i, vector.getElement(i) * scalar);
        }
        return result;
    }
}
