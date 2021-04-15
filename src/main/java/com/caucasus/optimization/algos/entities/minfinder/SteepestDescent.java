package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.*;
import com.caucasus.optimization.algos.interfaces.GradientMethod;
import com.caucasus.optimization.algos.interfaces.IntervalMinFinder;

import java.util.ArrayList;
import java.util.List;

public class SteepestDescent implements GradientMethod {
    private final QuadraticFunction function;
    private final Double eps;
    private final Domain domain;

    public SteepestDescent(QuadraticFunction function, Double eps, Domain domain) {
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
        double learningRate = function.getLearningRate(gradient, gradient.mul(-1));
        do {
            final Vector pass = function.getGradient(points.get(iterations));
            gradient = pass;
            final int pos = iterations;
            IntervalMinFinder minFinder = new GoldenSection( (Double lR)-> function.apply(points.get(pos).add(pass.mul(-lR))), 0., learningRate, eps);
            Solution solution = minFinder.getSolution();
            points.add(function.shiftVector(points.get(iterations), gradient, solution.getEndPoint()));
            iterations++;
            values.add(function.apply(points.get(iterations)));
        } while (Math.abs(values.get(iterations) - values.get(iterations - 1)) > eps ||
                points.get(iterations).dist(points.get(iterations - 1)) > eps ||
                gradient.length() > eps);
        return new GradientSolution(points, values);
    }
}
