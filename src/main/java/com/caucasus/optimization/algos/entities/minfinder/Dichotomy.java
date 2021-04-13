package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.Interval;
import com.caucasus.optimization.algos.entities.util.Solution;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * A IntervalMinFinder implementation based on Dichotomy minimization method
 *
 * @see com.caucasus.optimization.algos.interfaces.IntervalMinFinder
 */
public class Dichotomy extends AbstractIntervalMinFinder {
    /**
     * Constructs new method immutable object
     *
     * @param function    function on which to search
     * @param leftBorder  left border of domain of function definition
     * @param rightBorder right border of domain of function definition
     * @param eps         epsilon which is used to calculate
     */
    public Dichotomy(Function<Double, Double> function, double leftBorder, double rightBorder, double eps) {
        super(function, leftBorder, rightBorder, eps);
    }

    /**
     * Constructs new method immutable object
     *
     * @param function function on which to search
     * @param domain   domain of function definition
     * @param eps      epsilon which is used to calculate
     */
    public Dichotomy(Function<Double, Double> function, Interval domain, double eps) {
        super(function, domain, eps);
    }

    @Override
    public Solution calculateSolution() {
        double leftBorder = getLeftBorder();
        double rightBorder = getRightBorder();
        ArrayList<Interval> intervals = new ArrayList<>();
        ArrayList<Double> approximatelyMinimums = new ArrayList<>();
        approximatelyMinimums.add((leftBorder + rightBorder) * 0.5);
        intervals.add(new Interval(leftBorder, rightBorder));
        while (!validateAccuracy(leftBorder, rightBorder)) {
            final double x1 = (leftBorder + rightBorder - getDelta()) * 0.5;
            final double x2 = (leftBorder + rightBorder + getDelta()) * 0.5;
            if (compare(evaluateFunction(x1), evaluateFunction(x2)) <= 0) {
                rightBorder = x2;
            } else {
                leftBorder = x1;
            }
            intervals.add(new Interval(leftBorder, rightBorder));
            approximatelyMinimums.add((leftBorder + rightBorder) * 0.5);
        }
        return new Solution(intervals, approximatelyMinimums);
    }

    private double getDelta() {
        return getEps() * 0.5;
    }

    private double calcNthEps(double lb, double rb) {
        return (rb - lb) * 0.5;
    }

    private boolean validateAccuracy(double lb, double rb) {
        return compare(calcNthEps(lb, rb), getEps()) <= 0;
    }
}
