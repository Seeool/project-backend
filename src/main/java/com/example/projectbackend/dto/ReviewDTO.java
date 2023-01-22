package com.example.projectbackend.dto;

import com.example.projectbackend.domain.Member;
import com.example.projectbackend.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {

    private Long rno;
    private Long pid;
    private String mid;
    private String text;
    private Long grade;
}
