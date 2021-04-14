package com.caucasus.optimization.algos.entities.minfinder;

import com.caucasus.optimization.algos.entities.util.QuadraticFunction;
import com.caucasus.optimization.algos.entities.util.Vector;
import com.caucasus.optimization.algos.interfaces.GradientMethod;
import com.caucasus.optimization.algos.entities.util.GradientSolution;

import java.util.ArrayList;
import java.util.Collections;

public class Gradient implements GradientMethod {
    private final QuadraticFunction function;
    private final Double eps;

    public Gradient(QuadraticFunction function, Double eps) {
        this.function = function;
        this.eps = eps;
    }

    @Override
    public GradientSolution getSolution() {
        Vector x = new Vector(new ArrayList<>(Collections.nCopies(function.size(), 1.)));
        Vector antigradient = function.gradient(x).mul(-1.);
        return null;
    }
}
