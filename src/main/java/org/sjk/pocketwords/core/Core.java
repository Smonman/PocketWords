package org.sjk.pocketwords.core;

import org.sjk.pocketwords.ebook.EBook;
import org.sjk.pocketwords.ebook.factory.EBookFactory;
import org.sjk.pocketwords.printer.Printer;
import org.sjk.pocketwords.ebook.section.Section;
import org.sjk.pocketwords.tokenzier.impl.WhiteSpaceTokenzier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public final class Core implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Core.class);
    private static final Core INSTANCE = new Core();

    private Path inputFilePath;
    private Path outputFilePath;

    private Core() {
    }

    public static Core getInstance() {
        return Core.INSTANCE;
    }

    public void setInputFilePath(final Path inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public void setOutputFilePath(final Path outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    @Override
    public void run() {
        final EBook eBook = EBookFactory.create(inputFilePath);
        final String text = eBook.getSections().stream().map(Section::getText).collect(Collectors.joining("\n\n\n"));
        final List<String> tokens = new WhiteSpaceTokenzier().tokenize(text);
        // final List<String> filteredTokens = Filterer.filter(tokens, new SingleCharFilter());
        try {
            Printer.print(tokens, outputFilePath);
        } catch (final IOException e) {
            LOGGER.error("Cannot print result", e);
            throw new RuntimeException(e);
        }
    }
}
