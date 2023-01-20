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
    public List<ProductDTO> getProductAll() {
        List<Product> result = productRepository.findAll(Sort.by("name").descending());
        return result.stream().map(this::entityToDTO).collect(Collectors.toList());
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
        List<Object[]> result = productRepository.findFirst6ByOrderByReviewAvgDesc(PageRequest.of(0,6));
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
        Double reviewAvg = (Double) result.get(0)[1];
        return objectsToDTO(product, reviewAvg);
    }

    @Override
    public List<ProductDTO> getProductDiscount() {
        List<Product> result = productRepository.findByDiscountIs(true);
        return result.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductByCategory(int category) {
        List<Product> result = productRepository.findByCategoryIs(category);
        return result.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductDiscountByCategory(int category) {
        List<Product> result = productRepository.findByDiscountIsTrueAndCategoryIs(category);
        return result.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductWithQueryDsl(PageRequestDTO pageRequestDTO) {
        String category = pageRequestDTO.getCategory();
        String keyword = pageRequestDTO.getKeyword();
        String sort = pageRequestDTO.getSort();
        List<Product> result = productRepository.searchProduct(category, keyword, sort);
        List<ProductDTO> dtoList = result.stream().map(this::entityToDTO).collect(Collectors.toList());
        return dtoList;
    }


    @Override
    public List<ProductDTO> getProductDiscountWithQueryDsl(PageRequestDTO pageRequestDTO) {
        String category = pageRequestDTO.getCategory();
        String keyword = pageRequestDTO.getKeyword();
        List<Product> result = productRepository.searchProductDiscount(category, keyword);
        List<ProductDTO> dtoList = result.stream().map(this::entityToDTO).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public PageResponseDTO<ProductDTO> getProductPagingWithQueryDsl(PageRequestDTO pageRequestDTO) {
        System.out.println("페이징dsl 서비스 메서드 실행");
        String category = pageRequestDTO.getCategory();
        String keyword = pageRequestDTO.getKeyword();
        String sort = pageRequestDTO.getSort();

        Pageable pageable = pageRequestDTO.getPageable(sort);

        Page<ProductDTO> result = productRepository.searchProductPaging(category, keyword, pageable);

        List<ProductDTO> dtoList = result.getContent();

        return new PageResponseDTO<>(pageRequestDTO, dtoList, (int) result.getTotalElements());
    }

}
