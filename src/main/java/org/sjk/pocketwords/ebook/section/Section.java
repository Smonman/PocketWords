package org.sjk.pocketwords.ebook.section;

/**
 * Represents a section of an e-book.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface Section {

    /**
     * Gets the title of this section.
     *
     * @return the title of this section
     */
    String getTitle();

    /**
     * Gets the text of the section.
     *
     * @return the text
     */
    String getText();
}
