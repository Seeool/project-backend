package com.example.projectbackend.controller;

import com.example.projectbackend.dto.PageRequestDTO;
import com.example.projectbackend.dto.PageResponseDTO;
import com.example.projectbackend.dto.ReviewDTO;
import com.example.projectbackend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/{pid}")
    public ResponseEntity<?> getReviews(@PathVariable("pid") Long pid, PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(reviewService.getReviewsByPid(pid, pageRequestDTO));
    }
}
