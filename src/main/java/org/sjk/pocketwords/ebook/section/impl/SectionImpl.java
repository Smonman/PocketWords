package org.sjk.pocketwords.ebook.section.impl;

import org.sjk.pocketwords.ebook.section.Section;

public class SectionImpl implements Section {

    private final String title;
    private final String text;

    public SectionImpl(final String title, final String text) {
        this.title = title;
        this.text = text;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getText() {
        return this.text;
    }

    public static class Builder {

        private String title;
        private String text;

        private Builder() {
            this.title = "";
            this.text = "";
        }

        public Builder title(final String title) {
            this.title = title;
            return this;
        }

        public Builder text(final String text) {
            this.text = text;
            return this;
        }

        public SectionImpl build() {
            return new SectionImpl(this.title, this.text);
        }
    }
}
