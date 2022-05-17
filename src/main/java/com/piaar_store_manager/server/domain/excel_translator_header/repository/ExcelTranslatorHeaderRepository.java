package com.piaar_store_manager.server.domain.excel_translator_header.repository;

import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.excel_translator_header.entity.ExcelTranslatorHeaderEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelTranslatorHeaderRepository extends JpaRepository<ExcelTranslatorHeaderEntity, Integer> {
    public Optional<ExcelTranslatorHeaderEntity> findById(UUID id);
}
