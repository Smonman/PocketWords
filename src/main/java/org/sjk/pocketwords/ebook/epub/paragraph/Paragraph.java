package org.sjk.pocketwords.ebook.epub.paragraph;

import org.sjk.pocketwords.ebook.epub.chapter.Chapter;

/**
 * This represents a single paragraph of a {@link Chapter}.
 *
 * <p>A paragraph is declared by the {@code <p>} tags in a chapter.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface Paragraph {

    /**
     * Gets the text of this paragraph.
     *
     * @return the text
     */
    String getText();
}
