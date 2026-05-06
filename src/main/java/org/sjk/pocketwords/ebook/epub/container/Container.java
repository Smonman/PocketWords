package org.sjk.pocketwords.ebook.epub.container;

import java.nio.file.Path;

/**
 * Represents the container of the EPUB e-book.
 *
 * <p>The container is the file {@code container.xml}, that explains the structure of the e-book contents.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface Container {

    /**
     * Gets the relative path of the e-books root file.
     *
     * @return a relative path to the root file
     */
    Path getRootFile();
}
