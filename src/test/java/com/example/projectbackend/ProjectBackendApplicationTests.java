package com.example.projectbackend;

import com.example.projectbackend.domain.*;
import com.example.projectbackend.dto.ProductDTO;
import com.example.projectbackend.repository.BlogRepository;
import com.example.projectbackend.repository.MemberRepository;
import com.example.projectbackend.repository.ProductRepository;
import com.example.projectbackend.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class ProjectBackendApplicationTests {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;


    @Test
    void insertDummyMember() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .id("test" + i)
                    .pw("1234")
                    .email("test" + i + "@naver.com")
                    .uuid("uuid" + i)
                    .fileName("file" + i)
                    .fromSocial(false)
                    .build();
            member.addRole(MemberRole.USER);
            if (i >= 70) {
                member.addRole(MemberRole.MANAGER);
            }
            if (i >= 90) {
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        });
    }

    @Test
    void insertDummyProduct() {
        IntStream.rangeClosed(1, 200).forEach(i -> {

            int categoryNum = (int) (Math.random() * 11);
            int priceNum = (int) (Math.random() * 10000);
            int imgNum1 = (int) (Math.random() * 11) + 1;
            int imgNum2 = (int) (Math.random() * 11) + 1;
            int imgNum3 = (int) (Math.random() * 11) + 1;
            int imgNum4 = (int) (Math.random() * 11) + 1;
            String[] categories = {"과일", "정육/계란", "밀키트", "냉장/냉동/간편식", "통조림/즉석밥/면", "쌀/잡곡", "베이커리", "장/양념/소스", "우유/유제품", "채소", "건강식품"};
            Product product = Product.builder()
                    .category(categoryNum)
                    .name(categories[categoryNum] + " 더미" + i)
                    .price(priceNum)
                    .discount(false)
                    .text("이 상품은...." + i)
                    .origin("부산")
                    .stock(1000)
                    .salesVolume(priceNum)
                    .build();
            product.addImage("/img/product/product-" + imgNum1 + ".jpg");
            product.addImage("/img/product/product-" + imgNum2 + ".jpg");
            product.addImage("/img/product/product-" + imgNum3 + ".jpg");
            product.addImage("/img/product/product-" + imgNum4 + ".jpg");
            productRepository.save(product);
        });
    }

    @Test
    void insertDummyDiscountProduct() {
        IntStream.rangeClosed(1, 20).forEach(i -> {
            int dcRatio = (int) (Math.random() * 70) + 20;
            int categoryNum = (int) (Math.random() * 11);
            int priceNum = (int) (Math.random() * 10000);
            int imgNum1 = (int) (Math.random() * 11) + 1;
            int imgNum2 = (int) (Math.random() * 11) + 1;
            int imgNum3 = (int) (Math.random() * 11) + 1;
            int imgNum4 = (int) (Math.random() * 11) + 1;
            String[] categories = {"과일", "정육/계란", "밀키트", "냉장/냉동/간편식", "통조림/즉석밥/면", "쌀/잡곡", "베이커리", "장/양념/소스", "우유/유제품", "채소", "건강식품"};
            Product product = Product.builder()
                    .category(categoryNum)
                    .name(categories[categoryNum] + " 할인 더미" + i)
                    .discount(true)
                    .dcRatio(dcRatio)
                    .originPrice(priceNum)
                    .price(priceNum * (100 - dcRatio) / 100)
                    .text("이 상품은...." + i)
                    .origin("부산")
                    .stock(1000)
                    .salesVolume(priceNum)
                    .build();
            product.addImage("/img/product/product-" + imgNum1 + ".jpg");
            product.addImage("/img/product/product-" + imgNum2 + ".jpg");
            product.addImage("/img/product/product-" + imgNum3 + ".jpg");
            product.addImage("/img/product/product-" + imgNum4 + ".jpg");
            productRepository.save(product);
        });
    }

    @Test
    void insertDummyReviews() {
        IntStream.rangeClosed(1, 2500).forEach(i -> {
            long grade = (long) (Math.random() * 5);
            int pid = (int) (Math.random() * 119) + 1;
            int mno = (int) (Math.random() * 99) + 1;
            Product product = Product.builder()
                    .pid((long) pid)
                    .build();
            Member member = Member.builder()
                    .id("test"+mno)
                    .build();
            Review review = Review.builder()
                    .grade(grade)
                    .text("이 상품 평가는..." + i)
                    .member(member)
                    .product(product)
                    .build();
            reviewRepository.save(review);
        });
    }

    @Test
    void searchByCategory() {
        List<Product> list = productRepository.findByCategoryIs(1);
        list.forEach(product -> System.out.println(product));
    }

    @Test
    void searchByDiscount() {
        List<Product> list = productRepository.findByDiscountIs(true);
        list.forEach(product -> System.out.println(product));
    }

    @Test
    void searchByregDate() {
        List<Product> list = productRepository.findFirst6ByOrderByRegDateDesc();
        list.forEach(product -> System.out.println(product));
    }

    @Test
    void searchByavgGrade() {
        List<Object[]> list = productRepository.findFirst6ByOrderByReviewAvgDesc(PageRequest.of(0, 6));
        list.forEach(arr -> {
            Product product = (Product) arr[0];
            Double reviewAvg = (Double) arr[1];
            System.out.println(product);
            System.out.println(product.getImageSet());
            System.out.println(reviewAvg);
        });
    }

    @Test
    void searchBysalesVolume() {
        List<Product> list = productRepository.findFirst12ByOrderBySalesVolumeDesc();
        list.forEach(product -> System.out.println(product));
    }

    @Test
    void serachOneWithReviewAvg() {
        List<Object[]> result = productRepository.findOneWithReviewAvg(1L);
        System.out.println(result.get(0)[0]);
        System.out.println(result.get(0)[1]);
    }

//    @Test
//    void findReviewByPid() {
//        List<Review> result = reviewRepository.findReviewsByProduct_Pid(1L);
//        System.out.println(result);
//    }
}
