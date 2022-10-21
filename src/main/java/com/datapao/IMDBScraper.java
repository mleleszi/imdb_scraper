package com.datapao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class IMDBScraper {

    // made accessible for tests
    protected String IMDB_URL = "http://www.imdb.com";
    protected String IMDB_TOP_URL = "/chart/top";

    // Gets the title, rating, review count, oscar count for the top {limit} movies
    public List<MovieDTO> scrapeTopMovies(int limit) throws IOException {
        if (limit < 1 || limit > 250) {
            throw new RuntimeException("Limit must be between 1 and 250");
        }

        List<MovieDTO> movies = new ArrayList<>();
        Document document = Jsoup.connect(IMDB_URL + IMDB_TOP_URL).get();

        Element row;
        for (int i = 1; i <= limit; i++) {
            try {
                row = document.select("table.chart.full-width tr").get(i);

                // gets the title of the movie
                String title = row.select(".titleColumn a").text();
                if (title.length() == 0) {
                    throw new RuntimeException();
                }

                // gets the rating of the movie
                double rating = Double.parseDouble(row.select(".imdbRating").text());

                // gets the number of reviews for the movie
                String reviewString = row.select(".ratingColumn strong").attr("title");
                int reviewCount = Integer.parseInt(reviewString.split(" ")[3].replaceAll(",", ""));

                // gets the link for the page of the movie
                String link = row.select(".titleColumn a").attr("href");

                int oscarCount = getOscarCount(link);

                movies.add(new MovieDTO(title, rating, oscarCount, reviewCount));
            } catch (Exception e) {
                System.err.println("The structure of the website changed, cannot get some elements!");
                exit(1);
            }
        }

        return movies;
    }

    // Gets the number of Oscars won for a given movie link
    public int getOscarCount(String link) throws IOException {
        String awardString = Jsoup.connect(IMDB_URL + link)
                .get()
                .select("#__next > main > div > section.ipc-page-background.ipc-page-background--base.sc-9b716f3b-0.hWwhTB > div > section > div > div.sc-b1d8602f-1.fuYOtZ.ipc-page-grid__item.ipc-page-grid__item--span-2 > section:nth-child(3) > div > ul > li > a.ipc-metadata-list-item__label.ipc-metadata-list-item__label--link")
                .text();

        if (!(awardString.contains("Oscar") && awardString.contains("Won")))
            return 0;

        return Integer.parseInt(awardString.replaceAll("[\\D]", ""));
    }
}
