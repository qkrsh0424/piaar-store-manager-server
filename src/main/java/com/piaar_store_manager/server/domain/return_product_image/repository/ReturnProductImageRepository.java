package com.piaar_store_manager.server.domain.return_product_image.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.return_product_image.entity.ReturnProductImageEntity;

@Repository
public interface ReturnProductImageRepository extends JpaRepository<ReturnProductImageEntity, Integer> {
    List<ReturnProductImageEntity> findByErpReturnItemId(UUID erpReturnItemId);
    Optional<ReturnProductImageEntity> findById(UUID id);
}
