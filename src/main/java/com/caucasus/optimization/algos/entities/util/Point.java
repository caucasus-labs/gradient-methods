package com.caucasus.optimization.algos.entities.util;

import java.util.List;

public class Point {
    private final List<Double> values;

    public Point(List<Double> values) {
        this.values = values;
    }

    public Double get(int i) {
        return values.get(i);
    }
}
