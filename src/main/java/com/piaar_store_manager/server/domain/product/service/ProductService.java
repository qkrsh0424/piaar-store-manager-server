package com.piaar_store_manager.server.domain.product.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product.proj.ProductProjection;
import com.piaar_store_manager.server.domain.product.repository.ProductRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    
    public ProductEntity searchOne(UUID id) {
        Optional<ProductEntity> productEntityOpt = productRepository.findById(id);

        if (productEntityOpt.isPresent()) {
            return productEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public List<ProductEntity> searchList() {
        return productRepository.findAll();
    }

    public void saveAndModify(ProductEntity entity) {
        productRepository.save(entity);
    }

    public ProductEntity saveAndGet(ProductEntity entity) {
        return productRepository.save(entity);
    }
    
    public void destroyOne(ProductEntity product) {
        productRepository.delete(product);
    }

    public List<ProductProjection.RelatedCategoryAndOptions> qfindAllFJ(Map<String, Object> params) {
        return productRepository.qfindAllFJ(params);
    }

    public Page<ProductProjection.RelatedCategoryAndOptions> qfindAllFJByPage(Map<String, Object> params, Pageable pageable) {
        return productRepository.qfindAllFJByPage(params, pageable);
    }
}
