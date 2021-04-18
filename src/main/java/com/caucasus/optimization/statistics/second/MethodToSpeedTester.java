package com.caucasus.optimization.statistics.second;

import com.caucasus.optimization.algos.entities.util.Domain;
import com.caucasus.optimization.algos.entities.util.QuadraticFunction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/**
 * Class to retrieve one-dim minimizer function to iterations statistics.
 *
 * @author nkorzh
 */
public class MethodToSpeedTester {
    public static void main(String[] args) {
        // TODO: launch quadratic func at 5 different methods for each gradient method
//        final double eps = 1e-5;
//        final Domain domain = new Domain();
//        QuadraticFunction function = new QuadraticFunction();
//        writeStat("steepest_method_to_speed", getSteepestStat());
    }

    private static List<Integer> getSteepestStat(final QuadraticFunction function,
                                         final Domain domain,
                                         final double eps) {
        // TODO: launch steepest method with:
        //  brent, parabola, fibonacci, dichotomy, gold section and
        //  push getIterations to list 
        return List.of();
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
        try (final BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
            Locale.setDefault(Locale.US);
            try {
                final List<String> names = List.of("Метод Брента", "Метод парабол", "Метод Фибоначчи",
                        "Метод Дихотомии", "Метод Золотого сечения");
                for (int i = 0; i < 5; i++) {
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
