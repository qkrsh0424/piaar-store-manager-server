package com.piaar_store_manager.server.domain.product_detail.service;

import java.util.List;
import java.util.Optional;

import com.piaar_store_manager.server.domain.product_detail.entity.ProductDetailEntity;
import com.piaar_store_manager.server.domain.product_detail.repository.ProductDetailRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductDetailService {
    private final ProductDetailRepository productDetailRepository;

    public ProductDetailEntity searchOne(Integer detailCid) {
        Optional<ProductDetailEntity> detailEntityOpt = productDetailRepository.findById(detailCid);

        if (detailEntityOpt.isPresent()) {
            return detailEntityOpt.get();
        } else {
            throw new NullPointerException();
        }
    }

    public List<ProductDetailEntity> searchAll() {
        return productDetailRepository.findAll();
    }

    /**
     * <b>DB Select Related Method.</b>
     * <p>
     * optionCid에 대응되는 product detail 데이터를 모두 조회한다.
     * 
     * @param optionCid : Integer
     * @return List::ProductDetailEntity::
     * @see ProductDetailRepository#findByProductOptionCid
     */
    public List<ProductDetailEntity> searchList(Integer optionCid) {
        return productDetailRepository.findByProductOptionCid(optionCid);
    }

    public void saveAndModify(ProductDetailEntity productDetailEntity) {
        productDetailRepository.save(productDetailEntity);
    }

    public void destroyOne(Integer detailCid) {
        productDetailRepository.findById(detailCid).ifPresent(product -> {
            productDetailRepository.delete(product);
        });
    }
}
