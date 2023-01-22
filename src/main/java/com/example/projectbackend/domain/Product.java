package com.example.projectbackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@ToString(exclude = {"imageSet", "reviewSet"})
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private int category;
    private String name;
    private int price;
    private int stock;
    private int salesVolume;
    private String origin;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ProductImage> imageSet = new HashSet<>();

    private boolean discount;
    private int dcRatio;
    private int originPrice;
    private String text;


//    @Builder.Default
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    private Set<Review> reviewSet = new HashSet<>();

    public void changeCategory(int category) {
        this.category = category;
    }

    public void changeName(String name) {
        this.name = name;
    }
    public void changePrice(int price) {
        this.price = price;
    }
    public void changeStock(int stock) {
        this.stock = stock;
    }
    public void changeSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }
    public void changeOrigin(String origin) {
        this.origin = origin;
    }
    public void changeDiscount(boolean discount) {
        this.discount = discount;
    }
    public void changeDcRatio(int dcRatio) {
        this.dcRatio = dcRatio;
    }
    public void changeOriginPrice(int originPrice) {
        this.originPrice = originPrice;
    }
    public void changeText(String text) {
        this.text = text;
    }

    public void addImage(String fileName) {
        ProductImage productImage = ProductImage.builder()
                .fileName(fileName)
                .product(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(productImage);
    }

    public void clearImages() {
        imageSet.forEach(productImage -> productImage.changeProduct(null));
    }



}
