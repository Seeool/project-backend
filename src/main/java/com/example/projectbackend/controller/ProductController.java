package com.example.projectbackend.controller;

import com.example.projectbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("/{pid}")
    public ResponseEntity<?> read(@PathVariable Long pid) {
        return ResponseEntity.ok(productService.readWithReviewAvg(pid));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(productService.getProductAll());
    }

    @GetMapping("/featuredList")
    public ResponseEntity<?> featuredList() {
        return ResponseEntity.ok(productService.getFeaturedList());
    }

    @GetMapping("/latestList")
    public ResponseEntity<?> latestList() {
        return ResponseEntity.ok(productService.getOrderByRegDateDescList());
    }

    @GetMapping("/topRatedList")
    public ResponseEntity<?> topRatedList() {
        return ResponseEntity.ok(productService.getOrderByReviewAvgDescList());
    }

    @GetMapping("/discoutList")
    public ResponseEntity<?> discountList() {
        return ResponseEntity.ok(productService.getProductDiscount());
    }
}
