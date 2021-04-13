package com.caucasus.optimization.algos.entities.util;

import java.util.ArrayList;


/**
 * Class contains all steps of finding minimum of function
 * with the domain of definition of the minimum, approximate value of minimum on each step.
 */
public class Solution {
    private final ArrayList<Interval> intervals;
    private final ArrayList<Double> approximatelyMinimums;

    /**
     * Construct new solution containing domains of minimums and values of minimums
     *
     * @param intervals             list of domains on each step
     * @param approximatelyMinimums list of approximate minimums on each step
     */
    public Solution(ArrayList<Interval> intervals, ArrayList<Double> approximatelyMinimums) {
        this.intervals = intervals;
        this.approximatelyMinimums = approximatelyMinimums;
    }

    /**
     * @return list of domain of the minimum on each step
     */
    public ArrayList<Interval> getIntervals() {
        return intervals;
    }

    /**
     * @return minimum value after all steps
     */
    public double getEndPoint() {
        return approximatelyMinimums.get(approximatelyMinimums.size() - 1);
    }

    /**
     * @return list of approximate minimums on each step
     */
    public ArrayList<Double> getApproximatelyMinimums() {
        return approximatelyMinimums;
    }
}
