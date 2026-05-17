package org.sjk.pocketwords.ebook.epub.parser;

import org.sjk.pocketwords.ebook.epub.container.Container;
import org.sjk.pocketwords.ebook.epub.container.impl.ContainerImpl;
import org.sjk.pocketwords.ebook.parser.Parser;
import org.sjk.pocketwords.ebook.parser.exception.ParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;

/**
 * A concrete implementation of {@link Parser<>} for {@link Container}.
 *
 * @author Simon Josef Kreuzpointner
 */
public class ContainerParser implements Parser<Container, Path> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerParser.class);
    private static final String ROOT_FILE_ELEMENT_NAME = "rootfile";
    private static final String ROOT_FILE_PATH_ATTRIBUTE_NAME = "full-path";

    @Override
    public Container parse(final Path input) throws ParsingException {
        try {
            final XMLEventReader reader = this.getReader(input);
            final ContainerImpl.Builder builder = ContainerImpl.builder();
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

    private void readFile(final XMLEventReader reader, final ContainerImpl.Builder builder) throws XMLStreamException {
        while (reader.hasNext()) {
            final XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                final StartElement startElement = event.asStartElement();
                final String startElementName = startElement.getName().getLocalPart();
                if (startElementName.equals(ROOT_FILE_ELEMENT_NAME)) {
                    final String fullPathString = startElement.getAttributeByName(new QName(ROOT_FILE_PATH_ATTRIBUTE_NAME)).getValue();
                    builder.rootFile(Path.of(fullPathString));
                    LOGGER.debug("found root file {}", fullPathString);
                    return;
                }
            }
        }
        LOGGER.warn("could not find root file");
    }
}
