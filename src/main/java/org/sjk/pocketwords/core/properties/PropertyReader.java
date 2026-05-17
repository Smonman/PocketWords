package org.sjk.pocketwords.core.properties;

import java.io.IOException;
import java.util.Properties;

/**
 * Represents a reader to read in program properties from {@code .properties} files.
 *
 * @author Simon Josef Kreuzpointner
 */
public interface PropertyReader {

    /**
     * Reads in the properties from the default properties file.
     *
     * @return the read-in properties
     *
     * @throws IOException If the file does not exist or cannot be read
     */
    Properties readProperties() throws IOException;

    /**
     * Reads in the properties from the given properties file.
     *
     * @param filename the file name of the properties file
     * @return the read-in properties
     *
     * @throws IOException If the file does not exist or cannot be read
     */
    Properties readProperties(final String filename) throws IOException;
}
