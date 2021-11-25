package com.piaar_store_manager.server.model.product_detail.repository;

import java.util.List;

import com.piaar_store_manager.server.model.product_detail.entity.ProductDetailEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetailEntity, Integer> {
    
    /**
     * ProductOption cid에 대응하는 상품 상세 데이터를 조회한다.
     * 
     * @param productOptionCid : Integer
     * @return List::ProductDetailEntity::
     */
    List<ProductDetailEntity> findByProductOptionCid(Integer productOptionCid);
}
