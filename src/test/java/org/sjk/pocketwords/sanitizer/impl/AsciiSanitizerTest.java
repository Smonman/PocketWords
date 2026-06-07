package org.sjk.pocketwords.sanitizer.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class AsciiSanitizerTest {

    private static String produceAsciiString(final int lower, final int upper) {
        final StringBuilder sb = new StringBuilder();
        for (int i = lower; i <= upper; i++) {
            sb.append((char) i);
        }
        return sb.toString();
    }

    private static Stream<Arguments> sanitize_withAsciiCharacters_doestNotRemove() {
        return Stream.of(Arguments.of(0, 31), // ASCII control characters
                         Arguments.of(32, 127) // ASCII printable characters
        );
    }

    @ParameterizedTest
    @MethodSource
    void sanitize_withAsciiCharacters_doestNotRemove(final int lower, final int upper) {
        final String expected = produceAsciiString(lower, upper);

        final String actual = new AsciiSanitizer().sanitize(expected);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void sanitize_withExtendedAsciiCharacters_removes() {
        final String input = produceAsciiString(160, 255); // ASCII extended characters (ISO-8859-1 compliant)

        final String actual = new AsciiSanitizer().sanitize(input);

        Assertions.assertEquals("", actual);
    }
}
