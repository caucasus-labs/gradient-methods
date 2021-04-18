package com.caucasus.optimization.algos.entities.util;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
    private final List<Vector> vectors;

    public Matrix(List<Vector> vectors) {
        this.vectors = vectors;
    }

    public Vector mul(Vector vector) {
        List<Double> res = new ArrayList<>();
        for (Vector v : vectors) {
            res.add(v.scalar(vector));
        }
        return new Vector(res);
    }
}
