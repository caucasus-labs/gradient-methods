package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.*;
import com.caucasus.optimization.algos.interfaces.GradientMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Gradient implements GradientMethod {
    private final QuadraticFunction function;
    private final Double eps;
    private final Domain domain;

    public Gradient(QuadraticFunction function, Double eps, Domain domain) {
        this.function = function;
        this.eps = eps;
        this.domain = domain;
    }

    @Override
    public GradientSolution getSolution() {
        List<Vector> points = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        int iterations = 0;
        points.add(domain.middle());
        values.add(function.apply(points.get(0)));
        Vector gradient = function.getGradient(points.get(0));
        // TODO: what am I doing wrong?
        double learningRate = function.getLearningRate(gradient, gradient.mul(-1));
        if (Double.isNaN(learningRate)) {
            final String msg = "Learning rate is Nan! Points: " +
                    points.stream().map(Vector::toString).collect(Collectors.joining(", "));
            throw new RuntimeException(msg);
        }
        do {
            if (iterations != 0) {
                gradient = function.getGradient(points.get(0));
            }
            while (true) {
                Vector newPoint = function.shiftVector(points.get(iterations), gradient, learningRate);
                double newValue = function.apply(newPoint);
                if (newValue - values.get(iterations) < eps) {
                    points.add(newPoint);
                    values.add(newValue);
                    iterations++;
                    break;
                } else {
                    learningRate *= 0.5;
                }
            }
        } while (iterations < 10000 && (Math.abs(values.get(iterations) - values.get(iterations - 1)) > eps ||
                points.get(iterations).dist(points.get(iterations - 1)) > eps ||
                gradient.length() > eps));
        return new GradientSolution(points, values);
    }

}
