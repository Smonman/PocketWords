package org.sjk.pocketwords.ebook.epub;

import org.sjk.pocketwords.ebook.EBook;
import org.sjk.pocketwords.ebook.section.Section;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A concrete EPUB e-book.
 *
 * @author Simon Josef Kreuzpointner
 * @see <a href="https://www.w3.org/TR/epub-33">EPUB 3.3 W3C Recommendation</a>
 */
public class EpubEBook implements EBook {

    private final String identifier;
    private final String title;
    private final List<String> authors;
    private final List<Section> sections;

    public EpubEBook(final String identifier,
                     final String title,
                     final List<String> authors,
                     final List<Section> sections
    ) {
        this.identifier = identifier;
        this.title = title;
        this.authors = authors;
        this.sections = sections;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public List<String> getAuthors() {
        return this.authors;
    }

    @Override
    public List<Section> getSections() {
        return this.sections;
    }

    public static class Builder {

        private String identifier;
        private String title;
        private List<String> authors;
        private List<Section> sections;

        private Builder() {
            this.identifier = "";
            this.title = "";
            this.authors = Collections.emptyList();
            this.sections = Collections.emptyList();
        }

        public Builder identifier(final String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder authors(final Collection<String> authors) {
            this.authors = new LinkedList<>(authors);
            return this;
        }

        public Builder sections(final Collection<Section> sections) {
            this.sections = new LinkedList<>(sections);
            return this;
        }

        public EpubEBook build() {
            return new EpubEBook(this.identifier, this.title, this.authors, this.sections);
        }
    }
}
