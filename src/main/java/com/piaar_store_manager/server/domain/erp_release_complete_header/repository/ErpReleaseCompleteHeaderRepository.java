package com.piaar_store_manager.server.domain.erp_release_complete_header.repository;

import com.piaar_store_manager.server.domain.erp_release_complete_header.entity.ErpReleaseCompleteHeaderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpReleaseCompleteHeaderRepository extends JpaRepository<ErpReleaseCompleteHeaderEntity, Integer> {

}
