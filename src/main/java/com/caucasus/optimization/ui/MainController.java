package com.caucasus.optimization.ui;

import com.caucasus.optimization.algos.entities.minfinder.*;
import com.caucasus.optimization.algos.entities.util.*;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainController {
    @FXML
    private Label methodName;
    @FXML
    private TextField epsTextField;
    @FXML
    private Slider iterationSlider;
    @FXML
    private Label iterationNumberLabel;
    @FXML
    private Label x1Label, x2Label, approxLabel;
    @FXML
    private LineChart<Double, Double> lineChart;
    @FXML
    private ToggleButton gradientButton, steepestDescentButton, conjugateButton;

    private ArrayList<ButtonWithMethod> buttonsWithMethod;

    final QuadraticFunction function = new QuadraticFunction(List.of(2., 3.), List.of(-30., 10.), 0);
    final Domain domain = new Domain(new Vector(List.of(-20., -20.)), new Vector(List.of(20., 20.)));

    final Double DEFAULT_EPS = 0.00001;
    final int PLOT_STEP_COUNT = 100;
    final String NUMBER_FORMAT = "%.7f";

    private GradientSolution gradientSolution, steepestDescentSolution, conjugateSolution;

    private Methods currentMethod = Methods.GRADIENT;

    private int iterationNumber;

    public void initialize() {
        iterationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            iterationNumber = newValue.intValue();
            updateWindow();
        });

        calculateSolutions(DEFAULT_EPS);
        buttonsWithMethod = getButtonsWithMethodList();
        initToggleButtons(buttonsWithMethod);

        gradientButton.fire();
        updateWindow();
    }

    private ArrayList<ButtonWithMethod> getButtonsWithMethodList() {
        return new ArrayList<>(List.of(
                new ButtonWithMethod(gradientButton, Methods.GRADIENT),
                new ButtonWithMethod(steepestDescentButton, Methods.STEEPEST_DESCENT),
                new ButtonWithMethod(conjugateButton, Methods.CONJUGATE)
        ));
    }


    private void initToggleButtons(List<ButtonWithMethod> buttons) {
        makeToggleGroup(buttons.stream().map(ButtonWithMethod::getButton).collect(Collectors.toList()));
        updateButtonsText(buttonsWithMethod);
    }

    private void makeToggleGroup(List<ToggleButton> buttons) {
        ToggleGroup group = new ToggleGroup();
        buttons.forEach(button -> button.setToggleGroup(group));
    }

    private void updateWindow() {
        iterationNumberLabel.setText(Integer.toString(iterationNumber));
        //Point point = getCurrentSolution().getPoints().get(iterationNumber);
        //x1Label.setText(String.format(NUMBER_FORMAT, point.get(0)));
        //x2Label.setText(String.format(NUMBER_FORMAT, point.get(1)));
        //approxLabel.setText(String.format(NUMBER_FORMAT, function.apply(point)));

        clearChart();

//        lineChart.getData().add(functionSeries);
//        if (currentMethod.isNeedPlot()) {
//            Function <Double, Double> parabola = getCurrentParaboloidSolution().getParabolas().get(iterationNumber);
//            if (parabola == null) {
//                drawBorderPoints(left, right, approx);
//            } else {
//                drawParaboloid(parabola, interval);
//                addPointToChart(approx, parabola.apply(approx), "green");
//            }
//        } else {
//            drawBorderPoints(left, right, approx);
//        }
    }

//    private void drawBorderPoints(Double left, Double right, Double approx) {
//        addPointToChart(left, function.apply(left), "blue");
//        addPointToChart(right, function.apply(right), "blue");
//        addPointToChart(approx, function.apply(approx), "green");
//    }

    private void addPointToChart(final double x, final double y, String color) {
//        XYChart.Series<Double, Double> series = new XYChart.Series<>();
//        plotPoint(x, y, series);
//        lineChart.getData().add(series);
//
//        series.nodeProperty().get().setStyle("-fx-stroke-width: 7; -fx-stroke: " + color);
    }

    private XYChart.Series<Double, Double> plotLineSeries(
            final Function<Double, Double> function, final Interval interval) {
        double step = (interval.getRightBorder() - interval.getLeftBorder()) / PLOT_STEP_COUNT;

        final XYChart.Series<Double, Double> series = new XYChart.Series<>();
        for (double x = interval.getLeftBorder(); x < interval.getRightBorder(); x += step) {
            plotPoint(x, function.apply(x), series);
        }
        plotPoint(interval.getRightBorder(), function.apply(interval.getRightBorder()), series);

        return series;
    }

    private void plotPoint(final double x, final double y,
                           final XYChart.Series<Double, Double> series) {
        series.getData().add(new XYChart.Data<>(x, y));
    }

    public void clearChart() {
        lineChart.getData().clear();
    }

    private void calculateSolutions(Double eps) {
        gradientSolution = new Gradient(function, eps, domain).getSolution();
        steepestDescentSolution = new SteepestDescent(function, eps, domain).getSolution();
        conjugateSolution = new Conjugate(function, eps, domain).getSolution();
//        updateButtonsText(buttonsWithMethod);
    }

    private void updateButtonsText(List<ButtonWithMethod> buttonsWithMethod) {
        buttonsWithMethod.forEach(buttonWithMethod ->
                updateButtonText(buttonWithMethod.getButton(), buttonWithMethod.getMethod()));
    }

    private void updateButtonText(ToggleButton button, Methods method) {
        button.setText(method.getLabelString() + "\n" +
                String.format(NUMBER_FORMAT, function.apply(getMethodSolution(method).getEndPoint())));
    }

    @FXML
    private void clickCalculate() {
        double eps;
        try {
            eps = Double.parseDouble(epsTextField.getText());
        } catch (NumberFormatException e) {
            epsTextField.setText("Invalid argument");
            return;
        }
        calculateSolutions(eps);
        setupMethod(currentMethod);
    }

    private GradientSolution getMethodSolution(Methods method) {
        GradientSolution solution;
        switch (method) {
            case GRADIENT: solution = gradientSolution; break;
            case STEEPEST_DESCENT: solution = steepestDescentSolution; break;
            case CONJUGATE: solution = conjugateSolution; break;
            default:
                throw new IllegalStateException("Unexpected method: " + currentMethod);
        }
        return solution;
    }

    private GradientSolution getCurrentSolution() {
        return getMethodSolution(currentMethod);
    }

    private void setupMethod(Methods chosenMethod) {
        currentMethod = chosenMethod;
        iterationSlider.setValue(0);
        iterationSlider.setMax(getCurrentSolution().getPoints().size() - 1);
        methodName.setText(currentMethod.getLabelString());
        updateWindow();
    }

    @FXML
    private void clickGradient() {
        setupMethod(Methods.GRADIENT);
    }

    @FXML
    private void clickSteepestDescent() {
        setupMethod(Methods.STEEPEST_DESCENT);
    }

    @FXML
    private void clickConjugate() {
        setupMethod(Methods.CONJUGATE);
    }
}
