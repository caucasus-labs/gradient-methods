package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.*;
import com.caucasus.optimization.algos.interfaces.GradientMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Gradient extends AbstractGradientMethod {
    private final QuadraticFunction function;
    private final Double eps;
    private final Domain domain;

    public Gradient(QuadraticFunction function, Double eps, Domain domain) {
        this.function = function;
        this.eps = eps;
        this.domain = domain;
    }

    @Override
    public GradientSolution getSolution(boolean saveIterations) {
        iterations = 0;
        List<Vector> points = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        Vector newPoint = domain.between();
        Vector lastPoint = newPoint;
        double newValue = function.apply(lastPoint);
        double lastValue = newValue;
        if (saveIterations) {
            savePoint(newPoint, points);
            saveValue(newValue, values);
        }
        Vector gradient = function.getGradient(newPoint);
        double learningRate = function.getLearningRate(gradient, gradient.mul(-1));
        if (Double.isNaN(learningRate)) {
            final String msg = "Learning rate is Nan! Points: " +
                    points.stream().map(Vector::toString).collect(Collectors.joining(", "));
            throw new RuntimeException(msg);
        }
        do {
            if (iterations != 0) {
                gradient = function.getGradient(lastPoint);
            }
            while (true) {
                newPoint = function.shiftVector(lastPoint, gradient, learningRate);
                newValue = function.apply(newPoint);
                if (newValue - lastValue < eps) {
                    if (saveIterations) {
                        savePoint(newPoint, points);
                        saveValue(newValue, values);
                    }
                    iterations++;
                    break;
                } else {
                    learningRate *= 0.5;
                }
            }
            lastPoint = newPoint;
            lastValue = newValue;
        } while (iterations < 10000 && (Math.abs(newValue - lastValue) > eps ||
                newPoint.dist(lastPoint) > eps ||
                gradient.length() > eps));

        if (!saveIterations) {
            savePoint(newPoint, points);
            saveValue(newValue, values);
        }
        isCounted = true;
        return new GradientSolution(points, values);
    }
}
