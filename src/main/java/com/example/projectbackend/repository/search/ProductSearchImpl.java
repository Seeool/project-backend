package com.example.projectbackend.repository.search;

import com.example.projectbackend.domain.*;
import com.example.projectbackend.dto.BlogWithReplyCountDTO;
import com.example.projectbackend.dto.ProductDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public List<Product> searchProduct(String category, String keyword, String sort) {
        QProduct product = QProduct.product;
        JPQLQuery<Product> query = from(product);
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(category.isEmpty()) {
            booleanBuilder.or(product.name.contains(keyword));
            query.where(booleanBuilder);
        }
        if(!category.isEmpty()) {
            booleanBuilder.or(product.category.eq(Integer.parseInt(category)).and(product.name.contains(keyword)));
            query.where(booleanBuilder);
        }
        switch (sort) {
            case "0" -> query.orderBy(product.salesVolume.desc());
            case "1" -> query.orderBy(product.salesVolume.asc());
            case "2" -> query.orderBy(product.price.desc());
            case "3" -> query.orderBy(product.price.asc());
            case "4" -> query.orderBy(product.regDate.desc());
            case "5" -> query.orderBy(product.regDate.asc());
        }
//        this.getQuerydsl().applyPagination(pageable, query);
        List<Product> list = query.fetch();
        long count = query.fetchCount();

        return list;
    }

    @Override
    public List<Product> searchProductDiscount(String category, String keyword) {
        QProduct product = QProduct.product;
        JPQLQuery<Product> query = from(product);

        if(category.isEmpty()) {
            System.out.println("1??? ??????");
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            booleanBuilder.or(product.name.contains(keyword));
            booleanBuilder.and(product.discount.isTrue());
            query.where(booleanBuilder);
        }
        if(!category.isEmpty()) {
            System.out.println("2??? ??????");
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            booleanBuilder.or(product.category.eq(Integer.parseInt(category)).and(product.name.contains(keyword)));
            booleanBuilder.and(product.discount.isTrue());
            query.where(booleanBuilder);
        }

//        this.getQuerydsl().applyPagination(pageable, query);
        List<Product> list = query.fetch();
        long count = query.fetchCount();

        return list;
    }



    @Override
    public Page<ProductDTO> searchProductPaging(String category, String keyword, Pageable pageable) {
        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;
        JPQLQuery<Product> query = from(product);
        JPQLQuery<ProductImage> subQuery = from(productImage);
        query.leftJoin(productImage)
                .on(productImage.product.eq(product)
                        .and(productImage.ord.eq(subQuery.select(productImage.ord.min())
                                .where(productImage.product.eq(product)))))
                .groupBy(product);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(!category.isEmpty()) {
            booleanBuilder.and(product.category.eq(Integer.parseInt(category)));
        }
        booleanBuilder.and(product.name.contains(keyword));
        query.where(booleanBuilder);
        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleJPQLQuery = query.select(product, productImage);
        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<ProductDTO> dtoList = tupleList.stream().map(tuple -> {
            Product product1 = tuple.get(product);
            ProductImage productImage1 = tuple.get(productImage);
            ProductDTO productDTO = ProductDTO.builder()
                    .pid(product1.getPid())
                    .category(product1.getCategory())
                    .name(product1.getName())
                    .price(product1.getPrice())
                    .discount(product1.isDiscount())
                    .dcRatio(product1.getDcRatio())
                    .originPrice(product1.getOriginPrice())
                    .text(product1.getText())
                    .origin(product1.getOrigin())
                    .stock(product1.getStock())
                    .salesVolume(product1.getSalesVolume())
                    .regDate(product1.getRegDate())
                    .modDate(product1.getModDate())
                    .build();
            List<String> fileNames = new ArrayList<>();
            fileNames.add(productImage1.getFileName());
            productDTO.setFileNames(fileNames);
            return productDTO;
        }).toList();
        long count = query.fetchCount();


        return new PageImpl<>(dtoList, pageable, count);
    }


}
