package com.datapao;

import org.jsoup.nodes.Document;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommandLineRunner {

    public static void main(String[] args) throws Exception {
        IMDBScraper imdbScraper = new IMDBScraper();
        RatingAdjustor ratingAdjustor = new RatingAdjustor();
        CSVWriter csvWriter = new CSVWriter("output.csv");

        Document document = imdbScraper.getHTMLForURL("http://www.imdb.com/chart/top");
        List<MovieDTO> movies = imdbScraper.getTopMovies(document, 20);
        ratingAdjustor.calculateAdjustedRating(movies);

        List<String[]> lines = movies
                .stream()
                .sorted(Comparator.comparing(MovieDTO::getAdjustedRating).reversed())
                .map(MovieDTO::toStringArray)
                .collect(Collectors.toList());

        csvWriter.write(lines, "Title,Rating,Adjusted Rating");
    }
}
