package com.caucasus.optimization.ui;

import com.caucasus.optimization.algos.entities.minfinder.*;
import com.caucasus.optimization.algos.entities.util.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
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
    private NumberAxis xAxis, yAxis;
    @FXML
    private ToggleButton gradientButton, steepestDescentButton, conjugateButton, showWayButton, showLevelsButton;
    @FXML
    private ToggleButton f1Button, f2Button, f3Button;

    private ArrayList<ButtonWithMethod> buttonsWithMethod;

    private QuadraticFunction function = new QuadraticFunction(List.of(List.of(64., 126.), List.of(126., 64.)), List.of(-10., 30.), 13);
    private final Domain domain = new Domain(new Vector(List.of(-20., -20.)), new Vector(List.of(20., 20.)));
    private final Interval interval = new Interval(domain.getLower().get(0), domain.getUpper().get(0));

    private static final Double DEFAULT_EPS = 1e-5;
    private static final int PLOT_STEP_COUNT = 3000;
    private static final String NUMBER_FORMAT = "%.7f";
    private static final double MIN_BETWEEN_POINTS_DIST = 0.5;

    private GradientSolution gradientSolution, steepestDescentSolution, conjugateSolution;

    private Methods currentMethod;

    private int iterationNumber, functionSeriesSize, currentZoom;
    private final Vector defaultLastPoint = new Vector(List.of(domain.getUpper().get(0) + MIN_BETWEEN_POINTS_DIST * 2,
            domain.getUpper().get(1) + MIN_BETWEEN_POINTS_DIST * 2));
    private Vector lastPoint = defaultLastPoint;

    public void initialize() {
        iterationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            iterationNumber = newValue.intValue();
            drawMethodWay(oldValue.intValue(), newValue.intValue());
            updateWindow();
        });

        calculateSolutions(DEFAULT_EPS);
        buttonsWithMethod = getButtonsWithMethodList();
        initToggleButtons(buttonsWithMethod);
        drawFunctionLevelLines(function);
        gradientButton.fire();
        f1Button.fire();
        updateWindow();

        setAxisBounds(xAxis, domain.getLower().get(0), domain.getUpper().get(0));
        setAxisBounds(yAxis, domain.getLower().get(1), domain.getUpper().get(1));
        setZoom(0);
        drawMethodWay(0, 0);
    }

    void setZoom(int zoomLevel) {
        if (zoomLevel == 0) {
            setAxisBounds(xAxis, domain.getLower().get(0), domain.getUpper().get(0));
            setAxisBounds(yAxis, domain.getLower().get(1), domain.getUpper().get(1));
        }
        else {
            Vector approxMin = getCurrentSolution().getEndPoint();
            Double delta = 10.0 / zoomLevel;
            setAxisBounds(xAxis, approxMin.get(0) - delta, approxMin.get(0) + delta);
            setAxisBounds(yAxis, approxMin.get(1) - delta, approxMin.get(1) + delta);
        }
    }

    private void setAxisBounds(NumberAxis axis, Double lower, Double upper) {
        axis.setLowerBound(lower);
        axis.setUpperBound(upper);
    }

    private void drawFunctionLevelLines(QuadraticFunction function) {
        double levelLineStep = 1.;
        double approxMinimum = function.apply(gradientSolution.getEndPoint());
        boolean levelExist = true;

        int cnt = 0;
        for (double level = approxMinimum + levelLineStep; levelExist && cnt <= 100; level += levelLineStep, cnt++) {
            levelExist = drawFunctionLevelLine(function, level);
            levelLineStep *= 2.5;
        }
        functionSeriesSize = lineChart.getData().size();
    }

    private boolean drawFunctionLevelLine(QuadraticFunction function, Double level) {
        List<Function <Double, Double>> levelFunctionsList = getLevelLinesFunctions(function, level);

        boolean levelExist = false;
        for (Function<Double, Double> levelFunction : levelFunctionsList) {
            XYChart.Series<Double, Double> series = new XYChart.Series<>();

            List<XYChart.Data<Double, Double>> plotLineData = makePlotLineData(levelFunction, interval);
            series.getData().addAll(plotLineData);

            levelExist = levelExist || !plotLineData.isEmpty();
            lineChart.getData().add(series);
            series.nodeProperty().get().setStyle("-fx-stroke-width: 1; -fx-stroke: " + "#B4B4B4");
        }

        return levelExist;
    }

    private List<Function<Double, Double>> getLevelLinesFunctions(QuadraticFunction function, Double level) {
        List<List<Double>> A = function.getInitialA();
        Vector b = function.getB();
        Double c = function.getC();
        Double a12 = A.get(0).get(1);
        Double a11 = A.get(0).get(0);
        Double a22 = A.get(1).get(1);
        Double b1 = b.get(0);
        Double b2 = b.get(1);

        return quadraticEquationSolutions(
                (x -> a22),
                ((Double x) -> a12 * x + b2),
                ((Double x) -> a11 * x * x + b1 * x + c - level));
    }

    private List<Function<Double, Double>> quadraticEquationSolutions(
            Function<Double, Double> a,
            Function<Double, Double> b,
            Function<Double, Double> c) {
        Function<Double, Double> discriminant = (x -> Math.pow(b.apply(x), 2) - 4 * a.apply(x) * c.apply(x));
        return List.of(
                (x -> (-b.apply(x) - Math.sqrt(discriminant.apply(x))) / (2 * a.apply(x))),
                (x -> (-b.apply(x) + Math.sqrt(discriminant.apply(x))) / (2 * a.apply(x)))
        );
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
        makeToggleGroup(List.of(f1Button, f2Button, f3Button));
    }

    private void makeToggleGroup(List<ToggleButton> buttons) {
        ToggleGroup group = new ToggleGroup();
        buttons.forEach(button -> button.setToggleGroup(group));
    }

    private void updateWindow() {
        updateLabels(iterationNumber);
    }

    private void updateLabels(int iterationNumber) {
        iterationNumberLabel.setText(Integer.toString(iterationNumber));
        Vector point = getCurrentSolution().getPoints().get(iterationNumber);
        x1Label.setText(String.format(NUMBER_FORMAT, point.get(0)));
        x2Label.setText(String.format(NUMBER_FORMAT, point.get(1)));
        approxLabel.setText(String.format(NUMBER_FORMAT, function.apply(point)));
    }

    private void drawMethodWay(int oldIterationNumber, int newIterationNumber) {
        if (oldIterationNumber <= newIterationNumber) {
            List<Vector> points = getCurrentSolution().getPoints().subList(oldIterationNumber, newIterationNumber + 1);
            points.forEach(point -> addPointToChart(point, "red"));
        } else {
            clearMethodWay();
            drawMethodWay(0, newIterationNumber);
        }
    }

    private void addPointToChart(Vector point, String color) {
        if (point.dist(lastPoint) > MIN_BETWEEN_POINTS_DIST) {
            XYChart.Series<Double, Double> series = new XYChart.Series<>();
            if (showWayButton.isSelected()) {
                plotPoint(lastPoint.get(0), lastPoint.get(1), series.getData());
            }
            plotPoint(point.get(0), point.get(1), series.getData());
            lineChart.getData().add(series);

            series.nodeProperty().get().setStyle("-fx-stroke-width: 3; -fx-stroke: " + color);
            lastPoint = point;
        }
    }

    private List<XYChart.Data<Double, Double>> makePlotLineData(
            final Function<Double, Double> function, final Interval interval) {
        double step = (interval.getRightBorder() - interval.getLeftBorder()) / PLOT_STEP_COUNT;

        final ArrayList<XYChart.Data<Double, Double>> dataList = new ArrayList<>();
        for (double x = interval.getLeftBorder(); x <= interval.getRightBorder(); x += step) {
            plotPoint(x, function.apply(x), dataList);
        }
        plotPoint(interval.getRightBorder(), function.apply(interval.getRightBorder()), dataList);

        return dataList;
    }

    private void plotPoint(final Double x, final Double y,
                           final List<XYChart.Data<Double, Double>> dataList) {
        if (x >= domain.getLower().get(0) && x <= domain.getUpper().get(0) &&
                y >= domain.getLower().get(1) && y <= domain.getUpper().get(1)) {
            dataList.add(new XYChart.Data<>(x, y));
        }
    }

    private void clearChart() {
        lineChart.getData().clear();
    }

    private void clearMethodWay() {
        lineChart.getData().remove(functionSeriesSize, lineChart.getData().size());
        lastPoint = defaultLastPoint;
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
        clearMethodWay();
        iterationSlider.setValue(0);
        iterationSlider.setMax(getCurrentSolution().getPoints().size() - 1);
        methodName.setText(currentMethod.getLabelString());
        drawMethodWay(0, 0);
        updateWindow();
        setZoom(0);
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

    @FXML
    public void clickShowLevels() {
        for (int i = 0; i < functionSeriesSize; i++) {
            Node node = lineChart.getData().get(i).getNode();
            node.setVisible(!node.isVisible());
        }
    }

    @FXML
    public void clickShowWay() {
        clearMethodWay();
        drawMethodWay(0, iterationNumber);
    }

    @FXML
    public void clickShowLegend() {
        xAxis.setLabel((xAxis.getLabel().equals("") ? "x1" : ""));
        yAxis.setLabel((yAxis.getLabel().equals("") ? "x2" : ""));
    }

    @FXML
    public void clickShowAxis() {
        xAxis.setTickLabelsVisible(!xAxis.isTickLabelsVisible());
        yAxis.setTickLabelsVisible(!yAxis.isTickLabelsVisible());
    }

    public void setupFunction(QuadraticFunction f) {
        function = f;
        calculateSolutions(DEFAULT_EPS);
        clearChart();
        drawFunctionLevelLines(function);
        showLevelsButton.setSelected(true);
        setupMethod(currentMethod);
    }

    @FXML
    public void clickF1() {
        setupFunction(new QuadraticFunction(List.of(List.of(64., 126.), List.of(126., 64.)), List.of(-10., 30.), 13));
    }

    @FXML
    public void clickF2() {
        setupFunction(new QuadraticFunction(List.of(List.of(1., 0.), List.of(0., 1.)), List.of(0., 0.), 10.));
    }

    @FXML
    public void clickF3() {
        setupFunction(new QuadraticFunction(List.of(List.of(1., 2.), List.of(2., 3.)), List.of(4., 5.), 6));
    }

    @FXML
    public void clickZoomMinus() {
        setZoom(currentZoom > 0 ? --currentZoom : 0);
    }

    @FXML
    public void clickZoomPlus() {
        setZoom(currentZoom < 3 ? ++currentZoom : 3);
    }
}
