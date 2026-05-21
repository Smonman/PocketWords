package org.sjk.pocketwords.printer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

    private Printer() {
    }

    /**
     * Prints the given tokens to the given output file.
     *
     * <p>If the output file path is {@code null}, then {@link System#out} is used as the output target.
     *
     * @param tokens     a list of tokens to print
     * @param outputFile a file to print to, or {@code null} to use {@link System#out}
     * @throws IOException if an error occurs while printing
     */
    @SuppressWarnings({ "java:S106" })
    public static void print(final List<String> tokens, final Path outputFile) throws IOException {
        try {
            if (outputFile != null) {
                LOGGER.info("Writing printer output to {}", outputFile);
                Printer.printToWriter(tokens, Files.newBufferedWriter(outputFile));
            } else {
                LOGGER.info("Writing printer output to standard out");
                Printer.printToWriter(tokens, new OutputStreamWriter(System.out));
            }
        } catch (final IOException e) {
            throw new IOException("Error while writing output", e);
        }
    }

    private static void printToWriter(final List<String> tokens, final Writer writer) throws IOException {
        try (final BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (final String token : tokens) {
                bufferedWriter.write(token);
                bufferedWriter.write("\n");
            }
        }
    }
}
