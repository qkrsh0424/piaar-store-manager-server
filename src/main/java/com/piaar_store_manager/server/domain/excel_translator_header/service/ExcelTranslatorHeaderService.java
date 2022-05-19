package com.piaar_store_manager.server.domain.excel_translator_header.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.domain.excel_translator_header.entity.ExcelTranslatorHeaderEntity;
import com.piaar_store_manager.server.domain.excel_translator_header.repository.ExcelTranslatorHeaderRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelTranslatorHeaderService {
    private final ExcelTranslatorHeaderRepository excelTranslatorHeaderRepository;

    public void saveAndModify(ExcelTranslatorHeaderEntity entity) {
        excelTranslatorHeaderRepository.save(entity);
    }

    public ExcelTranslatorHeaderEntity searchOne(UUID headerId) {
        Optional<ExcelTranslatorHeaderEntity> headerEntityOpt = excelTranslatorHeaderRepository.findById(headerId);

        if (headerEntityOpt.isPresent()) {
            return headerEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    public List<ExcelTranslatorHeaderEntity> searchList() {
        return excelTranslatorHeaderRepository.findAll();
    }

    public void destroyOne(UUID headerId) {
        excelTranslatorHeaderRepository.findById(headerId).ifPresent(header -> {
            excelTranslatorHeaderRepository.delete(header);
        });
    }
}
