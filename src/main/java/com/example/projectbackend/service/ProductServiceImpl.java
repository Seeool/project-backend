package com.example.projectbackend.service;

import com.example.projectbackend.domain.Product;
import com.example.projectbackend.dto.ProductDTO;
import com.example.projectbackend.repository.ProductRepository;
import com.example.projectbackend.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

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
                product.addImage(arr[0], arr[1]);
            }
        }
        productRepository.save(product);
    }

    @Override
    public void delete(Long pid) {
        reviewRepository.deleteByProduct_Pid(pid);
        productRepository.deleteById(pid);
    }
}
