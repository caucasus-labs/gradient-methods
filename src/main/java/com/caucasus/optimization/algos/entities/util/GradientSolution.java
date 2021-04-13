package com.caucasus.optimization.algos.entities.util;

import java.util.List;

public class GradientSolution {
    private final List<Point> points;

    public GradientSolution(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Point getEndPoint() {
        return points.get(points.size() - 1);
    }

}
