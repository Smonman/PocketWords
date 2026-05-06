package org.sjk.pocketwords.printer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This class provides functionality to print the result either to {@code stdout} or a file.
 *
 * @author Simon Josef Kreuzpointner
 */
public final class Printer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Printer.class);

    private Printer() {}

    public static void print(final List<String> tokens, final Path outputFile) throws IOException {
        if (outputFile != null) {
            Printer.printToFile(tokens, outputFile);
        } else {
            Printer.printToStdOut(tokens);
        }
    }

    private static void printToFile(final List<String> tokens, final Path outputFile) throws IOException {
        try (final BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            for (final String token : tokens) {
                writer.write(token);
                writer.write("\n");
            }
        } catch (final IOException e) {
            LOGGER.error("error while writing to file {}", outputFile, e);
            throw new IOException(e);
        }
    }

    private static void printToStdOut(final List<String> tokens) {
        for (final String token : tokens) {
            System.out.println(token);
        }
    }
}
