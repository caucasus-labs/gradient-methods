package com.caucasus.optimization.algos.entities.util;

import java.util.ArrayList;
import java.util.function.Function;


/**
 * Class extends all fields of <i>Solution</i> in addition it contains parabola function for each step
 *
 * @see Solution
 */
public class ParaboloidSolution extends Solution {
    private final ArrayList<Function<Double, Double>> parabolas;

    /**
     * Construct new solution containing domains of minimums and values of minimums
     *
     * @param intervals             list of domains on each step
     * @param approximatelyMinimums list of approximate minimums on each step
     * @param parabolas             list of parabola functions
     */
    public ParaboloidSolution(ArrayList<Interval> intervals, ArrayList<Double> approximatelyMinimums, ArrayList<Function<Double, Double>> parabolas) {
        super(intervals, approximatelyMinimums);
        this.parabolas = parabolas;
    }

    /**
     * @return list of parabola functions
     */
    public ArrayList<Function<Double, Double>> getParabolas() {
        return parabolas;
    }
}
