package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.Domain;
import com.caucasus.optimization.algos.entities.util.GradientSolution;
import com.caucasus.optimization.algos.entities.util.QuadraticFunction;
import com.caucasus.optimization.algos.entities.util.Vector;
import com.caucasus.optimization.algos.interfaces.GradientMethod;

import java.util.ArrayList;
import java.util.List;

public class Conjugate extends AbstractGradientMethod {
    private final QuadraticFunction function;
    private final Double eps;
    private final Domain domain;

    public Conjugate(QuadraticFunction function, Double eps, Domain domain) {
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
        double lastValue;
        if (saveIterations) {
            savePoint(newPoint, points);
            saveValue(newValue, values);
        }
        Vector gradient = function.getGradient(newPoint);
        double learningRate = function.getLearningRate(gradient, gradient.mul(-1));
        Vector antiGradient = gradient.mul(-1);
        do {
            newPoint = lastPoint.add(antiGradient.mul(learningRate));
            gradient = function.getGradient(newPoint);
            double beta = function.getBeta(gradient, antiGradient);
            antiGradient = gradient.mul(-1.).add(antiGradient.mul(beta));
            learningRate = function.getLearningRate(gradient, antiGradient);

            if (saveIterations) {
                savePoint(newPoint, points);
                saveValue(newValue, values);
            }
            iterations++;
            lastValue = newValue;
            lastPoint = newPoint;
        } while (Math.abs(newValue - lastValue) > eps ||
                newPoint.dist(lastPoint) > eps ||
                gradient.length() > eps);

        if (!saveIterations) {
            savePoint(newPoint, points);
            saveValue(newValue, values);
        }
        isCounted = true;
        return new GradientSolution(points, values);
    }
}
