package com.caucasus.optimization.algos.entities.util;

import java.util.List;

public class GradientSolution {
    private final List<Vector> vectors;

    public GradientSolution(List<Vector> vectors) {
        this.vectors = vectors;
    }

    public List<Vector> getPoints() {
        return vectors;
    }

    public Vector getEndPoint() {
        return vectors.get(vectors.size() - 1);
    }

}
