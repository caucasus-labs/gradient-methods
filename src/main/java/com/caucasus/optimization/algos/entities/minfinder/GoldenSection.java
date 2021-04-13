package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.Interval;
import com.caucasus.optimization.algos.entities.util.Solution;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * A IntervalMinFinder implementation based on Golden Section minimization method
 *
 * @see com.caucasus.optimization.algos.interfaces.IntervalMinFinder
 */
public class GoldenSection extends AbstractIntervalMinFinder {
    private static final double TAU = (Math.sqrt(5) - 1) * 0.5;

    /**
     * Constructs new method immutable object
     *
     * @param function    function on which to search
     * @param leftBorder  left border of domain of function definition
     * @param rightBorder right border of domain of function definition
     * @param eps         epsilon which is used to calculate
     */
    public GoldenSection(Function<Double, Double> function, double leftBorder, double rightBorder, double eps) {
        super(function, leftBorder, rightBorder, eps);
    }

    /**
     * Constructs new method immutable object
     *
     * @param function function on which to search
     * @param domain   domain of function definition
     * @param eps      epsilon which is used to calculate
     */
    public GoldenSection(Function<Double, Double> function, Interval domain, double eps) {
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
        double x1 = leftBorder + (1 - TAU) * (rightBorder - leftBorder);
        double x2 = leftBorder + TAU * (rightBorder - leftBorder);
        double f1 = evaluateFunction(x1);
        double f2 = evaluateFunction(x2);
        double nthEps = (rightBorder - leftBorder) * 0.5;
        do {
            if (compare(f1, f2) <= 0) {
                rightBorder = x2;
                x2 = x1;
                f2 = f1;
                x1 = rightBorder - TAU * (rightBorder - leftBorder);
                f1 = evaluateFunction(x1);
            } else {
                leftBorder = x1;
                x1 = x2;
                f1 = f2;
                x2 = leftBorder + TAU * (rightBorder - leftBorder);
                f2 = evaluateFunction(x2);
            }
            nthEps *= TAU;
            intervals.add(new Interval(leftBorder, rightBorder));
            approximatelyMinimums.add((leftBorder + rightBorder) * 0.5);
        } while (compare(nthEps, getEps()) == 1);

        return new Solution(intervals, approximatelyMinimums);
    }

}
