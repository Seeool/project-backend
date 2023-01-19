package com.example.projectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductWithReviewAvgDTO {
    private Long pid;
    private int category;
    private String name;

    private int price;
    private int stock;
    private int salesVolume;
    private String origin;
    private List<String> fileNames;
    private boolean discount;
    private int dcRatio;
    private int originPrice;
    private String text;
    private LocalDateTime regDate, modDate;
    private Double reviewAvg;
}
