package com.example.projectbackend.controller;

import com.example.projectbackend.dto.PageRequestDTO;
import com.example.projectbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> list(PageRequestDTO pageRequestDTO) {
        System.out.println(pageRequestDTO);
        return ResponseEntity.ok(productService.getProductsPagination(pageRequestDTO));
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
    public ResponseEntity<?> discountList(PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(productService.getProductsDiscount(pageRequestDTO));
    }
}
