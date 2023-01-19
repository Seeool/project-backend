package com.example.projectbackend;

import com.example.projectbackend.dto.ProductWithReviewAvgDTO;
import com.example.projectbackend.repository.ProductRepository;
import com.example.projectbackend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServicesTest {
    @Autowired
    private ProductService productService;

    @Test
    void readwithreview() {
        ProductWithReviewAvgDTO productWithReviewAvgDTO = productService.readWithReviewAvg(1L);
        System.out.println(productWithReviewAvgDTO);
    }
}
