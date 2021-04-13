package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.Interval;
import com.caucasus.optimization.algos.entities.util.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

/**
 * A IntervalMinFinder implementation based on Fibonacci minimization method
 *
 * @see com.caucasus.optimization.algos.interfaces.IntervalMinFinder
 */
public class Fibonacci extends AbstractIntervalMinFinder {
    /**
     * Constructs new method immutable object
     *
     * @param function    function on which to search
     * @param leftBorder  left border of domain of function definition
     * @param rightBorder right border of domain of function definition
     * @param eps         epsilon which is used to calculate
     */
    public Fibonacci(Function<Double, Double> function, double leftBorder, double rightBorder, double eps) {
        super(function, leftBorder, rightBorder, eps);
    }

    /**
     * Constructs new method immutable object
     *
     * @param function function on which to search
     * @param domain   domain of function definition
     * @param eps      epsilon which is used to calculate
     */
    public Fibonacci(Function<Double, Double> function, Interval domain, double eps) {
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
        final int numberOfIterations = FibonacciGenerator.findIndexOfGreater((long) ((rightBorder - leftBorder) / getEps()));
        double x1 = leftBorder + calcRatioOfNthFibonacci(numberOfIterations, numberOfIterations + 2) * (rightBorder - leftBorder);
        double x2 = leftBorder + calcRatioOfNthFibonacci(numberOfIterations + 1, numberOfIterations + 2) * (rightBorder - leftBorder);
        double f1 = evaluateFunction(x1);
        double f2 = evaluateFunction(x2);
        for (int i = 0; i < numberOfIterations - 2; i++) {
            if (compare(f1, f2) <= 0) {
                rightBorder = x2;
                x2 = x1;
                f2 = f1;
                x1 = leftBorder + calcRatioOfNthFibonacci(numberOfIterations - i - 3, numberOfIterations - i - 1) * (rightBorder - leftBorder);
                f1 = evaluateFunction(x1);
            } else {
                leftBorder = x1;
                x1 = x2;
                f1 = f2;
                x2 = leftBorder + calcRatioOfNthFibonacci(numberOfIterations - i - 2, numberOfIterations - i - 1) * (rightBorder - leftBorder);
                f2 = evaluateFunction(x2);
            }
            intervals.add(new Interval(leftBorder, rightBorder));
            approximatelyMinimums.add((leftBorder + rightBorder) * 0.5);
        }
        x2 = x1 + getEps();
        f2 = evaluateFunction(x2);
        if (compare(f1, f2) == 0) {
            leftBorder = x1;
        } else {
            rightBorder = x2;
        }
        intervals.add(new Interval(leftBorder, rightBorder));
        approximatelyMinimums.add((leftBorder + rightBorder) * 0.5);
        return new Solution(intervals, approximatelyMinimums);
    }

    private double calcRatioOfNthFibonacci(int numeratorSerial, int denominatorSerial) {
        return ((double) FibonacciGenerator.getNth(numeratorSerial) / (double) FibonacciGenerator.getNth(denominatorSerial));
    }

    static class FibonacciGenerator {
        static final ArrayList<Long> list = generate();

        /**
         * @param n serial number
         * @return nth Fibonacci number
         */
        static public long getNth(int n) {
            return list.get(n);
        }

        // TODO implement indexOfGreater
        static public int findIndexOfGreater(long num) {
            int pos = Collections.binarySearch(list, num);
            return (pos < 0) ? -pos - 1 : pos;
        }

        private static ArrayList<Long> generate() {
            ArrayList<Long> list = new ArrayList<>();
            list.add(1L);
            list.add(1L);
            for (int i = 2; i < 89; i++) {
                list.add(list.get(i - 1) + list.get(i - 2));
            }
            return list;
        }
    }
}
