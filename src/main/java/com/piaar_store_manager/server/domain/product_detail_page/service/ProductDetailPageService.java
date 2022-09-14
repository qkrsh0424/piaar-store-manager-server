package com.piaar_store_manager.server.domain.product_detail_page.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.product_detail_page.entity.ProductDetailPageEntity;
import com.piaar_store_manager.server.domain.product_detail_page.repository.ProductDetailPageRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductDetailPageService {
    private final ProductDetailPageRepository productDetailPageRepository;

    public ProductDetailPageEntity searchOne(UUID pageId) {
        Optional<ProductDetailPageEntity> entityOpt = productDetailPageRepository.findById(pageId);

        if(entityOpt.isPresent()) {
            return entityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public List<ProductDetailPageEntity> searchBatch(UUID productId) {
        return productDetailPageRepository.findByProductId(productId);
    }

    public void saveAndModify(ProductDetailPageEntity entity) {
        productDetailPageRepository.save(entity);
    }

    public void deleteOne(ProductDetailPageEntity entity) {
        productDetailPageRepository.delete(entity);
    }
}
