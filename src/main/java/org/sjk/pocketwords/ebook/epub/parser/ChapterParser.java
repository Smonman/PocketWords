package org.sjk.pocketwords.ebook.epub.parser;

import org.sjk.pocketwords.ebook.epub.chapter.Chapter;
import org.sjk.pocketwords.ebook.epub.chapter.impl.ChapterImpl;
import org.sjk.pocketwords.ebook.epub.paragraph.Paragraph;
import org.sjk.pocketwords.ebook.parser.Parser;
import org.sjk.pocketwords.ebook.parser.exception.ParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A concrete implementation of {@link Parser} for {@link Chapter}.
 *
 * @author Simon Josef Kreuzpointner
 */
public class ChapterParser implements Parser<Chapter, Path> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChapterParser.class);
    private static final String BODY_ELEMENT_NAME = "body";
    private static final String P_ELEMENT_NAME = "p";
    private static final String TYPE_ATTRIBUTE_NAME = "type";
    private static final String EPUB_NS_URI = "http://www.idpf.org/2007/ops";
    private static final String TITLE_ATTRIBUTE_NAME = "title";
    final ParagraphParser paragraphParser = new ParagraphParser();

    private static void parseChapterTitle(final ChapterImpl.Builder builder, final StartElement startElement) {
        final Attribute typeAttribute =
            startElement.getAttributeByName(new QName(EPUB_NS_URI, TYPE_ATTRIBUTE_NAME));
        if (typeAttribute != null) {
            // parse epub:type attribute
            // see https://www.w3.org/TR/epub-33/#sec-xhtml-structural-semantics
            final String typeAttributeValue = typeAttribute.getValue();
            if (typeAttributeValue.equals("chapter")) {
                final String title = startElement.getAttributeByName(new QName(TITLE_ATTRIBUTE_NAME)).getValue();
                builder.title(title);
            }
        }
    }

    private void parseBodyElement(final XMLEventReader reader, final ChapterImpl.Builder builder)
        throws XMLStreamException, ParsingException {
        final List<Paragraph> paragraphs = new ArrayList<>();
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                final StartElement startElement = event.asStartElement();
                final String startElementName = startElement.getName().getLocalPart();
                ChapterParser.parseChapterTitle(builder, startElement);
                if (startElementName.equals(P_ELEMENT_NAME)) {
                    this.parseParagraph(reader, paragraphs);
                }
            } else if (event.isEndElement()) {
                final EndElement endElement = event.asEndElement();
                final String endElementName = endElement.getName().getLocalPart();
                if (endElementName.equals(BODY_ELEMENT_NAME)) {
                    // done parsing <body> block
                    builder.paragraphs(paragraphs);
                    break;
                }
            }
        }
    }

    private void parseParagraph(final XMLEventReader reader, final List<Paragraph> paragraphs) throws ParsingException {
        final Paragraph paragraph = this.paragraphParser.parse(reader);
        paragraphs.add(paragraph);
    }

    @Override
    public Chapter parse(final Path input) throws ParsingException {
        try {
            final XMLEventReader reader = this.getReader(input);
            final ChapterImpl.Builder builder = ChapterImpl.builder();
            this.readFile(reader, builder);
            return builder.build();
        } catch (final XMLStreamException e) {
            LOGGER.error("cannot parse {}", input, e);
            throw new ParsingException(e);
        } catch (final FileNotFoundException e) {
            LOGGER.error("file not found {}", input, e);
            throw new IllegalArgumentException("File not found.", e);
        }
    }

    private XMLEventReader getReader(final Path input) throws FileNotFoundException, XMLStreamException {
        final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        return xmlInputFactory.createXMLEventReader(new FileInputStream(input.toString()));
    }

    private void readFile(final XMLEventReader reader, final ChapterImpl.Builder builder)
        throws XMLStreamException, ParsingException {
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                final StartElement startElement = event.asStartElement();
                final String startElementName = startElement.getName().getLocalPart();
                if (startElementName.equals(BODY_ELEMENT_NAME)) {
                    this.parseBodyElement(reader, builder);
                }
            }
        }
    }
}
