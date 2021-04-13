package com.caucasus.optimization.algos.entities.util;

import java.util.Comparator;

/**
 * Implements custom comparator for double
 */
public class DoubleComparator implements Comparator<Double> {
    private final double eps;

    /**
     * Construct new comparator from epsilon
     *
     * @param eps epsilon for comparing
     */
    public DoubleComparator(double eps) {
        this.eps = eps;
    }

    @Override
    public int compare(Double lhs, Double rhs) {
        return Double.compare(lhs, rhs);
    }
}
