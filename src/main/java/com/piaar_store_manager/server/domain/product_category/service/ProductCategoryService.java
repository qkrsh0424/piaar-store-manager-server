package com.piaar_store_manager.server.domain.product_category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_category.repository.ProductCategoryRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryEntity searchOne(UUID id) {
        Optional<ProductCategoryEntity> entityOpt = productCategoryRepository.findById(id);

        if (entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public List<ProductCategoryEntity> searchList(){
        return productCategoryRepository.findAll();
    }

    public void saveAndModify(ProductCategoryEntity entity) {
        productCategoryRepository.save(entity);
    }
}
