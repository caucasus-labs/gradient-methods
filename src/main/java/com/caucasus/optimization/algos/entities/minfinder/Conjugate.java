package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.Domain;
import com.caucasus.optimization.algos.entities.util.GradientSolution;
import com.caucasus.optimization.algos.entities.util.QuadraticFunction;
import com.caucasus.optimization.algos.entities.util.Vector;
import com.caucasus.optimization.algos.interfaces.GradientMethod;

import java.util.ArrayList;
import java.util.List;

public class Conjugate implements GradientMethod {
    private final QuadraticFunction function;
    private final Double eps;
    private final Domain domain;

    public Conjugate(QuadraticFunction function, Double eps, Domain domain) {
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
        Vector antiGradient = gradient.mul(-1);
        double learningRate = function.getLearningRate(gradient, gradient.mul(-1));
        do {
            Vector newPoint = points.get(iterations).add(antiGradient.mul(learningRate));
            gradient = function.getGradient(newPoint);
            double beta = function.getBeta(gradient, antiGradient);
            antiGradient = gradient.mul(-1.).add(antiGradient.mul(beta));
            learningRate = function.getLearningRate(gradient, antiGradient);
            points.add(newPoint);
            values.add(function.apply(newPoint));
            iterations++;
        } while (Math.abs(values.get(iterations) - values.get(iterations - 1)) > eps ||
                points.get(iterations).dist(points.get(iterations - 1)) > eps ||
                gradient.length() > eps);
        return new GradientSolution(points, values);
    }
}
