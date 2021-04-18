package com.caucasus.optimization.algos.entities.util;

import java.util.List;

public class GradientSolution {
    private final List<Vector> vectors;
    private final List<Double> values;

    public GradientSolution(List<Vector> vectors, List<Double> values) {
        this.vectors = vectors;
        this.values = values;
    }

    public List<Vector> getPoints() {
        return vectors;
    }

    public Vector getEndPoint() {
        return vectors.get(vectors.size() - 1);
    }

    public int getIterations() {
        return vectors.size();
    }
}
