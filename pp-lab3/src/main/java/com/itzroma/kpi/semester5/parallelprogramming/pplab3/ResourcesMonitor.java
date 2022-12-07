package com.itzroma.kpi.semester5.parallelprogramming.pplab3;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResourcesMonitor {
    private final Resources resources;

    public synchronized void addToScalarR(double value) {
        resources.setScalarR(resources.getScalarR() + value);
    }

    public synchronized double copyScalarR() {
        return resources.getScalarR();
    }

    public synchronized double copyScalarX() {
        return resources.getScalarX();
    }
}
