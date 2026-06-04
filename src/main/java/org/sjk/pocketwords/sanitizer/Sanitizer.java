package org.sjk.pocketwords.sanitizer;

/**
 * This represents a sanitizer.
 *
 * <p>A sanitizer removes or replaces certain parts of the input with a <em>sanitized</em> equivalent.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface Sanitizer {

    /**
     * Sanitizes the given input string.
     *
     * @param input the string to sanitize
     * @return a sanitized equivalent output
     */
    String sanitize(final String input);
}
