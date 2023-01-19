package com.example.projectbackend.dto;

import com.example.projectbackend.domain.ProductImage;
import com.example.projectbackend.domain.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

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
}
