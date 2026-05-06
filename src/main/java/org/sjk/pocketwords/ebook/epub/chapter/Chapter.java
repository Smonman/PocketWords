package org.sjk.pocketwords.ebook.epub.chapter;

import org.sjk.pocketwords.ebook.epub.paragraph.Paragraph;

import java.util.List;

/**
 * Represents a single chapter of an EPUB e-book.
 *
 * <p>A chapter is usually a single {@code .xhtml} file of the book.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface Chapter {

    /**
     * Gets the title of this chapter.
     *
     * @return the title of this chapter
     */
    String getTitle();

    /**
     * Gets all paragraphs of this chapter in order.
     *
     * @return an {@code Iterable} of paragraphs
     */
    List<Paragraph> getParagraphs();
}
