package com.caucasus.optimization;

import com.caucasus.optimization.algos.entities.minfinder.*;
import com.caucasus.optimization.algos.entities.util.Interval;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;

/**
 * @author nkorzh
 * Single-launch class used to obtain the dependency between
 * minFinder method steps amount and epsilon.
 */
public class EpsDependencyChart {
    private static String folderName;

    public static void main(String[] args) {
        final Interval borders = new Interval(0, 1);

        final Function<Double, Double> function = x -> Math.exp(3.0D * x) + 5 * Math.exp(-2.0D * x);
        folderName = "." + File.separator + "output" + File.separator + "charts" + File.separator;

        buildDependencyChart(eps -> getStepsAmount(new Dichotomy(function, borders, eps)),
                Dichotomy.class.getSimpleName());
        buildDependencyChart(eps -> getStepsAmount(new Fibonacci(function, borders, eps)),
                Fibonacci.class.getSimpleName());
        buildDependencyChart(eps -> getStepsAmount(new GoldenSection(function, borders, eps)),
                GoldenSection.class.getSimpleName());
//        buildDependencyChart(eps -> getStepsAmount(new Brent(function, borders, eps)),
//                Brent.class.getSimpleName());
        buildDependencyChart(eps ->
                        new Paraboloid(function, borders, eps)
                                .getParaboloidSolution()
                                .getApproximatelyMinimums()
                                .size(),
                Paraboloid.class.getSimpleName());
    }

    private static void buildDependencyChart(final DoubleToIntFunction getStepsAmount,
                                             final String methodName) {
        final Path outputFile;
        try {
            outputFile = Paths.get(folderName + methodName + ".dat");
            if (outputFile.getParent() != null) {
                Files.createDirectories(outputFile.getParent());
            }
        } catch (final IOException e) {
            System.err.println("Cannot create parent directories for output file: " + e.getMessage());
            return;
        } catch (final InvalidPathException e) {
            System.err.println("Invalid path: " + folderName + methodName + ".dat");
            return;
        }
        try (final BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            writer.write("x y");
            writer.newLine();
            for (int power = 1; power <= 10; power++) {
                writer.write(String.format("%d %d%n", -power, getStepsAmount.applyAsInt(Math.pow(10, -power))));
            }
        } catch (final IOException e) {
            System.err.println("Error creating writer for file " + outputFile + ": " + e.getMessage());
        }
    }

    private static int getStepsAmount(AbstractIntervalMinFinder finder) {
        return finder.getSolution().getApproximatelyMinimums().size();
    }
}
