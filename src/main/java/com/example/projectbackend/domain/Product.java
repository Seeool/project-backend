package com.example.projectbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"imageSet", "reviewSet"})
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private String category;
    private String name;
    private int price;
    private int stock;
    private int salesVolume;
    private String origin;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ProductImage> imageSet = new HashSet<>();

    private boolean discount;
    private Long dcRatio;
    private int originPrice;
    private String text;

//    @Builder.Default
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    private Set<Review> reviewSet = new HashSet<>();

    public void changeCategory(String category) {
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
    public void changeDcRatio(Long dcRatio) {
        this.dcRatio = dcRatio;
    }
    public void changeOriginPrice(int originPrice) {
        this.originPrice = originPrice;
    }
    public void changeText(String text) {
        this.text = text;
    }

    public void addImage(String uuid, String fileName) {
        ProductImage productImage = ProductImage.builder()
                .uuid(uuid)
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
