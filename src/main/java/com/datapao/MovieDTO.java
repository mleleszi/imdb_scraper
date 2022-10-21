package com.datapao;

import lombok.Data;

@Data
public class MovieDTO {
    private String title;
    private double rating;
    private int oscarCount;
    private int reviewCount;
    private Double adjustedRating;

    public MovieDTO(String title, double rating, int oscarCount, int reviewCount) {
        this.title = title;
        this.rating = rating;
        this.oscarCount = oscarCount;
        this.reviewCount = reviewCount;
    }

    // creates a copy of movie, used for testing purpose
    public MovieDTO(MovieDTO movieDTO) {
        this.title = movieDTO.getTitle();
        this.rating = movieDTO.getRating();
        this.oscarCount = movieDTO.getOscarCount();
        this.reviewCount = movieDTO.getReviewCount();
        this.adjustedRating = movieDTO.getAdjustedRating();
    }

    public String toCSV() {
        return String.format("%s,%.2f,%.2f", title, rating, adjustedRating);
    }
}
