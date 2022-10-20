package com.datapao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieDTO {
    private String title;
    private double rating;
    private int oscarCount;
    private int ratingCount;
}
