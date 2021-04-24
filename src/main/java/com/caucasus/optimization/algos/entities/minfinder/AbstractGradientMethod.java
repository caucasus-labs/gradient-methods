package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.GradientSolution;
import com.caucasus.optimization.algos.entities.util.Vector;
import com.caucasus.optimization.algos.interfaces.GradientMethod;

import java.util.List;

public abstract class AbstractGradientMethod implements GradientMethod {
    GradientSolution solution;
    protected int iterations;
    protected boolean isCounted;

    {
        solution = null;
        iterations = 0;
        isCounted = false;
    }

    public abstract GradientSolution getSolution(boolean saveIterations);

    @Override
    public GradientSolution getSolution() {
        if (isCounted) {
            return solution;
        }
        return getSolution(true); // counts everything
    }

    @Override
    public int getIterations() {
        if (isCounted) {
            return iterations;
        }
        solution = getSolution(false); // lazy by default
        return iterations;
    }

    protected void savePoint(Vector newPoint, List<Vector> points) {
        points.add(newPoint);
    }

    protected void saveValue(Double newVal, List<Double> values) {
        values.add(newVal);
    }
}
