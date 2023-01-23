package com.example.projectbackend.repository.search;

import com.example.projectbackend.dto.BlogDTO;
import com.example.projectbackend.dto.BlogWithReplyCountDTO;
import com.example.projectbackend.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogSearch {
    Page<BlogWithReplyCountDTO> searchBlogPaging(String[] types, String category, String keyword, Pageable pageable);

}
