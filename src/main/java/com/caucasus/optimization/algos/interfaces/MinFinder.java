package com.caucasus.optimization.algos.interfaces;

import java.util.function.Function;

/**
 * The root interface in min finder hierarchy. A MinFinder represent method to find minimum on unimodal function.
 */
public interface MinFinder {
    /**
     * @return function on which to search
     */
    Function<Double, Double> getFunction();

    /**
     * @return left border of function definition
     */
    double getLeftBorder();

    /**
     * @return right border of function definition
     */
    double getRightBorder();

    /**
     * @return epsilon which is used to calculate
     */
    double getEps();

    /**
     *
     * @param x argument of function
     * @return result of function evaluation
     */
    double evaluateFunction(double x);

    /**
     *
     * @return count of evaluations of inner function
     */
    int getCountOfEvaluations();

}
