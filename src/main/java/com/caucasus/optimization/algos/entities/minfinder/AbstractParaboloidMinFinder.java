package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.Interval;
import com.caucasus.optimization.algos.entities.util.ParaboloidSolution;
import com.caucasus.optimization.algos.interfaces.ParaboloidMinFinder;

import java.util.function.Function;

/**
 * This class provides a skeletal implementation of the ParaboloidMinFinder interface to minimize the effort required to implement this interface.
 *
 * @see ParaboloidMinFinder
 */
abstract public class AbstractParaboloidMinFinder extends AbstractMinFinder implements ParaboloidMinFinder {
    private ParaboloidSolution paraboloidSolution;

    /**
     * Sole constructor
     *
     * @param function    function on which to search
     * @param leftBorder  left border of domain of function definition
     * @param rightBorder right border of domain of function definition
     * @param eps         epsilon which is used to calculate
     */
    public AbstractParaboloidMinFinder(Function<Double, Double> function, double leftBorder, double rightBorder, double eps) {
        this(function, new Interval(leftBorder, rightBorder), eps);
    }

    /**
     * Sole constructor
     *
     * @param function function on which to search
     * @param domain   domain of function definition
     * @param eps      epsilon which is used to calculate
     */
    public AbstractParaboloidMinFinder(Function<Double, Double> function, Interval domain, double eps) {
        super(function, domain, eps);
    }

    /**
     * This method provides solution by method implementation
     *
     * @return paraboloid method solution
     */
    abstract ParaboloidSolution calculateParaboloidSolution();

    @Override
    public ParaboloidSolution getParaboloidSolution() {
        if (paraboloidSolution == null) {
            paraboloidSolution = calculateParaboloidSolution();
        }
        return paraboloidSolution;
    }

    public class Parabola {
        private final Function<Double, Double> parabolaFunction;
        private final double pointOfMin;
        final double f1;
        final double f2;
        final double f3;
        public Parabola(double x1, double x2, double x3) {
            this(x1, x2, x3, evaluateFunction(x1), evaluateFunction(x2), evaluateFunction(x3));
        }
        public Parabola(final double x1, final double x2, final double x3, final double f1, final double f2, double f3) {
            this.f1 = f1;
            this.f2 = f2;
            this.f3 = f3;
            final double a1 = (f2 - f1) / (x2 - x1);
            final double a2 = ((f3 - f1) / (x3 - x1) - (f2 - f1) / (x2 - x1)) / (x3 - x2);
            this.parabolaFunction = x -> f1 + a1 * (x - x1) + a2 * (x - x1) * (x - x2);
            this.pointOfMin = (x1 + x2 - a1 / a2) * 0.5;
        }


        public Function<Double, Double> getParabolaFunction() {
            return parabolaFunction;
        }

        public double getPointOfMin() {
            return pointOfMin;
        }
    }
}
