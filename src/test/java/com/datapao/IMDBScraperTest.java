package com.datapao;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

// These tests require network connection, dependencies are not correctly mocked.
class IMDBScraperTest {

    IMDBScraper imdbScraper = new IMDBScraper();


    @Test
    public void getOscarCount_CorrectLink_ReturnOscarsCorrectly() throws IOException {
        // given
        String link = "/title/tt0068646/";
        int expected = 3;

        // when
        int actual = imdbScraper.getOscarCount(link);

        // then
        assertEquals(expected, actual);
    }


    @Test
    public void getOscarCount_WhenIncorrectLink_ThenThrowException() throws IOException {
        // given
        String link = "/title/tt0068646sanyi/";
        Class excepted = HttpStatusException.class;

        // when
        Executable executable = () -> imdbScraper.getOscarCount(link);

        // then
        assertThrows(excepted, executable);
    }

    @Test
    public void scrapeTopMovies_WhenIncorrectLink_ThenThrowException() {
        // given
        imdbScraper.IMDB_URL = "test.com";
        Class excepted = Exception.class;

        // when
        Executable executable = () -> imdbScraper.scrapeTopMovies(1);

        // then
        assertThrows(excepted, executable);
    }


    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 300})
    public void scrapeTopMovies_WhenLimitOutOfBounds_ThenThrowException(int limit) {
        // given
        Class excepted = RuntimeException.class;

        // when
        Executable executable = () -> imdbScraper.scrapeTopMovies(limit);

        // then
        assertThrows(excepted, executable);
    }

}
