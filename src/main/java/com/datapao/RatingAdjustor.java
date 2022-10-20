package com.datapao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;

public class RatingAdjustor {

    public void calculateAdjustedRating(List<MovieDTO> movies) {
        int maxReviewCount = findMaxReviewCount(movies);

        movies.forEach(movie -> {
            movie.setAdjustedRating(movie.getRating());
            reviewPenalizer(movie, maxReviewCount);
            oscarCalculator(movie);
        });
    }

    // Subtracts 0.1 points from the rating for every 100k deviation from the maximum rating count.
    public void reviewPenalizer(MovieDTO movieDTO, int maxReviewCount) {
        int reviewCount = movieDTO.getReviewCount();

        if (reviewCount == maxReviewCount)
            return;

        int deviation = (maxReviewCount - reviewCount) / 100000;
        movieDTO.setAdjustedRating(new BigDecimal( movieDTO.getAdjustedRating() - deviation * 0.1).setScale(1, RoundingMode.HALF_UP).doubleValue());
    }

    // Increases the rating based on the number of Oscars won.
    public void oscarCalculator(MovieDTO movieDTO) {
        int oscarCount = movieDTO.getOscarCount();

        if (oscarCount == 0)
            return;

        double adjustedRating = movieDTO.getAdjustedRating();

        if (oscarCount == 1 || oscarCount == 2) {
            adjustedRating += 0.3;
        } else if (oscarCount >= 3 && oscarCount <= 5) {
            adjustedRating += 0.5;
        } else if (oscarCount >= 6 && oscarCount <= 10) {
            adjustedRating += 1;
        } else if (oscarCount > 10) {
            adjustedRating += 1.5;
        }

        movieDTO.setAdjustedRating(adjustedRating);
    }

    public int findMaxReviewCount(List<MovieDTO> movies) {
        return movies
                .stream()
                .map(MovieDTO::getReviewCount)
                .mapToInt(v -> v)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }
}
