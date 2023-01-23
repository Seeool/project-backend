package com.example.projectbackend.repository;

import com.example.projectbackend.domain.Blog;
import com.example.projectbackend.domain.Product;
import com.example.projectbackend.dto.BlogDTO;
import com.example.projectbackend.dto.PageResponseDTO;
import com.example.projectbackend.repository.search.BlogSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, BlogSearch {

    @Query("select b, count(distinct r) from Blog b left join Reply r on r.blog = b group by b order by b.regDate limit 3")
    List<Object[]> findFirst3ByOrderByRegDateDescWithReplyCount();

    int countBlogByCategoryIs(int category);

}
