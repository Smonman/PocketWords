package org.sjk.pocketwords.ebook.epub.opf;

import java.nio.file.Path;
import java.util.List;

/**
 * This represents the OPF package of an EPUB e-book.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface Opf {

    String getIdentifier();

    String getTitle();

    List<String> getAuthors();

    List<Path> getChapterFiles();
}
