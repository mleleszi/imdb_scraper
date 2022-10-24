package com.datapao;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class IMDBScraperTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    Document document;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    Element element;

    @Spy
    IMDBScraper imdbScraper;


    @Test
    public void getOscarCount_OscarNominated_ThenReturn0() throws IOException {
        // given
        when(document.select(anyString()).text())
                .thenReturn("Nominated for 7 Oscars");
        int expected = 0;

        // when
        int actual = imdbScraper.getOscarCount(document);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void getOscarCount_OscarWon_ThenReturnCorrectValue() throws IOException {
        // given
        when(document.select(anyString()).text())
                .thenReturn("Won 3 Oscars");
        int expected = 3;

        // when
        int actual = imdbScraper.getOscarCount(document);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void getOscarCount_NoOscar_ThenReturn0() throws IOException {
        // given
        when(document.select(anyString()).text()).thenReturn("Awards");
        int expected = 0;

        // when
        int actual = imdbScraper.getOscarCount(document);

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 300})
    public void getTopMovies_WhenLimitOutOfBounds_ThenThrowException(int limit) {
        // given
        Class excepted = RuntimeException.class;

        // when
        Executable executable = () -> imdbScraper.getTopMovies(document, limit);

        // then
        assertThrows(excepted, executable);
    }

    @Test
    public void getTopMoviesTest() throws IOException {
        // given
        when(element.select(".imdbRating").text()).thenReturn("9.2");
        when(element.select(".titleColumn a").text()).thenReturn("Title");
        when(element.select(".ratingColumn strong").attr("title"))
                .thenReturn("9.2 based on 2,653,597 user ratings");
        doReturn(null).when(imdbScraper).getHTMLForURL(any());
        doReturn(1).when(imdbScraper).getOscarCount(any());
        when(document.select("table.chart.full-width tr").get(anyInt()))
                .thenReturn(element);

        // when
        List<MovieDTO> movies = imdbScraper.getTopMovies(document, 1);

        // then
        assertEquals(1, movies.size());
        assertEquals(9.2, movies.get(0).getRating());
        assertEquals("Title", movies.get(0).getTitle());
        assertEquals(2653597, movies.get(0).getReviewCount());
    }



}
