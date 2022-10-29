package com.itzroma.kpi.semester5.parallelprogramming.pputils.vector;

import java.util.Arrays;

public record Vector(double[] elements) {
    public Vector(int size) {
        this(new double[size]);
    }

    public double getElement(int index) {
        return elements[index];
    }

    public void setElement(int index, double value) {
        elements[index] = value;
    }

    public Vector copy() {
        return new Vector(elements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Arrays.equals(elements, vector.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }
}
