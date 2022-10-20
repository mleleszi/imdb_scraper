package com.datapao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;

public class RatingAdjustmentService {

    public void adjustRating(List<MovieDTO> movies) {
        int maxReviewCount = findMaxReviewCount(movies);

        movies.forEach(movie -> {
            double adjustedRating;
            int reviewCount = movie.getReviewCount();

            if (reviewCount != maxReviewCount) {
                int deviation = (maxReviewCount - reviewCount) / 100000;
                adjustedRating = new BigDecimal( movie.getRating() - deviation * 0.1).setScale(1, RoundingMode.HALF_UP).doubleValue();
            } else {
                adjustedRating = movie.getRating();
            }

            int oscarCount = movie.getOscarCount();
            if (oscarCount == 1 || oscarCount == 2) {
                adjustedRating += 0.3;
            } else if (oscarCount >= 3 && oscarCount <= 5) {
                adjustedRating += 0.5;
            } else if (oscarCount >= 6 && oscarCount <= 10) {
                adjustedRating += 1;
            } else if (oscarCount > 10) {
                adjustedRating += 1.5;
            }

            movie.setAdjustedRating(adjustedRating);

        });
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
