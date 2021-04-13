package com.caucasus.optimization.algos.interfaces;

import com.caucasus.optimization.algos.entities.util.ParaboloidSolution;

/**
 * This interface provide Solution of MinFinder witch based on parabola methods
 *
 * @see MinFinder
 */
public interface ParaboloidMinFinder extends MinFinder {
    /**
     * @return solution of parabola method
     */
    ParaboloidSolution getParaboloidSolution();
}
