package com.example.projectbackend.controller;

import com.example.projectbackend.dto.PageRequestDTO;
import com.example.projectbackend.dto.ReviewDTO;
import com.example.projectbackend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @PostMapping("/authentication/create")
    @PreAuthorize("principal.username == #reviewDTO.mid")
    public ResponseEntity<?> createReview(@RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.create(reviewDTO));
    }

    @PostMapping("/authentication/delete") //Delete요청은 payload body가 없다
    @PreAuthorize("(principal.username == #reviewDTO.mid) or hasRole('ADMIN')")
    public ResponseEntity<?> deleteReview(@RequestBody ReviewDTO reviewDTO) {
        Long reviewNo = reviewDTO.getReviewNo();
        reviewService.delete(reviewNo);
        return ResponseEntity.ok("Delete Success");
    }

}
