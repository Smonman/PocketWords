package org.sjk.pocketwords.ebook.epub.container.impl;

import org.sjk.pocketwords.ebook.epub.container.Container;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A concrete implementation of {@link Container}.
 *
 * @author Simon Josef Kreuzpointner
 */
public class ContainerImpl implements Container {

    private final Path rootFile;

    public ContainerImpl(final Path rootFile) {
        this.rootFile = rootFile;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Path getRootFile() {
        return this.rootFile;
    }

    public static class Builder {

        private Path rootFile;

        private Builder() {
            this.rootFile = Paths.get("");
        }

        public Builder rootFile(final Path rootFile) {
            this.rootFile = rootFile;
            return this;
        }

        public ContainerImpl build() {
            return new ContainerImpl(this.rootFile);
        }
    }
}
