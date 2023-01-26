package com.example.projectbackend.service;

import com.example.projectbackend.domain.Member;
import com.example.projectbackend.domain.Product;
import com.example.projectbackend.domain.Review;
import com.example.projectbackend.dto.PageRequestDTO;
import com.example.projectbackend.dto.PageResponseDTO;
import com.example.projectbackend.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    PageResponseDTO<ReviewDTO> getReviewsByPid(Long pid, PageRequestDTO pageRequestDTO);

    default ReviewDTO entityToDTO(Review review) {
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .reviewNo(review.getReviewNo())
                .mid(review.getMember().getMid())
                .pid(review.getProduct().getPid())
                .grade(review.getGrade())
                .text(review.getText())
                .regDate(review.getRegDate())
                .modDate(review.getModDate())
                .build();
        return reviewDTO;
    }

    default Review dtoToEntity(ReviewDTO reviewDTO) {
        Member member = Member.builder()
                .mid(reviewDTO.getMid())
                .build();
        Product product = Product.builder()
                .pid(reviewDTO.getPid())
                .build();

        Review review = Review.builder()
                .reviewNo(reviewDTO.getReviewNo())
                .member(member)
                .product(product)
                .grade(reviewDTO.getGrade())
                .text(reviewDTO.getText())
                .build();
        return review;
    }
}
