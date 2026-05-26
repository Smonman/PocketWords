package org.sjk.pocketwords.ebook.factory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.sjk.pocketwords.ebook.epub.factory.EpubFactory;

import java.nio.file.Path;

class EBookFactoryTest {

    private MockedStatic<EpubFactory> epubFactory;

    @BeforeEach
    void setUp() {
        this.epubFactory = Mockito.mockStatic(EpubFactory.class);
    }

    @AfterEach
    void tearDown() {
        this.epubFactory.close();
    }

    @ParameterizedTest
    @ValueSource(strings = { "epub" })
    void create_withSupportedExtension_doesNotThrow(final String extension) {
        final Path input = Path.of("file/to/ebook.%s".formatted(extension));
        Assertions.assertDoesNotThrow(() -> EBookFactory.create(input));
    }

    @Test
    void create_withUnsupportedExtension_throwsIllegalArgumentException() {
        final Path input = Path.of("file/to/ebook.xxx");
        Assertions.assertThrows(IllegalArgumentException.class, () -> EBookFactory.create(input));
    }
}
