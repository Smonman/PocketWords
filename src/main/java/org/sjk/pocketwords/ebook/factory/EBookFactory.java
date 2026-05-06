package org.sjk.pocketwords.ebook.factory;

import org.apache.commons.io.FilenameUtils;
import org.sjk.pocketwords.ebook.EBook;
import org.sjk.pocketwords.ebook.epub.factory.EpubFactory;

import java.nio.file.Path;

/**
 * An e-book factory.
 *
 * @author Simon Josef Kreuzpointner
 */
public class EBookFactory {

    private EBookFactory() {
    }

    /**
     * Creates a new instance of an e-book, based on its file.
     *
     * @param filepath the path to the e-book file
     * @return a new e-book
     */
    public static EBook create(final Path filepath) {
        final String extension = FilenameUtils.getExtension(filepath.toString());
        if (extension.equals("epub")) {
            return EpubFactory.create(filepath);
        }
        throw new IllegalArgumentException("Unsupported extension: " + extension);
    }
}
