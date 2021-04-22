package com.caucasus.optimization.statistics.second;

import com.caucasus.optimization.algos.entities.minfinder.Conjugate;
import com.caucasus.optimization.algos.entities.minfinder.Gradient;
import com.caucasus.optimization.algos.entities.minfinder.SteepestDescent;
import com.caucasus.optimization.algos.entities.util.Domain;
import com.caucasus.optimization.algos.entities.util.QuadraticFunction;
import com.caucasus.optimization.algos.entities.util.Vector;
import com.caucasus.optimization.algos.interfaces.GradientMethod;
import com.caucasus.optimization.statistics.ChartWriter;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Class to retrieve iterations to function dimension and conditionality number to iterations statistics.
 *
 * @author nkorzh
 */
public class DimConToSpeedTester {
    private final static int MAX_DIMENSION = 10000;
    private final static int TESTS_AMOUNT = 5;
    private final static int CONDITIONAL_STEP = 100;
    private final static int MAX_CONDITIONAL_NUMBER = 2200;

    private static class ChartPoint {
        private final int condNumber;
        private final double iterations;

        public ChartPoint(int condNumber, double iterations) {
            this.condNumber = condNumber;
            this.iterations = iterations;
        }

        public double getIterations() {
            return iterations;
        }

        public int getCondNumber() {
            return condNumber;
        }

        @Override
        public String toString() {
            return condNumber + " " + iterations;
        }
    }

    public static void main(String[] args) {
        List<Class<?>> methodTypes = List.of(
                Conjugate.class,
//                SteepestDescent.class
//                ,
                Gradient.class
        );
        // for each method
        List<Thread> threads = new ArrayList<>();
        for (Class<?> methodType : methodTypes) {
            Thread thread = new Thread(() -> {
                List<Thread> treads = new ArrayList<>();
                for (int dimension = 10; dimension <= MAX_DIMENSION; dimension *= 10) {
                    int dim = dimension;
                    Thread tread = new Thread(() -> {
                        List<ChartPoint> iterationsToK = new ArrayList<>();
                        for (int conditionalNumber = 2; conditionalNumber <= MAX_CONDITIONAL_NUMBER;
                             conditionalNumber += CONDITIONAL_STEP) {
                            int iterationsCount = 0;
                            for (int test = 0; test < TESTS_AMOUNT; test++) {
                                final QuadraticFunction function = generateFunction(dim, conditionalNumber);
                                final Domain domain = new Domain(
                                        new Vector(new ArrayList<>(Collections.nCopies(dim, -100.))),
                                        new Vector(new ArrayList<>(Collections.nCopies(dim, 100.)))
                                );
                                final double eps = 1e-6;
                                try {
                                    GradientMethod method = (GradientMethod) methodType
                                            .getDeclaredConstructor(QuadraticFunction.class, Double.class, Domain.class)
                                            .newInstance(function, eps, domain);
                                    iterationsCount += method.getIterations(); //getSolution().getIterations();
                                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                        NoSuchMethodException e) {
                                    System.err.println("Couldn't create instance of " +
                                            methodType.getSimpleName() + ": " + e.getMessage());
                                }
                            }
                            iterationsToK.add(new ChartPoint(conditionalNumber, (double) iterationsCount / TESTS_AMOUNT));
                        }
                        writePoints(methodType.getSimpleName().toLowerCase(), String.valueOf(dim), iterationsToK);
                    });
                    treads.add(tread);
                    tread.start();
                }
                for (var tha : treads) {
                    try {
                        tha.join();
                    } catch (InterruptedException ignored) {
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (var tha : threads) {
            try {
                tha.join();
            } catch (InterruptedException ignored) {
            }
        }


    }

    private static void writePoints(final String methodName, final String chartName, final List<ChartPoint> points) {
        final String fileName = String.join(File.separator,
                ".", "output", "charts", methodName, chartName + ".dat"
        );
        ChartWriter.writeChart(fileName, points, ChartPoint::toString);
    }

    private static QuadraticFunction generateFunction(int dimension, int conditionalNumber) {
        Random random = new Random();
        double first = random.nextDouble();
        double last = first * conditionalNumber;
        List<Double> list = random.doubles(dimension, first, last)
                .sorted().boxed().collect(Collectors.toList());
        List<List<Double>> a = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            a.add(new ArrayList<>(Collections.nCopies(dimension, 0.)));
            a.get(i).set(i, list.get(i));
        }
        a.get(0).set(0, first);
        a.get(dimension - 1).set(dimension - 1, last);
        return new QuadraticFunction(a, new ArrayList<>(Collections.nCopies(dimension, 0.)), 0.);
    }

}
