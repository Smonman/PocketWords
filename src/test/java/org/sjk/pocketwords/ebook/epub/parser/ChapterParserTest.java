package org.sjk.pocketwords.ebook.epub.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sjk.pocketwords.ebook.epub.chapter.Chapter;
import org.sjk.pocketwords.ebook.parser.exception.ParsingException;

import java.net.URISyntaxException;
import java.nio.file.Path;

class ChapterParserTest {

    @Test
    void parse() throws ParsingException, URISyntaxException {
        final Path path =
            Path.of(Thread.currentThread().getContextClassLoader().getResource("ebook/chapters/ch01.xhtml").toURI());
        final ChapterParser chapterParser = new ChapterParser();
        final String chapter1Text =
            "1801.—I have just returned from a visit to my landlord—the solitary neighbour that I shall be troubled " +
                "with. This is certainly a beautiful country! In all England, I do not believe that I could have " +
                "fixed on a situation so completely removed from the stir of society. A perfect misanthropist's " +
                "heaven: and Mr. Heathcliff and I are such a suitable pair to divide the desolation between us. A " +
                "capital fellow! He little imagined how my heart warmed towards him when I beheld his black eyes " +
                "withdraw so suspiciously under their brows, as I rode up, and when his fingers sheltered themselves," +
                " with a jealous resolution, still further in his waistcoat, as I announced my name.";
        final String chapter2Text = "'Mr. Heathcliff?' I said.";
        final String chapter3Text = "A nod was the answer.";
        final String chapter4Text =
            "Joseph mumbled indistinctly in the depths of the cellar, but gave no intimation of ascending; so his " +
                "master dived down to him, leaving me vis–a–vis the ruffianly bitch and a pair of grim shaggy " +
                "sheep–dogs, who shared with her a jealous guardianship over all my movements. Not anxious to come in" +
                " contact with their fangs, I sat still; but, imagining they would scarcely understand tacit insults," +
                " I unfortunately indulged in winking and making faces at the trio, and some turn of my physiognomy " +
                "so irritated madam, that she suddenly broke into a fury and leapt on my knees. I flung her back, and" +
                " hastened to interpose the table between us. This proceeding aroused the whole hive: half–a–dozen " +
                "four–footed fiends, of various sizes and ages, issued from hidden dens to the common centre. I felt " +
                "my heels and coat–laps peculiar subjects of assault; and parrying off the larger combatants as " +
                "effectually as I could with the poker, I was constrained to demand, aloud, assistance from some of " +
                "the household in re–establishing peace.";

        final Chapter actual = chapterParser.parse(path);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Chapter I", actual.getTitle());
        Assertions.assertEquals(4, actual.getParagraphs().size());
        Assertions.assertEquals(chapter1Text, actual.getParagraphs().get(0).getText());
        Assertions.assertEquals(chapter2Text, actual.getParagraphs().get(1).getText());
        Assertions.assertEquals(chapter3Text, actual.getParagraphs().get(2).getText());
        Assertions.assertEquals(chapter4Text, actual.getParagraphs().get(3).getText());
    }

    @Test
    void parse_noParagraphs() throws URISyntaxException, ParsingException {
        final Path path =
            Path.of(Thread.currentThread()
                          .getContextClassLoader()
                          .getResource("ebook/chapters/no-paragraphs.xhtml")
                          .toURI());
        final ChapterParser chapterParser = new ChapterParser();

        final Chapter actual = chapterParser.parse(path);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("Chapter I", actual.getTitle());
        Assertions.assertEquals(0, actual.getParagraphs().size());
    }

    @Test
    void parse_noTitle() throws URISyntaxException, ParsingException {
        final Path path =
            Path.of(Thread.currentThread()
                          .getContextClassLoader()
                          .getResource("ebook/chapters/no-title.xhtml")
                          .toURI());
        final ChapterParser chapterParser = new ChapterParser();

        final Chapter actual = chapterParser.parse(path);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals("", actual.getTitle());
        Assertions.assertEquals(0, actual.getParagraphs().size());
    }

    @Test
    void parse_invalidXml_throwsParsingException() throws URISyntaxException {
        final Path path =
            Path.of(Thread.currentThread()
                          .getContextClassLoader()
                          .getResource("ebook/chapters/invalid.xhtml")
                          .toURI());
        final ChapterParser chapterParser = new ChapterParser();

        Assertions.assertThrows(ParsingException.class, () -> chapterParser.parse(path));
    }

    @Test
    void parse_invalidParagraphs_skipsParagraph() throws URISyntaxException {
        final Path path =
            Path.of(Thread.currentThread()
                          .getContextClassLoader()
                          .getResource("ebook/chapters/invalid-paragraph.xhtml")
                          .toURI());
        final ChapterParser chapterParser = new ChapterParser();

        Assertions.assertThrows(ParsingException.class, () -> chapterParser.parse(path));
    }

    @Test
    void parse_fileDoesNotExist_throwsIllegalArgumentException() {
        final ChapterParser chapterParser = new ChapterParser();
        final Path fantasyFilePath = Path.of("I/do/not/exist.xhtml");

        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> chapterParser.parse(fantasyFilePath));
    }
}
