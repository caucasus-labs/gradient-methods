package com.caucasus.optimization.algos.entities.util;

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

    public QuadraticFunction(List<Double> a, List<Double> b, double c) {
        A = new Matrix(a);
        this.b = new Vector(b);
        this.c = c;

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
