package org.sjk.pocketwords.ebook.epub.paragraph.impl;

import org.sjk.pocketwords.ebook.epub.paragraph.Paragraph;

/**
 * A concrete implementation of {@link Paragraph}.
 *
 * @author Simon Josef Kreuzpointner
 */
public class ParagraphImpl implements Paragraph {

    private final String text;

    public ParagraphImpl(final String text) {
        this.text = text;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getText() {
        return this.text;
    }

    public static class Builder {

        private String text;

        private Builder() {
            this.text = "";
        }

        public Builder text(final String text) {
            this.text = text;
            return this;
        }

        public ParagraphImpl build() {
            return new ParagraphImpl(this.text);
        }
    }
}
