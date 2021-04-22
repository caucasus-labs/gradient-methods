package com.caucasus.optimization.algos.entities.util;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
    private final List<Double> vectors;

    public Matrix(List<Double> vectors) {
        this.vectors = vectors;
    }

    public Vector mul(Vector vector) {
        List<Double> res = new ArrayList<>(vector.size());
        for (int i = 0; i < vector.size(); i++) {
            res.add(vectors.get(i) * vector.get(i));
        }
        return new Vector(res);
    }
}
