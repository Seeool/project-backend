package com.example.projectbackend;

import com.example.projectbackend.domain.Member;
import com.example.projectbackend.domain.MemberRole;
import com.example.projectbackend.domain.Product;
import com.example.projectbackend.repository.BlogRepository;
import com.example.projectbackend.repository.MemberRepository;
import com.example.projectbackend.repository.ProductRepository;
import com.example.projectbackend.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
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

}
