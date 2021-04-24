package com.caucasus.optimization.ui;

import com.caucasus.optimization.algos.entities.minfinder.*;
import com.caucasus.optimization.algos.entities.util.*;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
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
    private ScatterChart<Double, Double> scatterChart;
    @FXML
    private ToggleButton gradientButton, steepestDescentButton, conjugateButton;

    private ArrayList<ButtonWithMethod> buttonsWithMethod;

    final QuadraticFunction function = new QuadraticFunction(List.of(List.of(64., 126.), List.of(126., 64.)), List.of(-10., 30.), 13);
    final Domain domain = new Domain(new Vector(List.of(-20., -20.)), new Vector(List.of(20., 20.)));

    final Double DEFAULT_EPS = 1e-5;
    final Double DEFAULT_FAST_EPS = 1e-3;
    final int PLOT_STEP_COUNT = 1000;
    final int LEVEL_LINE_COUNT = 10;
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
        drawFunctionLevelLines(function, 10.);
    }

    private void drawFunctionLevelLines(QuadraticFunction function, Double levelLineStep) {
        double approxMinimum = function.apply(getCurrentSolution().getEndPoint());
        boolean levelExist = true;

        for (Double level = approxMinimum; levelExist; level += levelLineStep) {
            levelExist = drawFunctionLevelLine(function, level);
        }
    }

    private boolean drawFunctionLevelLine(QuadraticFunction function, Double level) {
        double plotStep = (domain.getUpper().get(0) - domain.getLower().get(0)) / PLOT_STEP_COUNT;
        boolean pointsExists = false;
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        for (Double x1 = domain.getLower().get(0); x1 <= domain.getUpper().get(0); x1 += plotStep) {
            for (Double x2 = domain.getLower().get(1); x2 <= domain.getUpper().get(1); x2 += plotStep) {
                if (Math.abs(function.apply(new Vector(List.of(x1, x2))) - level) <= 1e-3) {
                    plotPoint(x1, x2, series);
                    pointsExists = true;
                }
            }
        }
        plotPoint(1., 1., series);
        scatterChart.getData().add(series);
        return pointsExists;
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
        Vector point = getCurrentSolution().getPoints().get(iterationNumber);
        x1Label.setText(String.format(NUMBER_FORMAT, point.get(0)));
        x2Label.setText(String.format(NUMBER_FORMAT, point.get(1)));
        approxLabel.setText(String.format(NUMBER_FORMAT, function.apply(point)));


//        clearChart();

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
        scatterChart.getData().clear();
    }

    private void calculateSolutions(Double eps) {
        gradientSolution = new Gradient(function, eps, domain).getSolution();
        steepestDescentSolution = new SteepestDescent(function, eps, domain).getSolution();
        conjugateSolution = new Conjugate(function, eps, domain).getSolution();
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
        return switch (method) {
            case GRADIENT -> gradientSolution;
            case STEEPEST_DESCENT -> steepestDescentSolution;
            case CONJUGATE -> conjugateSolution;
        };
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
