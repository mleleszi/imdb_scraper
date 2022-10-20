package com.datapao;

import lombok.Data;

@Data
public class MovieDTO {
    private String title;
    private Integer rating;
    private Integer oscarCount;
    private Integer ratingCount;
}
