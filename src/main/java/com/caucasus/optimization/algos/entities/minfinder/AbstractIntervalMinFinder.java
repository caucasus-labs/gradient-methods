package com.caucasus.optimization.algos.entities.minfinder;


import com.caucasus.optimization.algos.entities.util.Interval;
import com.caucasus.optimization.algos.entities.util.Solution;
import com.caucasus.optimization.algos.interfaces.IntervalMinFinder;

import java.util.function.Function;

/**
 * This class provides a skeletal implementation of the IntervalMinFinder interface to minimize the effort required to implement this interface.
 *
 * @see IntervalMinFinder
 */
abstract public class AbstractIntervalMinFinder extends AbstractMinFinder implements IntervalMinFinder {
    private Solution solution;

    /**
     * Sole constructor
     *
     * @param function    function on which to search
     * @param leftBorder  left border of domain of function definition
     * @param rightBorder right border of domain of function definition
     * @param eps         epsilon which is used to calculate
     */
    public AbstractIntervalMinFinder(Function<Double, Double> function, double leftBorder, double rightBorder, double eps) {
        this(function, new Interval(leftBorder, rightBorder), eps);
    }

    /**
     * Sole constructor
     *
     * @param function function on which to search
     * @param domain   domain of function definition
     * @param eps      epsilon which is used to calculate
     */
    public AbstractIntervalMinFinder(Function<Double, Double> function, Interval domain, double eps) {
        super(function, domain, eps);
    }

    @Override
    public Solution getSolution() {
        if (solution == null) {
            solution = calculateSolution();
        }
        return solution;
    }

    /**
     * This method provides solution by method implementation
     *
     * @return interval method solution
     */
    protected abstract Solution calculateSolution();
}
