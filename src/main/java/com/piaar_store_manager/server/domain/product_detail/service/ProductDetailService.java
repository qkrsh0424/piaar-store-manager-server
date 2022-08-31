package com.piaar_store_manager.server.domain.product_detail.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_detail.entity.ProductDetailEntity;
import com.piaar_store_manager.server.domain.product_detail.repository.ProductDetailRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductDetailService {
    private final ProductDetailRepository productDetailRepository;

    // deprecated
    public ProductDetailEntity searchOne(Integer detailCid) {
        Optional<ProductDetailEntity> detailEntityOpt = productDetailRepository.findById(detailCid);

        if (detailEntityOpt.isPresent()) {
            return detailEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public ProductDetailEntity searchOne(UUID detailId) {
        Optional<ProductDetailEntity> detailEntityOpt = productDetailRepository.findById(detailId);

        if (detailEntityOpt.isPresent()) {
            return detailEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public List<ProductDetailEntity> searchAll() {
        return productDetailRepository.findAll();
    }

    public List<ProductDetailEntity> searchListByOptionCid(Integer optionCid) {
        return productDetailRepository.findByProductOptionCid(optionCid);
    }

    public void saveAndModify(ProductDetailEntity productDetailEntity) {
        productDetailRepository.save(productDetailEntity);
    }

    // deprecated
    public void destroyOne(Integer detailCid) {
        productDetailRepository.findById(detailCid).ifPresent(product -> {
            productDetailRepository.delete(product);
        });
    }

    public void destroyOne(UUID detailId) {
        productDetailRepository.findById(detailId).ifPresent(detail -> {
            productDetailRepository.delete(detail);
        });
    }
}
