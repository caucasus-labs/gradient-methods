package com.caucasus.optimization.statistics.second;

import com.caucasus.optimization.algos.entities.minfinder.SteepestDescent;
import com.caucasus.optimization.algos.entities.util.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Class to retrieve one-dim minimizer function to iterations statistics.
 *
 * @author nkorzh
 */
public class MethodToSpeedTester {
    public static void main(String[] args) {
        // TODO: launch quadratic func at 5 different methods for each gradient method
        final double eps = 1e-5;
        Domain domain = new Domain(new Vector(List.of(-20., -20.)), new Vector(List.of(20., 20.)));
        QuadraticFunction function = new QuadraticFunction(List.of(List.of(64., 126.), List.of(126., 64.)), List.of(-10., 30.), 13);
        writeStat("steepestMethod", getSteepestStat(function, domain, eps));
    }

    private static List<Integer> getSteepestStat(final QuadraticFunction function,
                                                 final Domain domain,
                                                 final double eps) {
        GradientSolution fS = new SteepestDescent(function, eps, domain, Method.FIBONACCI).getSolution();
        GradientSolution dS = new SteepestDescent(function, eps, domain, Method.DICHOTOMY).getSolution();
        GradientSolution gS = new SteepestDescent(function, eps, domain, Method.GOLDEN_SECTION).getSolution();
        // TODO: launch steepest method with:
        //  brent, parabola, fibonacci, dichotomy, gold section and
        //  push getIterations to list 
        return List.of(fS.getIterations(), dS.getIterations(), gS.getIterations());
    }

    private static void writeStat(final String filename, final List<Integer> iterations) {
        final String folderName = "." + File.separator + "output" + File.separator + "tables" + File.separator;
        final Path outputFile;
        try {
            outputFile = Paths.get(folderName + filename + ".tex");
        } catch (final InvalidPathException e) {
            System.err.println("Invalid path: " + e.getMessage());
            return;
        }
        if (outputFile.getParent() != null) {
            try {
                Files.createDirectories(outputFile.getParent());
            } catch (final IOException e) {
                System.err.println("Cannot create parent directories for output file: " + e.getMessage());
            }
        }
        try (final BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            try {
                final List<String> names = List.of("Brent", "Parabola", "Fibonacci",
                        "Dichotomy", "Golden section");
                for (int i = 0; i < Math.min(names.size(), iterations.size()); i++) {
                    writer.write(String.format("%s & %d \\\\%n%n", names.get(i), iterations.get(i)));
                }
            } catch (final IOException e) {
                System.err.println("Error printing to file " + outputFile + ": " + e.getMessage());
            }
        } catch (final IOException e) {
            System.err.println("Error creating writer: " + e.getMessage());
        }
    }
}
