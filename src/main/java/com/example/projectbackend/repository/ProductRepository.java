package com.example.projectbackend.repository;

import com.example.projectbackend.domain.Product;
import com.example.projectbackend.dto.ProductDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findFirst6ByOrderByRegDateDesc();
    List<Product> findByDiscountIs(boolean discount);
    List<Product> findByCategoryIsContainingIgnoreCase(String category);

    @Query("select p, avg(coalesce(r.grade,0)) from Product p left join Review r on r.product = p group by p")
    List<Object[]> findFirst6ByOrderByReviewAvgDesc(Pageable pageable);

    List<Product> findFirst12ByOrderBySalesVolumeDesc();
}
