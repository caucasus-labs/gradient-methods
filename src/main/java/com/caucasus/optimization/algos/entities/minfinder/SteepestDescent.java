package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SteepestDescent extends AbstractGradientMethod {
    private final QuadraticFunction function;
    private final Double eps;
    private final Domain domain;
    private final Method method;

    public SteepestDescent(QuadraticFunction function, Double eps, Domain domain) {
        this(function, eps, domain, Method.GOLDEN_SECTION);
    }

    public SteepestDescent(QuadraticFunction function, Double eps, Domain domain, Method method) {
        this.function = function;
        this.eps = eps;
        this.domain = domain;
        this.method = method;
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
        do {
            lastPoint = newPoint;
            lastValue = newValue;
            final Vector pass = function.getGradient(lastPoint);
            gradient = pass;
            final Vector xk = lastPoint;
            final Function<Double, Double> funcToMinimize = alpha -> function.apply(xk.add(pass.mul(-alpha)));
            double newAlpha = (switch (method) {
                case FIBONACCI ->   new Fibonacci(funcToMinimize, 0., 1., eps).getSolution();
                case DICHOTOMY ->   new Dichotomy(funcToMinimize, 0., 1., eps).getSolution();
                case BRENT ->           new Brent(funcToMinimize, 0., 1., eps).getParaboloidSolution();
                case PARABOLOID -> new Paraboloid(funcToMinimize, 0., 1., eps).getParaboloidSolution();
                default ->      new GoldenSection(funcToMinimize, 0., 1., eps).getSolution();
            }).getEndPoint();

            newPoint = function.shiftVector(lastPoint, gradient, newAlpha);
            newValue = function.apply(lastPoint);

            if (saveIterations) {
                savePoint(newPoint, points);
                saveValue(newValue, values);
            }
            iterations++;
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
