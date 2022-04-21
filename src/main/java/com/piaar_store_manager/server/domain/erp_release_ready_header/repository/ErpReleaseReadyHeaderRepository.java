package com.piaar_store_manager.server.domain.erp_release_ready_header.repository;

import com.piaar_store_manager.server.domain.erp_release_ready_header.entity.ErpReleaseReadyHeaderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpReleaseReadyHeaderRepository extends JpaRepository<ErpReleaseReadyHeaderEntity, Integer> {

}
