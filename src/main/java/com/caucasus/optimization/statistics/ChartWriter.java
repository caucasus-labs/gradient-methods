package com.caucasus.optimization.statistics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

public class ChartWriter {
    public static <T> void writeChart(final String fileName,
                                      final List<? extends T> points,
                                      final Function<? super T, String> getLine) {
        final Path outputFile;
        try {
            outputFile = Paths.get(fileName);
            Files.createDirectories(outputFile.getParent());
        } catch (final InvalidPathException e) {
            System.err.println("Invalid path: " + e.getMessage());
            return;
        } catch (final IOException e) {
            System.err.println("Cannot create parent directories for output file: " + e.getMessage());
            return;
        }
        try (final BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            try {
                writer.write("x y");
                writer.newLine();
                for (T point : points) {
                    writer.write(getLine.apply(point));
                    writer.newLine();
                }
            } catch (final IOException e) {
                System.err.println("Error printing to file " + outputFile + ": " + e.getMessage());
            }
        } catch (final IOException e) {
            System.err.println("Error creating writer: " + e.getMessage());
        }
    }
}
