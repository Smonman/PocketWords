package org.sjk.pocketwords.core.properties.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Properties;

class PropertyReaderImplTest {

    private static final String EMPTY_PROPERTIES_FILENAME = "empty.properties";
    private static final String NULL_PROPERTIES_FILENAME = "null.properties";
    private static final String NON_EXISTING_PROPERTIES_FILENAME = "non-existing.properties";
    private static final String TEST_PROPERTIES_FILENAME = "test.properties";

    @Test
    void readProperties_emptyProperties() throws IOException {
        final Path path = Path.of("properties", EMPTY_PROPERTIES_FILENAME);
        final Properties actual = new PropertyReaderImpl().readProperties(path.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void readProperties_nullProperties() throws IOException {
        final Path path = Path.of("properties", NULL_PROPERTIES_FILENAME);
        final Properties actual = new PropertyReaderImpl().readProperties(path.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("", actual.get("value"));
    }

    @Test
    void readProperties_nonExistingProperties() {
        final Path path = Path.of("properties", NON_EXISTING_PROPERTIES_FILENAME);
        Assertions.assertThrows(FileNotFoundException.class,
                                () -> new PropertyReaderImpl().readProperties(path.toString()));
    }

    @Test
    void readProperties_correctValues() throws IOException {
        final Path path = Path.of("properties", TEST_PROPERTIES_FILENAME);
        final Properties actual = new PropertyReaderImpl().readProperties(path.toString());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("123", actual.get("value"));
        Assertions.assertEquals("abc", actual.get("value.a"));
    }

    @Test
    void readProperties_noFilename_usesDefault() throws IOException, IllegalAccessException {
        final PropertyReaderImpl propertyReaderSpy = Mockito.spy(PropertyReaderImpl.class);
        final Field field = ReflectionUtils.findFields(PropertyReaderImpl.class,
                                                       f -> f.getName().equals("DEFAULT_PROPERTY_FILE"),
                                                       ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).getFirst();
        field.setAccessible(true);
        final String defaultFileName = (String) field.get(null);
        Mockito.doReturn(new Properties()).when(propertyReaderSpy).readProperties(Mockito.anyString());
        propertyReaderSpy.readProperties();
        Mockito.verify(propertyReaderSpy).readProperties(defaultFileName);
    }
}
