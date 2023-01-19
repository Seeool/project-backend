package com.example.projectbackend.controller;

import com.example.projectbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> list(@RequestParam String category) {
        if(category.equals("null")) {
            return ResponseEntity.ok(productService.getProductAll());
        }else {
            return ResponseEntity.ok(productService.getProductByCategory(Integer.parseInt(category)));
        }
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
    public ResponseEntity<?> discountList(@RequestParam String category) {
        if(category.equals("null")) {
            return ResponseEntity.ok(productService.getProductDiscount());
        }else {
            return ResponseEntity.ok(productService.getProductDiscountByCategory(Integer.parseInt(category)));
        }
    }
}
