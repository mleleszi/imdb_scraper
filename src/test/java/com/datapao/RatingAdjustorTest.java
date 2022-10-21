package com.datapao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RatingAdjustorTest {
    RatingAdjustor ratingAdjustor = new RatingAdjustor();

    @Test
    public void reviewPenalizer_WhenDeviatonMoraThan100k_ThenAdjustedRatingCorrectly() {
        // given
        int maxReviewCount = 4654432;
        MovieDTO movieDTO = new MovieDTO("title", 9.1, 3, 3654323);
        movieDTO.setAdjustedRating(9.1);
        double expected = 8.1; // 9.1 - 0.1 * 10

        // when
        ratingAdjustor.reviewPenalizer(movieDTO, maxReviewCount);
        double actual = movieDTO.getAdjustedRating();

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void reviewPenalizer_WhenMaxReviews_ThenDontAdjustRating() {
        // given
        int maxReviewCount = 3654323;
        MovieDTO movieDTO = new MovieDTO("title", 9.1, 3, 3654323);
        movieDTO.setAdjustedRating(9.1);
        double expected = 9.1;

        // when
        ratingAdjustor.reviewPenalizer(movieDTO, maxReviewCount);
        double actual = movieDTO.getAdjustedRating();

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void reviewPenalizer_WhenLessThan100kDeviation_ThenDontAdjustRating() {
        // given
        int maxReviewCount = 3684323;
        MovieDTO movieDTO = new MovieDTO("title", 9.1, 3, 3654323);
        movieDTO.setAdjustedRating(9.1);
        double expected = 9.1;

        // when
        ratingAdjustor.reviewPenalizer(movieDTO, maxReviewCount);
        double actual = movieDTO.getAdjustedRating();

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void oscarCalculator_WhenNoOscars_ThenDontAdjustRating() {
        // given
        MovieDTO movieDTO = new MovieDTO("title", 9.1, 0, 3654323);
        movieDTO.setAdjustedRating(9.1);
        double expected = 9.1;

        // when
        ratingAdjustor.oscarCalculator(movieDTO);
        double actual = movieDTO.getAdjustedRating();

        // then
        assertEquals(expected, actual);

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void oscarCalculator_When1To2Oscars_ThenAdjustRatingCorrectly(int oscarCount) {
        // given
        MovieDTO movieDTO = new MovieDTO("title", 9.1, oscarCount, 3654323);
        movieDTO.setAdjustedRating(9.1);
        double expected = 9.4;

        // when
        ratingAdjustor.oscarCalculator(movieDTO);
        double actual = movieDTO.getAdjustedRating();

        // then
        assertEquals(expected, actual);

    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5})
    public void oscarCalculator_When3To5scars_ThenAdjustRatingCorrectly(int oscarCount) {
        // given
        MovieDTO movieDTO = new MovieDTO("title", 9.1, oscarCount, 3654323);
        movieDTO.setAdjustedRating(9.1);
        double expected = 9.6;

        // when
        ratingAdjustor.oscarCalculator(movieDTO);
        double actual = movieDTO.getAdjustedRating();

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10})
    public void oscarCalculator_When6To10scars_ThenAdjustRatingCorrectly(int oscarCount) {
        // given
        MovieDTO movieDTO = new MovieDTO("title", 9.1, oscarCount, 3654323);
        movieDTO.setAdjustedRating(9.1);
        double expected = 10.1;

        // when
        ratingAdjustor.oscarCalculator(movieDTO);
        double actual = movieDTO.getAdjustedRating();

        // then
        assertEquals(expected, actual);

    }

    @ParameterizedTest
    @ValueSource(ints = {12, 100, Integer.MAX_VALUE})
    public void oscarCalculator_WhenMoranThan10OOOscars_ThenAdjustRatingCorrectly(int oscarCount) {
        // given
        MovieDTO movieDTO = new MovieDTO("title", 9.1, oscarCount, 3654323);
        movieDTO.setAdjustedRating(9.1);
        double expected = 10.6;

        // when
        ratingAdjustor.oscarCalculator(movieDTO);
        double actual = movieDTO.getAdjustedRating();

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    public void calculateAdjustedRating_ThenCalculateCorrectly(int oscarCount) {
        // given
        MovieDTO movieDTO1 = new MovieDTO("title", 9.1, oscarCount, 4567876);
        MovieDTO movieDTO2 = new MovieDTO("title", 8.1, oscarCount + 1, 3767999);
        movieDTO1.setAdjustedRating(movieDTO1.getRating());
        movieDTO2.setAdjustedRating(movieDTO2.getRating());

        List<MovieDTO> movies = Arrays.asList(new MovieDTO[]{
                new MovieDTO(movieDTO1), new MovieDTO(movieDTO2)
        });

        ratingAdjustor.oscarCalculator(movieDTO1);
        ratingAdjustor.oscarCalculator(movieDTO2);
        ratingAdjustor.reviewPenalizer(movieDTO1, 4567876);
        ratingAdjustor.reviewPenalizer(movieDTO2, 4567876);

        // when
        ratingAdjustor.calculateAdjustedRating(movies);

        // then
        assertEquals(movieDTO1.getAdjustedRating(), movies.get(0).getAdjustedRating());
        assertEquals(movieDTO2.getAdjustedRating(), movies.get(1).getAdjustedRating());
    }






}
