package com.example.projectbackend.controller;

import com.example.projectbackend.dto.PageRequestDTO;
import com.example.projectbackend.dto.ProductDTO;
import com.example.projectbackend.service.MemberService;
import com.example.projectbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("/{pid}")
    public ResponseEntity<?> read(@PathVariable Long pid) {
        System.out.println("상품 읽어오기");
        return ResponseEntity.ok(productService.readWithReviewAvg(pid));
    }
    @PostMapping("/authentication/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody ProductDTO productDTO) {
        System.out.println("등록 시작");
        System.out.println(productDTO);
        try {
            Long pid = productService.create(productDTO);
            Map<String, Object> result = Map.of("pid", pid);
            return ResponseEntity.ok(result);
        } catch (ProductService.PidExistException e) {
            System.out.println("exist 에러");
            return ResponseEntity.badRequest().body("exist");
        }
    }
    @DeleteMapping("/authentication/{pid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long pid) {
        System.out.println("삭제 시작");
        productService.delete(pid);
        return ResponseEntity.ok("Delete Success");
    }
    @PutMapping("/authentication/{pid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modify(@RequestBody ProductDTO productDTO) {
        System.out.println("수정 시작");
        System.out.println(productDTO);
        productService.update(productDTO);
        return ResponseEntity.ok("Modify Success");
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
