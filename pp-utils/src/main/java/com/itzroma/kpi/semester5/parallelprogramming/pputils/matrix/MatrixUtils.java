package com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix;

import java.util.Arrays;
import java.util.Scanner;

public class MatrixUtils {
    private static final Scanner SCANNER;

    static {
        SCANNER = new Scanner(System.in);
    }

    public static Matrix scanMatrix(int matrixSize) {
        if (matrixSize > 0 && matrixSize <= 4) return scanMatrixFromUser(matrixSize);
        if (matrixSize > 4) return chooseFillOption(matrixSize);
        throw new IllegalArgumentException("Invalid matrix size provided");
    }

    private static Matrix chooseFillOption(int matrixSize) {
        System.out.print("Choose an option to fill the matrix (1 - value, 2 - random): ");
        switch (SCANNER.nextInt()) {
            case 1 -> {
                return createMatrixAndFillWithValue(matrixSize);
            }
            case 2 -> {
                return createMatrixAndFillWithRandomValue(matrixSize);
            }
            default -> throw new IllegalArgumentException("Invalid option provided");
        }
    }

    private static Matrix scanMatrixFromUser(int matrixSize) {
        double[][] matrix = new double[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                System.out.printf("Element %d:%d: ", i + 1, j + 1);
                matrix[i][j] = SCANNER.nextDouble();
            }
        }
        return new Matrix(matrix);
    }

    private static Matrix createMatrixAndFillWithValue(int matrixSize) {
        double[][] elements = new double[matrixSize][matrixSize];

        System.out.print("Enter the value you want to fill matrix with: ");
        double value = SCANNER.nextDouble();

        for (double[] row : elements) {
            Arrays.fill(row, value);
        }
        Matrix matrix = new Matrix(elements);
        System.out.println(matrix);
        return matrix;
    }

    private static Matrix createMatrixAndFillWithRandomValue(int matrixSize) {
        double[][] elements = new double[matrixSize][matrixSize];
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[i].length; j++) {
                elements[i][j] = Math.random() * 10;
            }
        }
        Matrix matrix = new Matrix(elements);
        System.out.println(matrix);
        return matrix;
    }

    public static Matrix multiplyMatrices(Matrix firstMatrix, Matrix secondMatrix) {
        double[][] result = new double[firstMatrix.elements().length][secondMatrix.elements()[0].length];
        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
            }
        }
        return new Matrix(result);
    }

    public static double multiplyMatricesCell(Matrix firstMatrix, Matrix secondMatrix, int row, int col) {
        double cell = 0;
        for (int i = 0; i < secondMatrix.elements().length; i++) {
            cell += firstMatrix.elements()[row][i] * secondMatrix.elements()[i][col];
        }
        return cell;
    }

    public static Matrix transposeMatrix(Matrix matrix) {
        int rows = matrix.elements().length;
        int cols = matrix.elements()[0].length;
        double[][] transpose = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transpose[j][i] = matrix.elements()[i][j];
            }
        }
        return new Matrix(transpose);
    }

    public static Matrix addMatrices(Matrix firstMatrix, Matrix secondMatrix) {
        double[][] elements = new double[firstMatrix.elements().length][secondMatrix.elements()[0].length];
        for (int i = 0; i < firstMatrix.elements().length; i++) {
            for (int j = 0; j < firstMatrix.elements()[i].length; j++) {
                elements[i][j] = firstMatrix.elements()[i][j] + secondMatrix.elements()[i][j];
            }
        }
        return new Matrix(elements);
    }

    public static Matrix sorted(Matrix matrix) {
        double[][] elements = new double[matrix.elements().length][matrix.elements()[0].length];
        System.arraycopy(matrix.elements(), 0, elements, 0, matrix.elements().length);
        Arrays.stream(elements).forEach(Arrays::sort);
        return new Matrix(elements);
    }

    public static Matrix matrixScalarMultiplication(Matrix matrix, double scalar) {
        double[][] elements = new double[matrix.elements().length][matrix.elements()[0].length];
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[i].length; j++) {
                elements[i][j] = matrix.elements()[i][j] * scalar;
            }
        }
        return new Matrix(elements);
    }
}
