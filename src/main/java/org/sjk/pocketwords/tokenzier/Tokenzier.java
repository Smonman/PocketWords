package org.sjk.pocketwords.tokenzier;

import java.util.List;

/**
 * This represents a tokenizer.
 *
 * <p>A tokenzier produces a sequence of tokens from a given string.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface Tokenzier {

    /**
     * Tokenizes the given string.
     *
     * @param input the string to tokenize
     * @return a sequence of tokens
     */
    List<String> tokenize(final String input);
}
