package com.piaar_store_manager.server.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product.proj.ProductProj;
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

    public ProductEntity searchOne(UUID productId) {
        Optional<ProductEntity> productEntityOpt = productRepository.findById(productId);

        if (productEntityOpt.isPresent()) {
            return productEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    // public List<ProductEntity> searchList() {
    //     return productRepository.findAll();
    // }

    public List<ProductEntity> searchAll() {
        return productRepository.findAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * productCid에 대응되는 product, product와 Many To One JOIN(m2oj) 연관관계에 놓여있는 user, category를 함께 조회한다.
     *
     * @param productCid : Integer
     * @return ProductProj
     * @see ProductRepository#searchOneM2OJ
     */
    // public ProductProj searchOneM2OJ(Integer productCid) {
    //     Optional<ProductProj> productProjOpt = productRepository.searchOneM2OJ(productCid);

    //     if(productProjOpt.isPresent()) {
    //         return productProjOpt.get();
    //     } else {
    //         throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
    //     }
    // }
    public ProductProj searchOneM2OJ(UUID productId) {
        Optional<ProductProj> productProjOpt = productRepository.searchOneM2OJ(productId);

        if(productProjOpt.isPresent()) {
            return productProjOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * 모든 product, product와 Many To One JOIN(m2oj) 연관관계에 놓여있는 user, category를 함께 조회한다.
     *
     * @return List::ProductProj::
     * @see ProductRepository#selectAll
     */
    public List<ProductProj> searchListM2OJ(){
        return productRepository.selectAll();
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * category cid에 대응하는 product 데이터를 모두 조회한다.
     *
     * @return List::ProductEntity::
     * @see ProductRepository#findByCategoryCid
     */
    public List<ProductEntity> searchBatchByCategoryCid(Integer categoryCid) {
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

    public void destroyOne(ProductEntity product) {
        productRepository.delete(product);
    }
}
