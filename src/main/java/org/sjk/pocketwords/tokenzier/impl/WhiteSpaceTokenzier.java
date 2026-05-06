package org.sjk.pocketwords.tokenzier.impl;

import org.sjk.pocketwords.tokenzier.Tokenzier;

import java.util.List;

/**
 * A concrete implementation of {@link Tokenzier}.
 *
 * <p>This tokenizer splits the input string on whitespace characters.
 *
 * @author Simon Josef Kreuzpointner
 */
public class WhiteSpaceTokenzier implements Tokenzier {

    @Override
    public List<String> tokenize(final String input) {
        return List.of(input.trim().split("\\s+"));
    }
}
