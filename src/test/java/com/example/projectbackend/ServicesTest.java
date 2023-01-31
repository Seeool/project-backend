package com.example.projectbackend;

import com.example.projectbackend.domain.Product;
import com.example.projectbackend.domain.ProductImage;
import com.example.projectbackend.dto.ProductWithReviewAvgDTO;
import com.example.projectbackend.dto.ReviewDTO;
import com.example.projectbackend.repository.ProductRepository;
import com.example.projectbackend.repository.ReviewRepository;
import com.example.projectbackend.service.ProductService;
import com.example.projectbackend.service.ReviewService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
public class ServicesTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewService reviewService;

    @Test
    @Transactional
    void RepositoryReadTest2() {
        List<Object[]> result = productRepository.findOneWithReviewAvg(1L);
        Product product = (Product) result.get(0)[0];

        List<ProductImage> productImages = new ArrayList<>();

        result.forEach(object -> {
            ProductImage productImage = (ProductImage) object[1];
            productImages.add(productImage);
        });
        Double reviewAvg = (Double) result.get(0)[2];
        Long reviewCount = (Long) result.get(0)[3];
        System.out.println(product);
        System.out.println(productImages);
        System.out.println(reviewAvg);
        System.out.println(reviewCount);

    }


    @Test
    void readwithreviewRep() {
        ProductWithReviewAvgDTO productWithReviewAvgDTO = productService.readWithReviewAvg(1L);
        System.out.println(productWithReviewAvgDTO);
    }

    @Test
    void getTopreatedFlist() {
        List<ProductWithReviewAvgDTO> result = productService.getOrderByReviewAvgDescList();
        result.stream().forEach(System.out::println);
    }

//    @Test
//    void readwithreviewServ() {
//        List<ReviewDTO> reviewDTOS = reviewService.getReviewsByPid(1L, );
//        System.out.println(reviewDTOS);
//    }






}
