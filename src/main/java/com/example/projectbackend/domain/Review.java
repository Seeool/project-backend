package com.example.projectbackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member", "product"})
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewNo;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private String text;
    private Long grade;


}
