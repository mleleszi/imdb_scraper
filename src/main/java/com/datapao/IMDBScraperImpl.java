package com.datapao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IMDBScraperImpl {

    private static final String IMDB_URL = "http://www.imdb.com";
    private static final String IMDB_TOP_URL = "/chart/top";

    public List<MovieDTO> scrapeTopMovies(int limit) throws IOException {
        if (limit < 1 || limit > 250) {
            throw new RuntimeException("Limit must be between 1 and 250");
        }

        List<MovieDTO> movies = new ArrayList<>();
        Document document = Jsoup.connect(IMDB_URL + IMDB_TOP_URL).get();

        Element row;
        for (int i = 1; i <= limit; i++) {
            row = document.select("table.chart.full-width tr").get(i);

            String title = row.select(".titleColumn a").text();
            double rating = Double.parseDouble(row.select(".imdbRating").text());

            String ratingString = row.select(".ratingColumn strong").attr("title");
            int ratingCount = Integer.parseInt(ratingString.split(" ")[3].replaceAll(",", ""));

            String link = row.select(".titleColumn a").attr("href");
            int oscarCount = getOscarCount(link);

            movies.add(new MovieDTO(title, rating, oscarCount, ratingCount));
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
