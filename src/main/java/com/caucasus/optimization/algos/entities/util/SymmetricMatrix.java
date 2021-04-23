package com.caucasus.optimization.algos.entities.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SymmetricMatrix {
    private final List<List<Double>> values;

    public SymmetricMatrix(List<List<Double>> initialValue) {
        values = new ArrayList<>();
        for (int i = 0; i < initialValue.size(); i++) {
            values.add(new ArrayList<>());
            for (int j = 0; j <= i; j++) {
                values.get(i).add(initialValue.get(i).get(j) * 2);
            }
        }
    }

    public Double get(int i, int j) {
        return (i == j) ? values.get(i).get(j) : ((j < i) ? values.get(i).get(j) : values.get(j).get(i)) * 0.5;
    }

    public Vector mul(Vector vector) {
        List<Double> res = new ArrayList<>(Collections.nCopies(vector.size(), 0.));
        for (int i = 0; i < vector.size(); i++) {
            for (int j = 0; j < vector.size(); j++) {
                res.set(i, res.get(i) + get(i, j) * vector.get(j));
            }
        }
        return new Vector(res);
    }
}
