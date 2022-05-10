package com.piaar_store_manager.server.domain.erp_second_merge_header.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_second_merge_header.entity.ErpSecondMergeHeaderEntity;

@Repository
public interface ErpSecondMergeHeaderRepository extends JpaRepository<ErpSecondMergeHeaderEntity, Integer> {
    Optional<ErpSecondMergeHeaderEntity> findById(UUID headerId);
}
