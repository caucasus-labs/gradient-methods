package com.caucasus.optimization.algos.entities.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class QuadraticFunction {
    private final Matrix A;
    private final Vector b;
    private final Double c;
    public QuadraticFunction(Matrix a, Vector b, Double c) {
        A = a;
        this.b = b;
        this.c = c;
    }
    public QuadraticFunction(List<List<Double>> a , List<Double> b, double c) {
        A = new Matrix(a.stream().map(Vector::new).collect(Collectors.toList()));
        this.b = new Vector(b);
        this.c = c;

    }

    public Vector gradient(Vector x) {
        return A.mul(x).add(b);
    }

    public int size() {
        return b.size();
    }

    public Double apply(Vector x) {
        return A.mul(x).scalar(x) * 0.5 + b.scalar(x) + c;
    }


}
