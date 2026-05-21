package org.sjk.pocketwords.printer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class PrinterTest {

    @Test
    void print_noFilePath_usesStdOut() throws IOException {
        // redirect System.out to buffer for this test
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os));

        final List<String> tokens = List.of("a", "b");
        Printer.print(tokens, null);

        os.flush();
        final String actual = os.toString();

        Assertions.assertEquals("a\nb\n", actual);
    }

    @Nested
    class WithFilePath {

        @TempDir
        private Path tempDir;

        @Test
        void print_withFilePath_writesToFile() throws IOException {
            final Path file = tempDir.resolve("file.txt");
            final List<String> tokens = List.of("a", "b");

            Printer.print(tokens, file);

            Assertions.assertTrue(Files.exists(file));
            Assertions.assertIterableEquals(tokens, Files.readAllLines(file));
        }

        @Test
        void print_withFilePath_noTokens_writesNothing() throws IOException {
            final Path file = tempDir.resolve("file.txt");
            final List<String> tokens = List.of();

            Printer.print(tokens, file);

            Assertions.assertTrue(Files.exists(file));
            Assertions.assertIterableEquals(tokens, Files.readAllLines(file));
        }

        @Test
        void print_withFilePath_fileDoesNotExists_throwsIoException() {
            final Path file = Path.of("I/dont/exist.txt");
            final List<String> tokens = List.of();

            Assertions.assertThrows(IOException.class, () -> Printer.print(tokens, file));
        }
    }
}
