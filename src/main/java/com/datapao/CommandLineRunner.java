package com.datapao;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommandLineRunner {

    public static void main(String[] args) throws Exception {
        IMDBScraper imdbScraper = new IMDBScraper();
        RatingAdjustor ratingAdjustor = new RatingAdjustor();
        CSVWriter csvWriter = new CSVWriter("output.csv");

        List<MovieDTO> movies = imdbScraper.scrapeTopMovies(20);
        ratingAdjustor.calculateAdjustedRating(movies);

        List<String> lines = movies
                .stream()
                .sorted(Comparator.comparing(MovieDTO::getAdjustedRating).reversed())
                .map(MovieDTO::toCSV)
                .collect(Collectors.toList());

        csvWriter.write(lines, "Title,Rating,Adjusted Rating");
    }
}
