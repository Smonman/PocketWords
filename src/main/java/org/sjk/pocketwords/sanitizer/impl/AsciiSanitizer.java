package org.sjk.pocketwords.sanitizer.impl;

import org.sjk.pocketwords.sanitizer.Sanitizer;

/**
 * A concrete implementation of {@link Sanitizer}.
 *
 * <p>This sanitizer removes any non-ASCII characters from the input string. This sanitizer also removes ASCII
 * characters from the extended character set. (ASCII code > 127)
 *
 * @author Simon Josef Kreuzpointner
 */
public class AsciiSanitizer implements Sanitizer {

    @Override
    public String sanitize(final String input) {
        final StringBuilder sb = new StringBuilder(input.length());
        for (final char c : input.toCharArray()) {
            if (c <= '\u007F') {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
