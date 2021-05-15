package com.caucasus.optimization.statistics.second;

import com.caucasus.optimization.algos.entities.minfinder.SteepestDescent;
import com.caucasus.optimization.algos.entities.util.*;
import com.caucasus.optimization.algos.entities.util.Vector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class to retrieve one-dim minimizer function to iterations statistics.
 *
 * @author nkorzh
 */
public class MethodToSpeedTester {
    static QuadraticFunction function = new QuadraticFunction(List.of(List.of(64., 126.), List.of(126., 64.)), List.of(-10., 30.), 13);
    public static void main(String[] args) {
        // TODO: launch quadratic func at 5 different methods for each gradient method
        final double eps = 1e-5;
        Domain domain = new Domain(new Vector(List.of(-100., -100.)), new Vector(List.of(100., 100.)));
        writeStat("steepestMethod", getSteepestStat(50, domain, eps));
    }

    private static List<Double> getSteepestStat(final int experimentsAmount,
                                                final Domain domain,
                                                final double eps) {
        List<Double> res = new ArrayList<>(List.of(0., 0., 0.));
        Random rand = new Random();
        Method[] methods = new Method[]{Method.FIBONACCI, Method.DICHOTOMY, Method.GOLDEN_SECTION};

        for (int i = 0; i < experimentsAmount; i++) {
            QuadraticFunction func = Utils.generateFunction(
                    rand.nextInt(100) + 1,
                    rand.nextInt(100) + 2);
            for (int j = 0; j < methods.length; j++) {
                res.set(j,
                        res.get(j) + new SteepestDescent(
                                func,
                                eps,
                                domain,
                                methods[j]
                                ).getIterations()
                );
            }
        }
        for (int i = 0; i < res.size(); i++) {
            res.set(i, res.get(i) / experimentsAmount);
        }
        return res;
    }

    private static void writeStat(final String filename, final List<Double> iterations) {
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
        try (final BufferedWriter writer = Files.newBufferedWriter(outputFile, Charset.forName("UTF-8"))) {
            try {
                final List<String> names = List.of(
//                        "Brent",
//                        "Parabola",
                        "Fibonacci",
                        "Dichotomy",
                        "Golden section");
                for (int i = 0; i < Math.min(names.size(), iterations.size()); i++) {
                    writer.write(String.format(Locale.ENGLISH, "%s & %.1f \\\\%n%n", names.get(i), iterations.get(i)));
                }
            } catch (final IOException e) {
                System.err.println("Error printing to file " + outputFile + ": " + e.getMessage());
            }
        } catch (final IOException e) {
            System.err.println("Error creating writer: " + e.getMessage());
        }
    }
}
