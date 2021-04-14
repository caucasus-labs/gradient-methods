package com.caucasus.optimization.ui;

import javafx.scene.control.ToggleButton;

public class ButtonWithMethod {
    private ToggleButton button;
    private Methods method;

    public ButtonWithMethod(ToggleButton button, Methods method) {
        this.button = button;
        this.method = method;
    }

    public ToggleButton getButton() {
        return button;
    }

    public Methods getMethod() {
        return method;
    }
}
