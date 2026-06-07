package org.sjk.pocketwords.core.properties.impl;

import org.sjk.pocketwords.core.properties.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This is a concrete implementation of {@link PropertyReader}.
 *
 * @author Simon Josef Kreuzpointner
 */
public class PropertyReaderImpl implements PropertyReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyReaderImpl.class);
    private static final String DEFAULT_PROPERTY_FILE = "PocketWords.properties";

    @Override
    public Properties readProperties() throws IOException {
        return this.readProperties(DEFAULT_PROPERTY_FILE);
    }

    @Override
    public Properties readProperties(final String filename) throws IOException {
        LOGGER.info("Reading properties from file {}", filename);
        try (final InputStream stream = Thread.currentThread()
                                              .getContextClassLoader()
                                              .getResourceAsStream(filename)) {
            if (stream == null) {
                throw new FileNotFoundException(filename);
            }
            final Properties properties = new Properties();
            properties.load(stream);
            return properties;
        }
    }
}
