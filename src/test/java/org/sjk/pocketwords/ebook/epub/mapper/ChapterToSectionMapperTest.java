package org.sjk.pocketwords.ebook.epub.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sjk.pocketwords.ebook.epub.chapter.Chapter;
import org.sjk.pocketwords.ebook.epub.chapter.impl.ChapterImpl;
import org.sjk.pocketwords.ebook.epub.paragraph.Paragraph;
import org.sjk.pocketwords.ebook.epub.paragraph.impl.ParagraphImpl;
import org.sjk.pocketwords.ebook.section.Section;

import java.util.List;

class ChapterToSectionMapperTest {

    @Test
    void mapToSection() {
        final List<Paragraph> paragraphs = List.of(
            ParagraphImpl.builder().text("paragraph 1").build(),
            ParagraphImpl.builder().text("paragraph 2").build()
        );
        final Chapter chapter = ChapterImpl.builder()
                                           .title("title")
                                           .paragraphs(paragraphs)
                                           .build();
        final ChapterToSectionMapper mapper = new ChapterToSectionMapper();

        final Section actual = mapper.mapToSection(chapter);
        Assertions.assertEquals(chapter.getTitle(), actual.getTitle());
        Assertions.assertEquals("paragraph 1\n\nparagraph 2", actual.getText());
    }

    @Test
    void mapToSections() {
        final List<Paragraph> paragraphs = List.of(
            ParagraphImpl.builder().text("paragraph 1").build(),
            ParagraphImpl.builder().text("paragraph 2").build()
        );
        final Chapter chapter = ChapterImpl.builder()
                                           .title("title")
                                           .paragraphs(paragraphs)
                                           .build();
        final ChapterToSectionMapper mapper = new ChapterToSectionMapper();

        final List<Section> actual = mapper.mapToSections(List.of(chapter));
        Assertions.assertEquals(chapter.getTitle(), actual.getFirst().getTitle());
        Assertions.assertEquals("paragraph 1\n\nparagraph 2", actual.getFirst().getText());
    }
}
