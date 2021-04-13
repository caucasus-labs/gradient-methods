package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.Interval;
import com.caucasus.optimization.algos.entities.util.ParaboloidSolution;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * A IntervalMinFinder implementation based on Brent minimization method
 *
 * @see com.caucasus.optimization.algos.interfaces.IntervalMinFinder
 */
public class Brent extends AbstractParaboloidMinFinder {
    private static final double K = (3 - Math.sqrt(5)) / 2;

    /**
     * Constructs new method immutable object
     *
     * @param function    function on which to search
     * @param leftBorder  left border of domain of function definition
     * @param rightBorder right border of domain of function definition
     * @param eps         epsilon which is used to calculate
     */
    public Brent(Function<Double, Double> function, double leftBorder, double rightBorder, double eps) {
        super(function, leftBorder, rightBorder, eps);
    }

    /**
     * Constructs new method immutable object
     *
     * @param function function on which to search
     * @param domain   domain of function definition
     * @param eps      epsilon which is used to calculate
     */
    public Brent(Function<Double, Double> function, Interval domain, double eps) {
        super(function, domain, eps);
    }

    @Override
    public ParaboloidSolution calculateParaboloidSolution() {
        double leftBorder, rightBorder, x, w, v, fx, fw, fv, d, e, g, u, fu;
        leftBorder = getLeftBorder();
        rightBorder = getRightBorder();
        ArrayList<Interval> intervals = new ArrayList<>();
        ArrayList<Function<Double, Double>> functions = new ArrayList<>();
        ArrayList<Double> approximatelyMinimums = new ArrayList<>();
        x = w = v = leftBorder + K * (rightBorder - leftBorder);
        intervals.add(new Interval(leftBorder, rightBorder));
        functions.add(null);
        approximatelyMinimums.add(x);
        fx = fw = fv = evaluateFunction(x);
        d = e = rightBorder - leftBorder;
        double tol;
        Parabola parabola;
        while (compare(Math.abs(rightBorder - leftBorder), getEps()) > 0) {
            parabola = null;
            g = e;
            e = d;
            tol = getEps() * Math.abs(x) + getEps() / 10;
            if (compare(Math.abs(x - (leftBorder + rightBorder) * 0.5) + (rightBorder - leftBorder) * 0.5, 2 * tol) <= 0) {
                break;
            }
            if (areDifferent(x, w, v) && areDifferent(fx, fw, fv)) {
                parabola = new Parabola(x, w, v, fx, fw, fv);
                u = parabola.getPointOfMin();
                if (compare(u, leftBorder) >= 0 && compare(u, rightBorder) <= 0 && compare(Math.abs(u - x), g * 0.5) < 0) {
                    if (compare((u - leftBorder), 2 * tol) < 0 || compare((rightBorder - u), 2 * tol) < 0) {
                        u = x - Math.signum(x - (leftBorder + rightBorder) * 0.5) * tol;
                    }
                }
            } else {
                if (compare(x, (leftBorder + rightBorder) * 0.5) < 0) {
                    u = x + K * (rightBorder - x);
                    e = rightBorder - x;
                } else {
                    u = x - K * (x - leftBorder);
                    e = x - leftBorder;
                }
            }
            if (compare(Math.abs(u - x), tol) < 0) {
                u = x + Math.signum(u - x) * tol;
            }
            d = Math.abs(u - x);
            fu = evaluateFunction(u);
            if (compare(fu, fx) <= 0) {
                if (compare(u, x) >= 0) {
                    leftBorder = x;
                } else {
                    rightBorder = x;
                }
                v = w;
                w = x;
                x = u;
                fv = fw;
                fw = fx;
                fx = fu;
            } else {
                if (compare(u, x) >= 0) {
                    rightBorder = u;
                } else {
                    leftBorder = u;
                }
                if (compare(fu, fw) <= 0 || compare(w, x) == 0) {
                    v = w;
                    w = u;
                    fv = fw;
                    fw = fu;
                } else if (compare(fu, fv) <= 0 || compare(v, x) == 0 || compare(v, w) == 0) {
                    v = u;
                    fv = fu;
                }
            }
            intervals.add(new Interval(leftBorder, rightBorder));
            functions.add((parabola == null) ? null : parabola.getParabolaFunction());
            approximatelyMinimums.add(x);
        }

        return new ParaboloidSolution(intervals, approximatelyMinimums, functions);
    }

    private boolean notEqual(double lhs, double rhs) {
        return compare(lhs, rhs) != 0;
    }

    private boolean areDifferent(double a, double b, double c) {
        return notEqual(a, b) && notEqual(b, c) && notEqual(a, c);
    }


}
