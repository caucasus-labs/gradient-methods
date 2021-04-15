package com.caucasus.optimization.algos.entities.util;

import java.util.ArrayList;
import java.util.List;

public class Domain {
    private final Vector lower;
    private final Vector upper;

    public Domain(Vector lower, Vector upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public Vector getLower() {
        return lower;
    }

    public Vector getUpper() {
        return upper;
    }

    public Vector middle() {
        List<Double> res = new ArrayList<>();
        for (int i = 0; i < lower.size(); i++) {
            res.add((lower.get(i)  + upper.get(i)) * 0.5);
        }
        return new Vector(res);
    }

    public boolean validate(Vector x) {
        for (int i = 0; i < lower.size(); i++) {
            if (x.get(i) < lower.get(i) || x.get(i) > upper.get(i)) {
                return false;
            }
        }
        return true;
    }
}
