package org.sjk.pocketwords.ebook.epub.parser;

import org.sjk.pocketwords.ebook.epub.paragraph.Paragraph;
import org.sjk.pocketwords.ebook.epub.paragraph.impl.ParagraphImpl;
import org.sjk.pocketwords.ebook.parser.Parser;
import org.sjk.pocketwords.ebook.parser.exception.ParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.XMLEvent;

/**
 * A concrete implementation of {@link Parser} for {@link Paragraph}.
 *
 * @author Simon Josef Kreuzpointner
 */
public class ParagraphParser implements Parser<Paragraph, XMLEventReader> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParagraphParser.class);
    private static final String P_ELEMENT_NAME = "p";

    @Override
    public Paragraph parse(final XMLEventReader input) throws ParsingException {
        try {
            final ParagraphImpl.Builder builder = ParagraphImpl.builder();
            this.readFile(input, builder);
            return builder.build();
        } catch (final XMLStreamException e) {
            LOGGER.error("cannot parse {}", input, e);
            throw new ParsingException(e);
        }
    }

    private void readFile(final XMLEventReader reader, final ParagraphImpl.Builder builder) throws XMLStreamException {
        final StringBuilder sb = new StringBuilder();

        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (event.isCharacters()) {
                final Characters characters = event.asCharacters();
                sb.append(characters.getData());
            } else if (event.isEndElement()) {
                final EndElement endElement = event.asEndElement();
                final String endElementName = endElement.getName().getLocalPart();
                if (endElementName.equals(P_ELEMENT_NAME)) {
                    // done parsing <p> block
                    builder.text(sb.toString());
                    break;
                }
            }
        }
    }
}
