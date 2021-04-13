package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.Interval;
import com.caucasus.optimization.algos.entities.util.ParaboloidSolution;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * A ParaboloidMinFinder implementation based on Golden Section minimization method
 *
 * @see com.caucasus.optimization.algos.interfaces.ParaboloidMinFinder
 */
public class Paraboloid extends AbstractParaboloidMinFinder {
    /**
     * Constructs new method immutable object
     *
     * @param function    function on which to search
     * @param leftBorder  left border of domain of function definition
     * @param rightBorder right border of domain of function definition
     * @param eps         epsilon which is used to calculate
     */
    public Paraboloid(Function<Double, Double> function, double leftBorder, double rightBorder, double eps) {
        super(function, leftBorder, rightBorder, eps);
    }

    /**
     * Constructs new method immutable object
     *
     * @param function function on which to search
     * @param domain   domain of function definition
     * @param eps      epsilon which is used to calculate
     */
    public Paraboloid(Function<Double, Double> function, Interval domain, double eps) {
        super(function, domain, eps);
    }

    @Override
    public ParaboloidSolution calculateParaboloidSolution() {
        double leftBorder = getLeftBorder();
        double rightBorder = getRightBorder();
        ArrayList<Interval> intervals = new ArrayList<>();
        ArrayList<Function<Double, Double>> functions = new ArrayList<>();
        ArrayList<Double> approximatelyMinimums = new ArrayList<>();
        intervals.add(new Interval(leftBorder, rightBorder));
        double x1 = leftBorder;
        double x2 = (leftBorder + rightBorder) * 0.5;
        double x3 = rightBorder;
        double f1 = evaluateFunction(x1);
        double f2 = evaluateFunction(x2);
        double f3 = evaluateFunction(x3);
        Parabola parabola = new Parabola(x1, x2, x3);
        approximatelyMinimums.add(parabola.getPointOfMin());
        functions.add(parabola.getParabolaFunction());
        while (!validateAccuracy(leftBorder, rightBorder)) {
            double fm = evaluateFunction(parabola.getPointOfMin());
            if (compare(x1, parabola.getPointOfMin()) < 0 && compare(parabola.getPointOfMin(), x2) < 0
                    && compare(fm, f2) >= 0) {
                leftBorder = parabola.getPointOfMin();
                rightBorder = x3;
                x1 = parabola.getPointOfMin();
                f1 = fm;
            } else if (compare(x1, parabola.getPointOfMin()) < 0 && compare(parabola.getPointOfMin(), x2) < 0
                    && compare(fm, f2) < 0) {
                leftBorder = x1;
                rightBorder = x2;
                x3 = x2;
                f3 = f2;
                x2 = parabola.getPointOfMin();
                f2 = fm;
            } else if (compare(x2, parabola.getPointOfMin()) < 0 && compare(parabola.getPointOfMin(), x3) < 0
                    && compare(fm, f2) <= 0) {
                leftBorder = x2;
                rightBorder = x3;
                x1 = x2;
                f1 = f2;
                x2 = parabola.getPointOfMin();
                f2 = fm;
            } else {
                leftBorder = x1;
                rightBorder = parabola.getPointOfMin();
                x3 = parabola.getPointOfMin();
                f3 = fm;
            }
            if (compare(rightBorder - leftBorder, getEps()) >= 0) {
                intervals.add(new Interval(leftBorder, rightBorder));
                parabola = new Parabola(x1, x2, x3, f1, f2, f3);
                approximatelyMinimums.add(parabola.getPointOfMin());
                functions.add(parabola.getParabolaFunction());
            }
        }

        return new ParaboloidSolution(intervals, approximatelyMinimums, functions);
    }

    private double calcNthEps(double lb, double rb) {
        return (rb - lb) * 0.5;
    }

    private boolean validateAccuracy(double lb, double rb) {
        return compare(calcNthEps(lb, rb), getEps()) <= 0;
    }




}
