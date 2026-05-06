package org.sjk.pocketwords.ebook.epub.parser;

import org.sjk.pocketwords.ebook.epub.opf.Opf;
import org.sjk.pocketwords.ebook.epub.opf.impl.OpfImpl;
import org.sjk.pocketwords.ebook.parser.Parser;
import org.sjk.pocketwords.ebook.parser.exception.ParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A concrete implementation of {@link Parser} for {@link Opf}.
 *
 * @author Simon Josef Kreuzpointner
 */
public class OpfParser implements Parser<Opf, Path> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpfParser.class);
    private static final String METADATA_ELEMENT_NAME = "metadata";
    private static final String IDENTIFIER_ELEMENT_NAME = "identifier";
    private static final String TITLE_ELEMENT_NAME = "title";
    private static final String CREATOR_ELEMENT_NAME = "creator";
    private static final String MANIFEST_ELEMENT_NAME = "manifest";
    private static final String ITEM_ELEMENT_NAME = "item";
    private static final String ID_ATTRIBUTE_NAME = "id";
    private static final String HREF_ATTRIBUTE_NAME = "href";
    private static final String SPINE_ELEMENT_NAME = "spine";
    private static final String ITEM_REF_ELEMENT_NAME = "itemref";
    private static final String ID_REF_ATTRIBUTE_NAME = "idref";

    @Override
    public Opf parse(final Path input) throws ParsingException {
        try {
            final XMLEventReader reader = this.getReader(input);
            final OpfImpl.Builder builder = OpfImpl.builder();
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
        return xmlInputFactory.createXMLEventReader(new FileInputStream(input.toString()));
    }

    private void readFile(final XMLEventReader reader, final OpfImpl.Builder builder) throws XMLStreamException {
        final List<String> authors = new ArrayList<>();
        final Map<String, String> items = new HashMap<>();
        final List<String> chapters = new ArrayList<>();

        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                final StartElement startElement = event.asStartElement();
                final String startElementName = startElement.getName().getLocalPart();
                switch (startElementName) {
                    case METADATA_ELEMENT_NAME -> {
                        // parse <metadata> block
                        while (reader.hasNext()) {
                            final XMLEvent event2 = reader.nextEvent();
                            if (event2.isStartElement()) {
                                final StartElement startElement2 = event2.asStartElement();
                                final String startElement2Name = startElement2.getName().getLocalPart();
                                switch (startElement2Name) {
                                    case IDENTIFIER_ELEMENT_NAME -> {
                                        final String identifier = reader.nextEvent().asCharacters().getData();
                                        builder.identifier(identifier);
                                    }
                                    case TITLE_ELEMENT_NAME -> {
                                        final String title = reader.nextEvent().asCharacters().getData();
                                        builder.title(title);
                                    }
                                    case CREATOR_ELEMENT_NAME -> {
                                        final String author = reader.nextEvent().asCharacters().getData();
                                        authors.add(author);
                                    }
                                }
                            } else if (event2.isEndElement()) {
                                final EndElement endElement2 = event2.asEndElement();
                                final String endElement2Name = endElement2.getName().getLocalPart();
                                if (endElement2Name.equals(METADATA_ELEMENT_NAME)) {
                                    // done with parsing the <metadata> block
                                    builder.authors(authors);
                                    break;
                                }
                            }
                        }
                    }
                    case MANIFEST_ELEMENT_NAME -> {
                        // parse <manifest> block
                        while (reader.hasNext()) {
                            final XMLEvent event3 = reader.nextEvent();
                            if (event3.isStartElement()) {
                                final StartElement startElement3 = event3.asStartElement();
                                final String startElement3Name = startElement3.getName().getLocalPart();
                                if (startElement3Name.equals(ITEM_ELEMENT_NAME)) {
                                    // parse <item> element
                                    final String id =
                                        startElement3.getAttributeByName(new QName(ID_ATTRIBUTE_NAME)).getValue();
                                    final String href =
                                        startElement3.getAttributeByName(new QName(HREF_ATTRIBUTE_NAME)).getValue();
                                    items.put(id, href);
                                }
                            } else if (event3.isEndElement()) {
                                final EndElement endElement3 = event3.asEndElement();
                                final String endElement3Name = endElement3.getName().getLocalPart();
                                if (endElement3Name.equals(MANIFEST_ELEMENT_NAME)) {
                                    // done with parsing the <manifest> block
                                    break;
                                }
                            }
                        }
                    }
                    case SPINE_ELEMENT_NAME -> {
                        // parse <spine> block
                        while (reader.hasNext()) {
                            final XMLEvent event4 = reader.nextEvent();
                            if (event4.isStartElement()) {
                                final StartElement startElement4 = event4.asStartElement();
                                final String startElement4Name = startElement4.getName().getLocalPart();
                                if (startElement4Name.equals(ITEM_REF_ELEMENT_NAME)) {
                                    // parse <itemref> element
                                    final String idRef =
                                        startElement4.getAttributeByName(new QName(ID_REF_ATTRIBUTE_NAME)).getValue();
                                    // FIXME: quick and dirty fix to only get text chapter entries
                                    if (idRef.startsWith("id-")) {
                                        chapters.add(items.get(idRef));
                                    }
                                }
                            } else if (event4.isEndElement()) {
                                final EndElement endElement4 = event4.asEndElement();
                                final String endElement4Name = endElement4.getName().getLocalPart();
                                if (endElement4Name.equals(SPINE_ELEMENT_NAME)) {
                                    // done with parsing the <spine> block
                                    builder.chapterFiles(chapters.stream()
                                                                 .map(Path::of)
                                                                 .toList());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
