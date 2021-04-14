package com.caucasus.optimization.algos.entities.util;

public class Domain {
    private final Vector lower;
    private final Vector upper;

    public Domain(Vector lower, Vector upper) {
        this.lower = lower;
        this.upper = upper;
    }
}
