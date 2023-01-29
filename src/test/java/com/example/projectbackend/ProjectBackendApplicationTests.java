package com.example.projectbackend;

import com.example.projectbackend.config.PasswordEncoderConfig;
import com.example.projectbackend.domain.*;
import com.example.projectbackend.dto.ProductDTO;
import com.example.projectbackend.repository.*;
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
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private PasswordEncoderConfig passwordEncoderConfig;


    @Test
    void insertDummyMember() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .mid("test" + i)
                    .pw(passwordEncoderConfig.passwordEncoder().encode("1234"))
                    .email("test" + i + "@naver.com")
                    .fileName("img/member/base.png")
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
    void insertAdminManagerUser() {

        Member admin = Member.builder()
                .mid("admin")
                .pw(passwordEncoderConfig.passwordEncoder().encode("1234"))
                .email("admin@naver.com")
                .fileName("img/member/base.png")
                .name("김아무개")
                .fromSocial(false)
                .build();
        admin.addRole(MemberRole.ADMIN);

        Member manager = Member.builder()
                .mid("manager")
                .pw(passwordEncoderConfig.passwordEncoder().encode("1234"))
                .email("manager@naver.com")
                .fileName("img/member/base.png")
                .name("이아무개")
                .fromSocial(false)
                .build();
        manager.addRole(MemberRole.MANAGER);

        Member user = Member.builder()
                .mid("user")
                .pw(passwordEncoderConfig.passwordEncoder().encode("1234"))
                .email("user@naver.com")
                .fileName("img/member/base.png")
                .fromSocial(false)
                .name("박아무개")
                .build();
        user.addRole(MemberRole.USER);


        memberRepository.save(admin);
        memberRepository.save(manager);
        memberRepository.save(user);
    }


    @Test
    void insertDummyProduct() {
        IntStream.rangeClosed(1, 100).forEach(i -> {

            int categoryNum = (int) (Math.random() * 11);
            int priceNum = (int) (Math.random() * 10000);
            int imgNum1 = (int) (Math.random() * 12) + 1;
            int imgNum2 = (int) (Math.random() * 12) + 1;
            int imgNum3 = (int) (Math.random() * 12) + 1;
            int imgNum4 = (int) (Math.random() * 12) + 1;
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
            int imgNum1 = (int) (Math.random() * 12) + 1;
            int imgNum2 = (int) (Math.random() * 12) + 1;
            int imgNum3 = (int) (Math.random() * 12) + 1;
            int imgNum4 = (int) (Math.random() * 12) + 1;
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
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            long grade = (long) (Math.random() * 5);
            int pid = (int) (Math.random() * 120) + 1;
            int mno = (int) (Math.random() * 100) + 1;
            Product product = Product.builder()
                    .pid((long) pid)
                    .build();
            Member member = Member.builder()
                    .mid("test" + mno)
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
    void insertDummyBlogs() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            int categoryNum = (int) (Math.random() * 4);
            int mno = (int) (Math.random() * 100) + 1;
            int imgNum = (int) (Math.random() * 6) + 1;
            Member member = Member.builder()
                    .mid("test" + mno)
                    .build();
            Blog blog = Blog.builder()
                    .title("더미 블로그 제목.." + i)
                    .text("테스트용 내용" + i)
                    .category(categoryNum)
                    .member(member)
                    .fileName("/img/blog/blog-" + imgNum + ".jpg")
                    .build();
            blogRepository.save(blog);
        });
    }

    @Test
    void insertDummyReplys() {
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            int bid = (int) (Math.random() * 100) + 1;
            int mno = (int) (Math.random() * 100) + 1;
            Blog blog = Blog.builder()
                    .bid((long) bid)
                    .build();
            Member member = Member.builder()
                    .mid("test" + mno)
                    .build();
            Reply reply = Reply.builder()
                    .text("이 상품 평가는..." + i)
                    .member(member)
                    .blog(blog)
                    .build();
            replyRepository.save(reply);
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
        List<Object[]> list = productRepository.findFirst6ByOrderByReviewAvgDesc();
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

    @Test
    void find3blogsTest() {
        List<Object[]> result = blogRepository.findFirst3ByOrderByRegDateDescWithReplyCount();
        result.stream().forEach(System.out::println);
    }

    @Test
    void findBlogCountByCategoryTest() {
        System.out.println(blogRepository.countBlogByCategoryIs(0));
    }

    @Test
    void findBlogCountTest() {
        System.out.println(blogRepository.count());
    }


    @Test
    @Transactional
    void findBlogByBid() {
        List<Object[]> result = blogRepository.findBlogByBid(77L);
        Object[] rrr = result.get(0);
        Blog blog = (Blog) rrr[0];
        Member member = (Member) rrr[1];

        System.out.println(blog);
        System.out.println(member.getRoleSet());
        System.out.println(rrr[2]);


    }

}
