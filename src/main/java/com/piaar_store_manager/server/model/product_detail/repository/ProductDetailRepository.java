package com.piaar_store_manager.server.model.product_detail.repository;

import java.util.List;

import com.piaar_store_manager.server.model.product_detail.entity.ProductDetailEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetailEntity, Integer> {
    List<ProductDetailEntity> findByProductOptionCid(Integer productOptionCid);
}
