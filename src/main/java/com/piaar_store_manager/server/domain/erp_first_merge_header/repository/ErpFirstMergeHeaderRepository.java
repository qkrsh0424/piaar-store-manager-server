package com.piaar_store_manager.server.domain.erp_first_merge_header.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_first_merge_header.entity.ErpFirstMergeHeaderEntity;

@Repository
public interface ErpFirstMergeHeaderRepository extends JpaRepository<ErpFirstMergeHeaderEntity, Integer> {
    Optional<ErpFirstMergeHeaderEntity> findById(UUID headerId);
}
