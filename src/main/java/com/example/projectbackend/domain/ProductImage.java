package com.example.projectbackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "product")
public class ProductImage implements Comparable<ProductImage>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    private String fileName;
    private int ord;
    @ManyToOne
    private Product product;

    @Override
    public int compareTo(ProductImage o) {
        return this.ord - o.ord;
    }

    public void changeProduct(Product product) {
        this.product = product;
    }
}
