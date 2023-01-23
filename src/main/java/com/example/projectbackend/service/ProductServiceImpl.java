package com.example.projectbackend.service;

import com.example.projectbackend.domain.Product;
import com.example.projectbackend.domain.ProductImage;
import com.example.projectbackend.dto.PageRequestDTO;
import com.example.projectbackend.dto.PageResponseDTO;
import com.example.projectbackend.dto.ProductDTO;
import com.example.projectbackend.dto.ProductWithReviewAvgDTO;
import com.example.projectbackend.repository.ProductRepository;
import com.example.projectbackend.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long create(ProductDTO productDTO) {
        Product product = dtoToEntity(productDTO);
        return productRepository.save(product).getPid();
    }

    @Override
    public ProductDTO read(Long pid) {
        Optional<Product> result = productRepository.findById(pid);
        Product product = result.orElseThrow();
        return entityToDTO(product);
    }
    @Override
    public void update(ProductDTO productDTO) {
        Optional<Product> result = productRepository.findById(productDTO.getPid());
        Product product = result.orElseThrow();

        product.changeCategory(product.getCategory());
        product.changeName(product.getName());
        product.changePrice(product.getPrice());
        product.changeDiscount(product.isDiscount());
        product.changeOrigin(product.getOrigin());
        product.changeDcRatio(product.getDcRatio());
        product.changeText(product.getText());
        product.changeStock(product.getStock());
        product.changeSalesVolume(product.getSalesVolume());
        product.changeOriginPrice(product.getOriginPrice());

        product.clearImages();

        if(productDTO.getFileNames() != null) {
            for(String fileName : productDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                product.addImage(arr[0]);
            }
        }
        productRepository.save(product);
    }

    @Override
    public void delete(Long pid) {
        reviewRepository.deleteByProduct_Pid(pid);
        productRepository.deleteById(pid);
    }

    @Override
    public List<ProductDTO> getFeaturedList() {
        List<Product> result = productRepository.findFirst12ByOrderBySalesVolumeDesc();
        return result.stream().map(product -> entityToDTO(product)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getOrderByRegDateDescList() {
        List<Product> result = productRepository.findFirst6ByOrderByRegDateDesc();
        List<ProductDTO> dtoList = result.stream().map(this::entityToDTO).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public List<ProductWithReviewAvgDTO> getOrderByReviewAvgDescList() {
        List<Object[]> result = productRepository.findFirst6ByOrderByReviewAvgDesc();
        return result.stream().map(objects -> {
            Product product = (Product) objects[0];
            Double reviewAvg = (Double) objects[1];
            return objectsToDTO(product, reviewAvg);
        }).collect(Collectors.toList());
    }

    @Override
    public ProductWithReviewAvgDTO readWithReviewAvg(Long pid) {
        List<Object[]> result = productRepository.findOneWithReviewAvg(pid);
        Product product = (Product) result.get(0)[0];

        List<ProductImage> productImages = new ArrayList<>();
        result.forEach(object -> {
            ProductImage productImage = (ProductImage) object[1];
            productImages.add(productImage);
        });

        Double reviewAvg = (Double) result.get(0)[2];
        Long reviewCount = (Long) result.get(0)[3];
        return objectsToDTO2(product, productImages, reviewAvg, reviewCount);
    }

    @Override
    public List<ProductDTO> getProductsDiscount(PageRequestDTO pageRequestDTO) {
        String category = pageRequestDTO.getCategory();
        String keyword = pageRequestDTO.getKeyword();
        List<Product> result = productRepository.searchProductDiscount(category, keyword);
        List<ProductDTO> dtoList = result.stream().map(this::entityToDTO).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public PageResponseDTO<ProductDTO> getProductsPagination(PageRequestDTO pageRequestDTO) {
        System.out.println("페이징dsl 서비스 메서드 실행");
        String category = pageRequestDTO.getCategory(); //String인 이유는 쿼리스트링에 있는 변수는 무조건 String으로 가져오기 때문
        String keyword = pageRequestDTO.getKeyword();
        String sort = pageRequestDTO.getSort();

        Pageable pageable = null;
        pageRequestDTO.getPageableDesc("salesVolume");

        switch (sort) {
            case "" -> pageable = pageRequestDTO.getPageableDesc("salesVolume");
            case "0" -> pageable = pageRequestDTO.getPageableDesc("salesVolume");
            case "1" -> pageable = pageRequestDTO.getPageableAsc("salesVolume");
            case "2" -> pageable = pageRequestDTO.getPageableDesc("price");
            case "3" -> pageable = pageRequestDTO.getPageableAsc("price");
            case "4" -> pageable = pageRequestDTO.getPageableDesc("regDate");
            case "5" -> pageable = pageRequestDTO.getPageableAsc("regDate");
        }

        Page<ProductDTO> result = productRepository.searchProductPaging(category, keyword, pageable);

        List<ProductDTO> dtoList = result.getContent();

        return new PageResponseDTO<>(pageRequestDTO, dtoList, (int) result.getTotalElements());
    }

}
