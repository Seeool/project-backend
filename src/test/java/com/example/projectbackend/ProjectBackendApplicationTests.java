package com.example.projectbackend;

import com.example.projectbackend.domain.Member;
import com.example.projectbackend.domain.MemberRole;
import com.example.projectbackend.domain.Product;
import com.example.projectbackend.domain.Review;
import com.example.projectbackend.dto.ProductDTO;
import com.example.projectbackend.repository.BlogRepository;
import com.example.projectbackend.repository.MemberRepository;
import com.example.projectbackend.repository.ProductRepository;
import com.example.projectbackend.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		IntStream.rangeClosed(1,100).forEach(i -> {
			Member member = Member.builder()
					.id("test"+i)
					.pw("1234")
					.email("test"+i+"@naver.com")
					.uuid("uuid"+i)
					.fileName("file"+i)
					.fromSocial(false)
					.build();
			member.addRole(MemberRole.USER);
			if(i >= 70) {
				member.addRole(MemberRole.MANAGER);
			}
			if(i>= 90) {
				member.addRole(MemberRole.ADMIN);
			}
			memberRepository.save(member);
		});
	}

	@Test
	void insertDummyProduct() {
		IntStream.rangeClosed(1,100).forEach(i -> {

			int categoryNum = (int) (Math.random() * 5);
			int priceNum = (int)(Math.random()*10000);
			String[] categories = {"과일","정육","밀키트","간편식","통조림","쌀"};
			Product product = Product.builder()
					.category(categories[categoryNum])
					.name("음식"+i)
					.price(priceNum)
					.discount(false)
					.text("이 음식은...."+i)
					.origin("부산")
					.stock(1000)
					.salesVolume(priceNum)
					.build();

			productRepository.save(product);
		});
	}

	@Test
	void insertDummyReviews() {

		IntStream.rangeClosed(1,100).forEach(i -> {
			long grade = (long)(Math.random() * 5);
			int pid = (int)(Math.random() * 100);
			Product product = Product.builder()
					.pid(1L)
					.build();
			Member member = Member.builder()
					.id("test1")
					.build();
			Review review = Review.builder()
					.grade(grade)
					.text("이 상품 평가는..."+i)
					.member(member)
					.product(product)
					.build();
			reviewRepository.save(review);
		});
	}

	@Test
	void searchByCategory() {
		List<Product> list = productRepository.findByCategoryIsContainingIgnoreCase("통조림");
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
		List<Object[]> list = productRepository.findFirst6ByOrderByReviewAvgDesc(PageRequest.of(0,6));
		list.forEach(product -> System.out.println(product[1]));
	}

	@Test
	void searchBysalesVolume() {
		List<Product> list = productRepository.findFirst12ByOrderBySalesVolumeDesc();
		list.forEach(product -> System.out.println(product));
	}
}
