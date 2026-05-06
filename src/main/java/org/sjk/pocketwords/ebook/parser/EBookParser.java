package org.sjk.pocketwords.ebook.parser;

import org.sjk.pocketwords.ebook.EBook;

import java.nio.file.Path;

/**
 * A specialized {@link Parser} that parses {@link EBook} from files.
 *
 * @param <T> the type of the e-book
 * @author Simon Josef Kreuzpointner
 */
public interface EBookParser<T extends EBook> extends Parser<T, Path> {

}
