package com.piaar_store_manager.server.domain.product_detail_page.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product_detail_page.entity.ProductDetailPageEntity;

@Repository
public interface ProductDetailPageRepository extends JpaRepository<ProductDetailPageEntity, Integer> {
    List<ProductDetailPageEntity> findByProductId(UUID productId);
    Optional<ProductDetailPageEntity> findById(UUID id);
}
