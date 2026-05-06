package org.sjk.pocketwords.ebook.epub.chapter.impl;

import org.sjk.pocketwords.ebook.epub.chapter.Chapter;
import org.sjk.pocketwords.ebook.epub.paragraph.Paragraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A concrete implementation of {@link Chapter}.
 *
 * @author Simon Josef Kreuzpointner
 */
public class ChapterImpl implements Chapter {

    private final String title;
    private final List<Paragraph> paragraphs;

    public ChapterImpl(final String title, final List<Paragraph> paragraphs) {
        this.title = title;
        this.paragraphs = paragraphs;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public List<Paragraph> getParagraphs() {
        return this.paragraphs;
    }

    public static class Builder {

        private String title;
        private List<Paragraph> paragraphs;

        private Builder() {
            this.title = "";
            this.paragraphs = Collections.emptyList();
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder paragraphs(final Collection<Paragraph> paragraphs) {
            this.paragraphs = new ArrayList<>(paragraphs);
            return this;
        }

        public ChapterImpl build() {
            return new ChapterImpl(this.title, this.paragraphs);
        }
    }
}
