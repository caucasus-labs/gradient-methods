package com.caucasus.optimization.algos.interfaces;


import com.caucasus.optimization.algos.entities.util.Solution;

/**
 * This interface provide Solution of MinFinder witch based on intervals methods
 *
 * @see MinFinder
 */
public interface IntervalMinFinder extends MinFinder {
    /**
     * @return solution of interval method
     */
    Solution getSolution();
}
