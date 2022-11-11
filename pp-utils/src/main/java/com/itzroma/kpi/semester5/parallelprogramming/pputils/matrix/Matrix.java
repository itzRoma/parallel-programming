package com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix;

import java.util.Arrays;

public record Matrix(double[][] elements) {
    public Matrix(int rows, int cols) {
        this(new double[rows][cols]);
    }

    public double getElement(int row, int col) {
        return elements[row][col];
    }

    public void setElement(int row, int col, double value) {
        elements[row][col] = value;
    }

    public Matrix copy() {
        return new Matrix(elements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;
        return Arrays.deepEquals(elements, matrix.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(elements);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        Arrays.stream(elements).forEach(row -> res.append(Arrays.toString(row)).append(System.lineSeparator()));
        return res.delete(res.length() - 2, res.length()).toString();
    }
}
