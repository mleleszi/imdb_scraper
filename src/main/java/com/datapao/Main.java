package com.datapao;

import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        List<MovieDTO> movies = new IMDBScraper().scrapeTopMovies(20);
        new RatingAdjustmentService().adjustRating(movies);

        List<String> lines = movies.stream().map(MovieDTO::toCSV).collect(Collectors.toList());
        new CSVWriter("output.csv").write(lines);
    }
}
