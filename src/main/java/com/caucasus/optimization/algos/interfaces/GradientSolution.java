package com.caucasus.optimization.algos.interfaces;

import java.util.List;

class GradientSolution {
    public final List<Point> points;

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
