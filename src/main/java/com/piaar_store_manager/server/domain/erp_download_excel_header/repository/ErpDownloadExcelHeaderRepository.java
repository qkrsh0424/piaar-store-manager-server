package com.piaar_store_manager.server.domain.erp_download_excel_header.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_download_excel_header.entity.ErpDownloadExcelHeaderEntity;

@Repository
public interface ErpDownloadExcelHeaderRepository extends JpaRepository<ErpDownloadExcelHeaderEntity, Integer> {
    Optional<ErpDownloadExcelHeaderEntity> findById(UUID headerId);
}
