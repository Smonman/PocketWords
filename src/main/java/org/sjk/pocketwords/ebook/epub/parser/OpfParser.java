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

    private static void parseMetadataElement(final XMLEventReader reader, final OpfImpl.Builder builder)
        throws XMLStreamException {
        final List<String> authors = new ArrayList<>();
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                final StartElement startElement = event.asStartElement();
                final String startElementName = startElement.getName().getLocalPart();
                switch (startElementName) {
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
            } else if (event.isEndElement()) {
                final EndElement endElement = event.asEndElement();
                final String endElementName = endElement.getName().getLocalPart();
                if (endElementName.equals(METADATA_ELEMENT_NAME)) {
                    // done with parsing the <metadata> block
                    builder.authors(authors);
                    break;
                }
            }
        }
    }

    private static void parseSpineElement(final XMLEventReader reader,
                                          final OpfImpl.Builder builder,
                                          final Map<String, String> items
    ) throws XMLStreamException {
        final List<String> chapters = new ArrayList<>();
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                final StartElement startElement = event.asStartElement();
                final String startElementName = startElement.getName().getLocalPart();
                if (startElementName.equals(ITEM_REF_ELEMENT_NAME)) {
                    // parse <itemref> element
                    final String idRef = startElement.getAttributeByName(new QName(ID_REF_ATTRIBUTE_NAME)).getValue();
                    // FIXME: quick and dirty fix to only get text chapter entries
                    if (idRef.startsWith("id-")) {
                        chapters.add(items.get(idRef));
                    }
                }
            } else if (event.isEndElement()) {
                final EndElement endElement = event.asEndElement();
                final String endElementName = endElement.getName().getLocalPart();
                if (endElementName.equals(SPINE_ELEMENT_NAME)) {
                    // done with parsing the <spine> block
                    builder.chapterFiles(chapters.stream().map(Path::of).toList());
                    break;
                }
            }
        }
    }

    private static void parseManifestElement(final XMLEventReader reader, final Map<String, String> items)
        throws XMLStreamException {
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                final StartElement startElement = event.asStartElement();
                final String startElementName = startElement.getName().getLocalPart();
                if (startElementName.equals(ITEM_ELEMENT_NAME)) {
                    // parse <item> element
                    final String id = startElement.getAttributeByName(new QName(ID_ATTRIBUTE_NAME)).getValue();
                    final String href = startElement.getAttributeByName(new QName(HREF_ATTRIBUTE_NAME)).getValue();
                    items.put(id, href);
                }
            } else if (event.isEndElement()) {
                final EndElement endElement = event.asEndElement();
                final String endElementName = endElement.getName().getLocalPart();
                if (endElementName.equals(MANIFEST_ELEMENT_NAME)) {
                    // done with parsing the <manifest> block
                    break;
                }
            }
        }
    }

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
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        return xmlInputFactory.createXMLEventReader(new FileInputStream(input.toString()));
    }

    private void readFile(final XMLEventReader reader, final OpfImpl.Builder builder) throws XMLStreamException {
        final Map<String, String> items = new HashMap<>();
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                final StartElement startElement = event.asStartElement();
                final String startElementName = startElement.getName().getLocalPart();
                switch (startElementName) {
                    case METADATA_ELEMENT_NAME -> OpfParser.parseMetadataElement(reader, builder);
                    case MANIFEST_ELEMENT_NAME -> OpfParser.parseManifestElement(reader, items);
                    case SPINE_ELEMENT_NAME -> OpfParser.parseSpineElement(reader, builder, items);
                }
            }
        }
    }
}
