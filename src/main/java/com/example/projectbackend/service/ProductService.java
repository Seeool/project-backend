package com.example.projectbackend.service;

import com.example.projectbackend.domain.Product;
import com.example.projectbackend.dto.PageRequestDTO;
import com.example.projectbackend.dto.PageResponseDTO;
import com.example.projectbackend.dto.ProductDTO;
import com.example.projectbackend.dto.ProductWithReviewAvgDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface ProductService {
    Long create(ProductDTO productDTO);
    ProductDTO read(Long pid);
    void update(ProductDTO productDTO);
    void delete(Long pid);
    List<ProductDTO> getProductAll();
    List<ProductDTO> getFeaturedList();
    List<ProductDTO> getOrderByRegDateDescList();
    List<ProductWithReviewAvgDTO> getOrderByReviewAvgDescList();
    ProductWithReviewAvgDTO readWithReviewAvg(Long pid);
    List<ProductDTO> getProductDiscount();
    List<ProductDTO> getProductByCategory(int category);
    List<ProductDTO> getProductDiscountByCategory(int category);
    List<ProductDTO> getProductWithQueryDsl(PageRequestDTO pageRequestDTO);
    PageResponseDTO<ProductDTO> getProductPagingWithQueryDsl(PageRequestDTO pageRequestDTO);
    List<ProductDTO> getProductDiscountWithQueryDsl(PageRequestDTO pageRequestDTO);
    default Product dtoToEntity(ProductDTO productDTO) {
        Product product = Product.builder()
                .pid(productDTO.getPid())
                .category(productDTO.getCategory())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .discount(productDTO.isDiscount())
                .dcRatio(productDTO.getDcRatio())
                .originPrice(productDTO.getOriginPrice())
                .text(productDTO.getText())
                .origin(productDTO.getOrigin())
                .stock(productDTO.getStock())
                .salesVolume(productDTO.getSalesVolume())
                .build();
        if(productDTO.getFileNames() != null) {
            productDTO.getFileNames().forEach(fileName -> {
                product.addImage(fileName);
            });
        }
        return product;
    }

    default ProductDTO entityToDTO(Product product) {
        ProductDTO productDTO = ProductDTO.builder()
                .pid(product.getPid())
                .category(product.getCategory())
                .name(product.getName())
                .price(product.getPrice())
                .discount(product.isDiscount())
                .dcRatio(product.getDcRatio())
                .originPrice(product.getOriginPrice())
                .text(product.getText())
                .origin(product.getOrigin())
                .stock(product.getStock())
                .salesVolume(product.getSalesVolume())
                .regDate(product.getRegDate())
                .modDate(product.getModDate())
                .build();
        List<String> fileNames = product.getImageSet().stream().sorted().map(productImage ->
                productImage.getFileName()).collect(Collectors.toList());
        productDTO.setFileNames(fileNames);
        return productDTO;
    }

    default ProductWithReviewAvgDTO objectsToDTO(Product product, Double reviewAvg) {
        ProductWithReviewAvgDTO productWithReviewAvgDTO = ProductWithReviewAvgDTO.builder()
                .pid(product.getPid())
                .category(product.getCategory())
                .name(product.getName())
                .price(product.getPrice())
                .discount(product.isDiscount())
                .dcRatio(product.getDcRatio())
                .originPrice(product.getOriginPrice())
                .text(product.getText())
                .origin(product.getOrigin())
                .stock(product.getStock())
                .salesVolume(product.getSalesVolume())
                .regDate(product.getRegDate())
                .modDate(product.getModDate())
                .reviewAvg(reviewAvg)
                .build();

        List<String> fileNames = product.getImageSet().stream().sorted().map(productImage ->
                productImage.getFileName()).collect(Collectors.toList());
        productWithReviewAvgDTO.setFileNames(fileNames);

        return productWithReviewAvgDTO;
    }

}
