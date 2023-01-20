package com.example.projectbackend.repository.search;

import com.example.projectbackend.domain.Product;
import com.example.projectbackend.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductSearch {
    List<Product> searchProduct(String category, String keyword, String sort);

    Page<ProductDTO> searchProductPaging(String category, String keyword, Pageable pageable);

    List<Product> searchProductDiscount(String category, String keyword);

}
