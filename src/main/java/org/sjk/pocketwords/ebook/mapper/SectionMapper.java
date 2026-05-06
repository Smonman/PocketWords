package org.sjk.pocketwords.ebook.mapper;

import org.sjk.pocketwords.ebook.section.Section;

import java.util.List;

/**
 * Section Mapper.
 *
 * <p>Maps some other type to {@link Section}.
 *
 * @param <T> the type to be mapped
 * @author Simon Josef Kreuzpointner
 */
public interface SectionMapper<T> {

    /**
     * Maps a single objects of type {@code T} to a single instance of {@link Section}.
     *
     * @param t the object to be mapped
     * @return a new instance of {@code Section} mapped from the given object
     */
    Section mapToSection(final T t);

    /**
     * Maps a sequence of objects of type {@code T} to a sequence of instances of {@link Section}.
     *
     * @param ts the objects to be mapped
     * @return a list of {@code Section} objects
     */
    default List<Section> mapToSections(final List<T> ts) {
        return ts.stream().map(this::mapToSection).toList();
    }
}
