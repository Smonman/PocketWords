package org.sjk.pocketwords.ebook.parser;

import org.sjk.pocketwords.ebook.parser.exception.ParsingException;

/**
 * This represents a parser.
 *
 * @param <T> type of the parser result
 * @param <I> type of the parser input
 * @author Simon Josef Kreuzpointner
 */
public interface Parser<T, I> {

    /**
     * Parses the given instance of type {@code I} and produces an instance of type {@code T}.
     *
     * @param input the input to be parsed
     * @return a new instance of the parsed object
     *
     * @throws ParsingException if parsing is not possible
     */
    T parse(final I input) throws ParsingException;
}
