package org.sjk.pocketwords.tokenzier.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.sjk.pocketwords.tokenzier.Tokenzier;

import java.util.List;
import java.util.stream.Stream;

class WhiteSpaceTokenzierTest {

    static Stream<Arguments> tokenize() {
        return Stream.of(Arguments.of("ab", List.of("ab")),
                         Arguments.of("a b", List.of("a", "b")),
                         Arguments.of("a  b", List.of("a", "b")),
                         Arguments.of("a\nb", List.of("a", "b")),
                         Arguments.of("a\n\nb", List.of("a", "b")),
                         Arguments.of("a\tb", List.of("a", "b")),
                         Arguments.of("a\t\tb", List.of("a", "b")));
    }

    @ParameterizedTest
    @MethodSource
    void tokenize(final String input, final List<String> expected) {
        final Tokenzier tokenzier = new WhiteSpaceTokenzier();
        final List<String> actual = tokenzier.tokenize(input);

        Assertions.assertIterableEquals(expected, actual);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "  ", "\n", "\n\n", "\t", "\t\t" })
    void tokenize_emptyBlankNullString_returnsEmptyList(final String input) {
        final Tokenzier tokenzier = new WhiteSpaceTokenzier();
        final List<String> actual = tokenzier.tokenize(input);

        Assertions.assertIterableEquals(List.of(), actual);
    }
}
