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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * Class to retrieve iterations to function dimension and conditionality number to iterations statistics.
 *
 * @author nkorzh
 */
public class DimConToSpeedTester {
    private final static int MAX_DIMENSION = 1000;
    private final static int TESTS_PER_POINT = 5;
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
                Conjugate.class
//                ,
//                SteepestDescent.class
//                ,
//                Gradient.class
        );
        // for each method
        List<Thread> threads = new ArrayList<>();
        for (Class<?> methodType : methodTypes) {
            Thread thread = new Thread(() -> {
                ExecutorService workers = Executors.newFixedThreadPool(10);
                ExecutorService dimworkers = Executors.newFixedThreadPool(5);
                final int startDim = 1000;
                CountDownLatch dimensions = new CountDownLatch((int) (Math.log10(MAX_DIMENSION / startDim) + 1));
                for (int dimension = startDim; dimension <= MAX_DIMENSION; dimension *= 10) {
                    final int dim = dimension;
                    dimworkers.submit(() -> {
                        scheduleChart(workers, dim, methodType);
                        System.err.println("Done " + methodType.getSimpleName() + ", dim: " + dim);
                        dimensions.countDown();
                    });
                }
                try {
                    dimensions.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    workers.shutdown();
                    dimworkers.shutdown();
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

    private static void scheduleChart(final ExecutorService workers, final int dim, final Class<?> methodType) {
        final int iterations = MAX_CONDITIONAL_NUMBER / CONDITIONAL_STEP;
        List<ChartPoint> iterationsToK = new ArrayList<>(Collections.nCopies(iterations, null));
        CountDownLatch chartXValues = new CountDownLatch(iterations);

        for (int i = 0; i < iterations; i++) {
            final int iter = i;
            final int finalConditionalNumber = 2 + CONDITIONAL_STEP * i;
            workers.submit(() -> {
                ExecutorService ww = Executors.newFixedThreadPool(TESTS_PER_POINT);
                final AtomicInteger iterationsCount = new AtomicInteger(0);
                final CountDownLatch testsPerMu = new CountDownLatch(TESTS_PER_POINT);
                for (int test = 0; test < TESTS_PER_POINT; test++) {
                    int testNum = test;
                    ww.submit(() -> {
                        iterationsCount.getAndAdd(getIterations(dim, finalConditionalNumber, methodType)); //getSolution().getIterations();
                        testsPerMu.countDown();
                        System.out.println(methodType.getSimpleName() + ": " + iter + " -- " + testNum);
                    });
                }
                try {
                    testsPerMu.await();
                    iterationsToK.set(iter, new ChartPoint(finalConditionalNumber, (double) iterationsCount.get() / TESTS_PER_POINT));
                    System.out.println("Done " + iter + " iter");
                } catch (InterruptedException e) {
                    System.err.println("Interrupted at " + iter);
                    e.printStackTrace();
                }
                chartXValues.countDown();
                ww.shutdown();
            });
        }
        try {
            chartXValues.await();
            writePoints(methodType.getSimpleName().toLowerCase(), String.valueOf(dim), iterationsToK);
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting for chart counting finishes");
        }
    }

    private static int getIterations(final int dim, final int condNumber, final Class<?> methodType ) {
        final QuadraticFunction function = generateFunction(dim, condNumber);
        final Domain domain = new Domain(
                new Vector(new ArrayList<>(Collections.nCopies(dim, -100.))),
                new Vector(new ArrayList<>(Collections.nCopies(dim, 100.)))
        );
        final double eps = 1e-5;
        try {
            GradientMethod method = (GradientMethod) methodType
                    .getDeclaredConstructor(QuadraticFunction.class, Double.class, Domain.class)
                    .newInstance(function, eps, domain);
            return method.getIterations(); //getSolution().getIterations();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            System.err.println("Couldn't create instance of " +
                    methodType.getSimpleName() + ": " + e.getMessage());
        }
        System.err.println("Error counting iterations!");
        return 0;
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
