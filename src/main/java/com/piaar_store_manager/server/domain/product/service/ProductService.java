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

    public ProductEntity searchOne(Integer productCid) {
        Optional<ProductEntity> productEntityOpt = productRepository.findById(productCid);

        if (productEntityOpt.isPresent()) {
            return productEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }
    
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

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * categoryCid에 대응하는 product 데이터를 모두 조회한다.
     *
     * @return List::ProductEntity::
     * @see ProductRepository#findByProductCategoryCid
     */
    public List<ProductEntity> searchListByCategory(Integer categoryCid) {
        return productRepository.findByProductCategoryCid(categoryCid);
    }

    public void saveAndModify(ProductEntity entity) {
        productRepository.save(entity);
    }

    public ProductEntity saveAndGet(ProductEntity entity) {
        return productRepository.save(entity);
    }

    public void saveListAndModify(List<ProductEntity> entities) {
        productRepository.saveAll(entities);
    }

    public void destroyOne(UUID productId) {
        productRepository.findById(productId).ifPresent(product -> {
            productRepository.delete(product);
        });
    }

    public void destroyOne(ProductEntity product) {
        productRepository.delete(product);
    }

    public List<ProductProjection.RelatedCategoryAndOptions> findAllFJ(Map<String, Object> params) {
        return productRepository.qfindAllFJ(params);
    }

    public Page<ProductProjection.RelatedCategoryAndOptions> findAllFJByPage(Map<String, Object> params, Pageable pageable) {
        return productRepository.qfindAllFJByPage(params, pageable);
    }

    public ProductProjection.RelatedCategoryAndOptions qSelectProductAndOptions(UUID productId) {
        return productRepository.qSelectProductAndOptions(productId);
    }
}
