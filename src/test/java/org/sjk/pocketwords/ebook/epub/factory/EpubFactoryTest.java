package org.sjk.pocketwords.ebook.epub.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.sjk.pocketwords.ebook.epub.parser.EpubParser;
import org.sjk.pocketwords.ebook.factory.FactoryCreationException;
import org.sjk.pocketwords.ebook.parser.exception.ParsingException;

import java.nio.file.Path;

class EpubFactoryTest {

    @Test
    void create_doesNotThrow() {
        try (final MockedConstruction<EpubParser> epubParser = Mockito.mockConstruction(EpubParser.class)) {
            final Path input = Path.of("path/to/ebook.epub");
            Assertions.assertDoesNotThrow(() -> EpubFactory.create(input));
            Assertions.assertEquals(1, epubParser.constructed().size());
        }
    }

    @Test
    void create_withInvalidPath_throwsFactoryCreationException() {
        try (final var _ = Mockito.mockConstruction(EpubParser.class,
                                                    (mock, _) -> Mockito.doThrow(ParsingException.class)
                                                                        .when(mock)
                                                                        .parse(Mockito.any()))) {
            final Path input = Path.of("path/to/ebook.epub");
            Assertions.assertThrows(FactoryCreationException.class, () -> EpubFactory.create(input));
        }
    }
}
