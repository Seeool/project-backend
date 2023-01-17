package com.example.projectbackend.repository;

import com.example.projectbackend.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    void deleteByProduct_Pid(Long pid);
}
