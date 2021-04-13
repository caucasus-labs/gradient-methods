package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.DoubleComparator;
import com.caucasus.optimization.algos.entities.util.Interval;
import com.caucasus.optimization.algos.interfaces.MinFinder;

import java.util.Comparator;
import java.util.function.Function;

/**
 * This class provides a skeletal implementation of the MinFinder interface to minimize the effort required to implement this interface.
 *
 * @see MinFinder
 */
public abstract class AbstractMinFinder implements MinFinder {
    private final Function<Double, Double> function;
    private final Interval domain;
    private final double eps;
    private final Comparator<Double> comparator;
    private int counter;

    /**
     * Sole constructor
     *
     * @param function function on which to search
     * @param domain   domain of function definition
     * @param eps      epsilon which is used to calculate
     */
    public AbstractMinFinder(Function<Double, Double> function, Interval domain, double eps) {
        this.function = function;
        this.domain = domain;
        this.eps = eps;
        this.comparator = new DoubleComparator(eps);
        this.counter = 0;
    }

    /**
     * method provides comparing of doubles in MinFinder
     *
     * @param lhs left hand side operand
     * @param rhs right hand side operand
     * @return -1 if lhs < rhs, 0 if lhs == rhs, 1 if lhs > rhs
     */
    protected int compare(Double lhs, Double rhs) {
        return comparator.compare(lhs, rhs);
    }

    @Override
    public Function<Double, Double> getFunction() {
        return function;
    }

    @Override
    public double getLeftBorder() {
        return domain.getLeftBorder();
    }

    @Override
    public double getRightBorder() {
        return domain.getRightBorder();
    }

    @Override
    public double getEps() {
        return eps;
    }

    @Override
    public double evaluateFunction(double x) {
        counter++;
        return function.apply(x);
    }

    @Override
    public int getCountOfEvaluations() {
        return counter;
    }

}
