package org.sjk.pocketwords.ebook.epub.parser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.sjk.pocketwords.ebook.epub.paragraph.Paragraph;
import org.sjk.pocketwords.ebook.parser.exception.ParsingException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.util.stream.Stream;

class ParagraphParserTest {

    private static XMLInputFactory xmlInputFactory;
    private XMLEventReader reader;

    @BeforeAll
    static void beforeAll() {
        xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    }

    static Stream<Arguments> paragraphXml() {
        return Stream.of(Arguments.of("<p>test</p>", "test"),
                         Arguments.of("<p>Test</p>", "Test"),
                         Arguments.of("<p>Test Test</p>", "Test Test"),
                         Arguments.of("<p>&amp;</p>", "&"),
                         Arguments.of("<p>Test <em>emph</em></p>", "Test emph"),
                         Arguments.of("<p></p>", ""),
                         Arguments.of("<p>abc\ndef</p>", "abc def"),
                         Arguments.of("<p class=\"text\">abc</p>", "abc"));
    }

    @ParameterizedTest
    @MethodSource("paragraphXml")
    void parse(final String input, final String expected) throws ParsingException, XMLStreamException {
        this.reader = xmlInputFactory.createXMLEventReader(new ByteArrayInputStream(input.getBytes()));

        final ParagraphParser parser = new ParagraphParser();
        final Paragraph actual = parser.parse(this.reader);
        Assertions.assertEquals(expected, actual.getText());
    }

    @Test
    void parse_withEmptyInput() throws ParsingException, XMLStreamException {
        this.reader = xmlInputFactory.createXMLEventReader(new ByteArrayInputStream("<div></div>".getBytes()));
        this.reader.nextTag();

        final ParagraphParser parser = new ParagraphParser();
        final Paragraph actual = parser.parse(this.reader);
        Assertions.assertEquals("", actual.getText());
    }

    @ParameterizedTest
    @ValueSource(strings = { "<p><br>", "<p></html>", "<p>&", "<p>" })
    void parse_withInvalidXml_throwsParsingException(final String input) throws XMLStreamException {
        this.reader = xmlInputFactory.createXMLEventReader(new ByteArrayInputStream(input.getBytes()));

        final ParagraphParser parser = new ParagraphParser();
        Assertions.assertThrows(ParsingException.class, () -> parser.parse(this.reader));
    }

    @AfterEach
    void tearDown() throws XMLStreamException {
        if (this.reader != null) {
            this.reader.close();
        }
    }
}
