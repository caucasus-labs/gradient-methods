package com.caucasus.optimization.algos.entities.util;

import java.util.ArrayList;
import java.util.List;


public class QuadraticFunction {
    private final SymmetricMatrix A;
    private final Vector b;
    private final Double c;

    public QuadraticFunction(SymmetricMatrix a, Vector b, Double c) {
        A = a;
        this.b = b;
        this.c = c;
    }

    public QuadraticFunction(List<List<Double>> a, List<Double> b, double c) {
        A = new SymmetricMatrix(a);
        this.b = new Vector(b);
        this.c = c;

    }

    public List<List<Double>> getInitialA(){
        List<List<Double>> res = new ArrayList<>();
        for (int i = 0; i < A.size(); i++) {
            res.add(new ArrayList<>());
            for (int j = 0; j < A.size(); j++) {
                res.get(i).add(A.get(i, j) * ((i == j) ? 0.5 : 1));
            }
        }
        return res;
    }

    public Vector getGradient(Vector x) {
        return A.mul(x).add(b);
    }

    public int size() {
        return b.size();
    }

    public double apply(Vector x) {
        return A.mul(x).scalar(x) * 0.5 + b.scalar(x) + c;
    }

    public Vector shiftVector(Vector x, Vector gradient, double learningRate) {
        return x.add(gradient.mul(-learningRate));
    }

    public double getBeta(Vector gradient, Vector antiGradient) {
        return A.mul(gradient).scalar(antiGradient) / A.mul(antiGradient).scalar(antiGradient);
    }

    public double getLearningRate(Vector gradient, Vector antiGradient) {
        return -gradient.scalar(antiGradient) / (A.mul(antiGradient).scalar(antiGradient));
    }
}
