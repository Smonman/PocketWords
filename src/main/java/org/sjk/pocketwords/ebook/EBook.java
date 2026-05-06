package org.sjk.pocketwords.ebook;

import org.sjk.pocketwords.ebook.section.Section;

import java.util.List;

/**
 * Represents an e-book.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface EBook {

    /**
     * Gets a unique identifier for this e-book.
     *
     * @return a unique identifier
     */
    String getIdentifier();

    /**
     * Gets the title of this e-book.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Gets all authors of this e-book in order.
     *
     * @return an {@code Iterable} of authors
     */
    List<String> getAuthors();

    /**
     * Gets all sections of this e-book in order.
     *
     * @return an {@code Iterable} of sections
     */
    List<Section> getSections();
}
