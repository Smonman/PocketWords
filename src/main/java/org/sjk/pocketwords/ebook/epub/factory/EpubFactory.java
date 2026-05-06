package org.sjk.pocketwords.ebook.epub.factory;

import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.sjk.pocketwords.ebook.EBook;
import org.sjk.pocketwords.ebook.epub.parser.EpubParser;
import org.sjk.pocketwords.ebook.parser.exception.ParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class EpubFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpubFactory.class);

    private EpubFactory() {
    }

    public static EBook create(final Path filepath) {
        try {
            final EpubParser parser = new EpubParser();
            return parser.parse(filepath);
        } catch (final ParsingException e) {
            LOGGER.error("cannot create EBook from {}", filepath, e);
            // FIXME: could there be a better exception?
            throw new RuntimeException(e);
        }
    }
}
