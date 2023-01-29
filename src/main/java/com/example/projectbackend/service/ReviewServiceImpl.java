package com.example.projectbackend.service;

import com.example.projectbackend.domain.Review;
import com.example.projectbackend.dto.PageRequestDTO;
import com.example.projectbackend.dto.PageResponseDTO;
import com.example.projectbackend.dto.ReviewDTO;
import com.example.projectbackend.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public Long create(ReviewDTO reviewDTO) {
        Review review = dtoToEntity(reviewDTO);
        return reviewRepository.save(review).getReviewNo();
    }

    @Override
    public void delete(Long reviewNo) {
        reviewRepository.deleteById(reviewNo);
    }

    @Override
    public PageResponseDTO<ReviewDTO> getReviewsByPid(Long pid, PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), Sort.by("reviewNo").descending());

        Page<Review> reviews = reviewRepository.findReviewsByPid(pid, pageable);
        List<ReviewDTO> dtoList = reviews.stream().map(this::entityToDTO).collect(Collectors.toList());
        int count = (int) reviews.getTotalElements();
        return new PageResponseDTO<>(pageRequestDTO, dtoList, count);
    }
}
