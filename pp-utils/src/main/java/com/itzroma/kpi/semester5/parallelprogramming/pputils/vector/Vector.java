package com.itzroma.kpi.semester5.parallelprogramming.pputils.vector;

import java.util.Arrays;

public record Vector(double[] elements) {
    @Override
    public String toString() {
        return Arrays.toString(elements);
    }
}
