package com.example.projectbackend.repository;

import com.example.projectbackend.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select r from Review r where r.product.pid = :pid")
    Page<Review> findReviewsByPid(Long pid, Pageable pageable);
    void deleteByProduct_Pid(Long pid);
}
