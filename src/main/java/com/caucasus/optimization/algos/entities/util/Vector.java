package com.caucasus.optimization.algos.entities.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Vector {
    private final List<Double> values;

    public Vector(List<Double> values) {
        this.values = values;
    }

    public double scalar(Vector other) {
        double res = 0;
        for (int i = 0; i < this.size(); i++) {
            res += this.get(i) * other.get(i);
        }
        return res;
    }

    public Vector mul(double d) {
        return new Vector(values.stream().map(i -> i * d).collect(Collectors.toList()));
    }

    public double dist(Vector other) {
        return this.mul(-1.).add(other).length();
    }

    public Vector add(Vector other) {
        List<Double> res = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            res.add(this.get(i) + other.get(i));
        }
        return new Vector(res);
    }

    public double length() {
        double res = 0;
        for (double i : values) {
            res += i * i;
        }
        return Math.sqrt(res);
    }

    public int size() {
        return values.size();
    }

    public Double get(int i) {
        return values.get(i);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "values=" + values +
                '}';
    }
}
