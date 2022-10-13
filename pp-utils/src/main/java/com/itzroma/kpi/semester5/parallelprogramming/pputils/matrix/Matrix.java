package com.itzroma.kpi.semester5.parallelprogramming.pputils.matrix;

import java.util.Arrays;

public record Matrix(double[][] elements) {
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        Arrays.stream(elements).forEach(row -> res.append(Arrays.toString(row)).append(System.lineSeparator()));
        return res.delete(res.length() - 2, res.length()).toString();
    }
}
