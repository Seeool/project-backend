package com.example.projectbackend.repository;

import com.example.projectbackend.domain.Product;
import com.example.projectbackend.dto.ProductDTO;
import com.example.projectbackend.repository.search.ProductSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {
    List<Product> findFirst6ByOrderByRegDateDesc();
    //등록 최신순으로 6상품 찾기
    
    List<Product> findByCategoryIs(int category);
    //특정 카테고리 상품 찾기
    
    List<Product> findByDiscountIs(boolean discount);
    //할인 중인 상품 찾기
    
    List<Product> findByDiscountIsTrueAndCategoryIs(int category);
    //할인 중인 상품 중 특정 카테고리 상품 찾기

    @Query("select p, pi, avg(coalesce(r.grade,0)) from Product p left join ProductImage pi on (pi.product = p and pi.ord = (select min(pii.ord) from ProductImage pii where pii.product = p)) left join Review r on r.product = p group by p order by avg(coalesce(r.grade,0)) desc, p.salesVolume desc limit 6")
    List<Object[]> findFirst6ByOrderByReviewAvgDesc();

    List<Product> findFirst12ByOrderBySalesVolumeDesc();
    //판매량(인기)가 많은 순으로 12상품 찾기


    @Query("select p, pi, avg(coalesce(r.grade,0)), count(distinct r) from Product p left join ProductImage pi on pi.product = p left join Review r on r.product = p where p.pid = :pid group by pi")
    List<Object[]> findOneWithReviewAvg(@Param("pid") Long pid); //모든 이미지 다 가져오기 group by pi임
    //Object[]는 Optional이 아니라 List로,, 모든 이미지 다 가져오기

}
