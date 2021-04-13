package com.caucasus.optimization.algos.entities.util;

/**
 * Class represent interval in double numbers
 */
public class Interval {
    private final double leftBorder;
    private final double rightBorder;

    public Interval(double leftBorder, double rightBorder) {
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }

    /**
     * @return left border of interval
     */
    public double getLeftBorder() {
        return leftBorder;
    }

    /**
     * @return right border of interval
     */
    public double getRightBorder() {
        return rightBorder;
    }
}
