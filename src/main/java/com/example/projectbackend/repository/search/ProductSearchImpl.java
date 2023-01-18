package com.example.projectbackend.repository.search;

import com.example.projectbackend.domain.Product;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductSearchImpl extends QuerydslRepositorySupport {
    public ProductSearchImpl() {
        super(Product.class);
    }


}
