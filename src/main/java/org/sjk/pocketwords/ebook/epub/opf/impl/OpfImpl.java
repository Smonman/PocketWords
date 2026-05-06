package org.sjk.pocketwords.ebook.epub.opf.impl;

import org.sjk.pocketwords.ebook.epub.opf.Opf;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A concrete implementation of {@link Opf}.
 *
 * @author Simon Josef Kreuzpointner
 */
public class OpfImpl implements Opf {

    private final String identifier;
    private final String title;
    private final List<String> authors;
    private final List<Path> chapterFiles;

    public OpfImpl(final String identifier,
                   final String title,
                   final List<String> authors,
                   final List<Path> chapterFiles
    ) {
        this.identifier = identifier;
        this.title = title;
        this.authors = authors;
        this.chapterFiles = chapterFiles;
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
    public List<Path> getChapterFiles() {
        return this.chapterFiles;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifier);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OpfImpl that = (OpfImpl) o;
        return Objects.equals(this.identifier, that.identifier);
    }

    public static class Builder {

        private String identifier;
        private String title;
        private List<String> authors;
        private List<Path> chapterFiles;

        public Builder identifier(final String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder authors(final Collection<String> authors) {
            this.authors = new ArrayList<>(authors);
            return this;
        }

        public Builder chapterFiles(final Collection<Path> chapterFiles) {
            this.chapterFiles = new ArrayList<>(chapterFiles);
            return this;
        }

        public OpfImpl build() {
            return new OpfImpl(this.identifier, this.title, this.authors, this.chapterFiles);
        }
    }
}
