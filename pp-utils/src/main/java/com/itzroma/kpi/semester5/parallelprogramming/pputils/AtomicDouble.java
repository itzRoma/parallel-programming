package com.itzroma.kpi.semester5.parallelprogramming.pputils;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicDouble extends Number {
    private final AtomicLong bits;

    public AtomicDouble() {
        this(0.0d);
    }

    public AtomicDouble(double initialValue) {
        bits = new AtomicLong(Double.doubleToLongBits(initialValue));
    }

    public final double get() {
        return Double.longBitsToDouble(bits.get());
    }

    public final void set(double newValue) {
        bits.set(Double.doubleToLongBits(newValue));
    }

    /**
     * Compares and sets the minimum between current and provided values.
     *
     * @param x the value to compare with
     */
    public final void compareMinAndSet(double x) {
        bits.accumulateAndGet(Double.doubleToLongBits(x), Math::min);
    }

    /**
     * Compares and sets the maximum between current and provided values.
     *
     * @param x the value to compare with
     */
    public final void compareMaxAndSet(double x) {
        bits.accumulateAndGet(Double.doubleToLongBits(x), Math::max);
    }

    @Override
    public int intValue() {
        return (int) get();
    }

    @Override
    public long longValue() {
        return (long) get();
    }

    @Override
    public float floatValue() {
        return (float) get();
    }

    @Override
    public double doubleValue() {
        return get();
    }
}
