package com.example.projectbackend.service;

import com.example.projectbackend.domain.Product;
import com.example.projectbackend.dto.ProductDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface ProductService {
    Long create(ProductDTO productDTO);
    ProductDTO read(Long pid);
    void update(ProductDTO productDTO);
    void delete(Long pid);

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
                String[] arr = fileName.split("_");
                product.addImage(arr[0], arr[2]);
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
                .build();
        List<String> fileNames = product.getImageSet().stream().sorted().map(productImage ->
                productImage.getUuid()+"_"+productImage.getFileName()).collect(Collectors.toList());
        productDTO.setFileNames(fileNames);
        return productDTO;
    }


}
