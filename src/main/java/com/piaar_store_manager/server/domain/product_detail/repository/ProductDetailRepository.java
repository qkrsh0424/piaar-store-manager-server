package com.piaar_store_manager.server.domain.product_detail.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_detail.entity.ProductDetailEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetailEntity, Integer> {
    
    Optional<ProductDetailEntity> findById(UUID id);

    /**
     * productOptionCid에 대응하는 product detail 데이터를 조회한다.
     * 
     * @param productOptionCid : Integer
     * @return List::ProductDetailEntity::
     */
    List<ProductDetailEntity> findByProductOptionCid(Integer productOptionCid);
}
