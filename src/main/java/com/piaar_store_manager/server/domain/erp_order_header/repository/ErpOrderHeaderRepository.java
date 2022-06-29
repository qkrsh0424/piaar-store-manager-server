package com.piaar_store_manager.server.domain.erp_order_header.repository;


import com.piaar_store_manager.server.domain.erp_order_header.entity.ErpOrderHeaderEntity;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpOrderHeaderRepository extends JpaRepository<ErpOrderHeaderEntity, Integer> {
    Optional<ErpOrderHeaderEntity> findById(UUID id);
}
