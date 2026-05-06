package org.sjk.pocketwords.ebook.epub.mapper;

import org.sjk.pocketwords.ebook.epub.chapter.Chapter;
import org.sjk.pocketwords.ebook.epub.paragraph.Paragraph;
import org.sjk.pocketwords.ebook.mapper.SectionMapper;
import org.sjk.pocketwords.ebook.section.Section;
import org.sjk.pocketwords.ebook.section.impl.SectionImpl;

import java.util.stream.Collectors;

/**
 * This is a concrete implementation of {@link SectionMapper}.
 *
 * <p>This mapper maps {@link Chapter} to {@link Section}.
 *
 * @author Simon Josef Kreuzpointner
 */
public class ChapterToSectionMapper implements SectionMapper<Chapter> {

    @Override
    public Section mapToSection(final Chapter chapter) {
        final String text = chapter
            .getParagraphs()
            .stream()
            .map(Paragraph::getText)
            .collect(Collectors.joining("\n\n"));
        final String title = chapter.getTitle();
        return SectionImpl.builder()
                          .title(title)
                          .text(text)
                          .build();
    }
}
