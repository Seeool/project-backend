package com.example.projectbackend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductImage implements Comparable<ProductImage>{
    @Id
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
