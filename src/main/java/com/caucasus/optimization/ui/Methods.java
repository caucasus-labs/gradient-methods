package com.caucasus.optimization.ui;

public enum Methods {
    GRADIENT("Gradient method"),
    STEEPEST_DESCENT("Steepest descent method"),
    CONJUGATE("Conjugate method");

    private final String labelString;

    Methods(String labelString) {
        this.labelString = labelString;
    }

    public String getLabelString() {
        return labelString;
    }
}
