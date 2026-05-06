package org.sjk.pocketwords.ebook.epub.parser;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.sjk.pocketwords.ebook.epub.EpubEBook;
import org.sjk.pocketwords.ebook.epub.chapter.Chapter;
import org.sjk.pocketwords.ebook.epub.container.Container;
import org.sjk.pocketwords.ebook.epub.mapper.ChapterToSectionMapper;
import org.sjk.pocketwords.ebook.epub.opf.Opf;
import org.sjk.pocketwords.ebook.parser.EBookParser;
import org.sjk.pocketwords.ebook.parser.exception.ParsingException;
import org.sjk.pocketwords.ebook.section.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * A concrete implementation of {@link EBookParser} for EPUB e-books.
 *
 * @author Simon Josef Kreuzpointner
 */
public class EpubParser implements EBookParser<EpubEBook> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpubParser.class);
    private static final Path CONTAINER_FILE_PATH = Path.of("META-INF/container.xml");

    @Override
    public EpubEBook parse(final Path input) throws ParsingException {
        // TODO: find a more beautiful way to handle the paths
        final Path eBookContentsPath = this.unzipEBookFile(input);
        LOGGER.debug("unzipped EBook contents into {}", eBookContentsPath);

        final EpubEBook.Builder builder = EpubEBook.builder();
        final Container container = this.parseContainer(eBookContentsPath);
        final Opf opf = this.parseOpf(eBookContentsPath, container);

        builder.identifier(opf.getIdentifier());
        builder.title(opf.getTitle());
        builder.authors(opf.getAuthors());

        final Path chaptersBasePath = eBookContentsPath.resolve(container.getRootFile().getParent());
        final List<Chapter> chapters = this.parseChapters(chaptersBasePath, opf);

        final List<Section> sections = this.mapChapters(chapters).stream().toList();
        builder.sections(sections);
        return builder.build();
    }

    private Path unzipEBookFile(final Path input) throws ParsingException {
        try (final ZipFile zipFile = new ZipFile(input.toFile())) {
            if (!zipFile.isValidZipFile()) {
                throw new IllegalArgumentException("Invalid zip file");
            }
            if (zipFile.isEncrypted()) {
                throw new UnsupportedOperationException("Encrypted files are not supported");
            }
            final Path temporaryDirectory = this.createTemporaryDirectory();
            zipFile.extractAll(temporaryDirectory.toAbsolutePath().toString());
            return temporaryDirectory;
        } catch (final ZipException e) {
            LOGGER.error("cannot open file {}", input, e);
            throw new ParsingException("cannot open file", e);
        } catch (final IOException e) {
            throw new ParsingException("cannot extract", e);
        }
    }

    private Path createTemporaryDirectory() throws IOException {
        return Files.createTempDirectory("PocketWords").toFile().toPath();
    }

    private Container parseContainer(final Path basePath) throws ParsingException {
        final Path containerPath = basePath.resolve(CONTAINER_FILE_PATH);
        final ContainerParser containerParser = new ContainerParser();
        return containerParser.parse(containerPath);
    }

    private Opf parseOpf(final Path basePath, final Container container) throws ParsingException {
        final Path opfPath = basePath.resolve(container.getRootFile());
        final OpfParser opfParser = new OpfParser();
        return opfParser.parse(opfPath);
    }

    private List<Chapter> parseChapters(final Path basePath, final Opf opf) {
        final ChapterParser chapterParser = new ChapterParser();
        final List<Chapter> result = new ArrayList<>();
        for (final Path chapterFile : opf.getChapterFiles()) {
            try {
                final Path chapterPath = basePath.resolve(chapterFile);
                final Chapter chapter = chapterParser.parse(chapterPath);
                result.add(chapter);
            } catch (final ParsingException e) {
                LOGGER.error("cannot parse chapter, skipping", e);
            }
        }
        return result;
    }

    private List<Section> mapChapters(final List<Chapter> chapters) {
        final ChapterToSectionMapper mapper = new ChapterToSectionMapper();
        return mapper.mapToSections(chapters);
    }
}
