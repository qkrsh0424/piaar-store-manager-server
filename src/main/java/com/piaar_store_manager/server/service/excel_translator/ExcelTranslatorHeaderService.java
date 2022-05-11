package com.piaar_store_manager.server.service.excel_translator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.piaar_store_manager.server.exception.CustomNotFoundDataException;
import com.piaar_store_manager.server.model.excel_translator_header.entity.ExcelTranslatorHeaderEntity;
import com.piaar_store_manager.server.model.excel_translator_header.repository.ExcelTranslatorHeaderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelTranslatorHeaderService {
    
    private ExcelTranslatorHeaderRepository excelTranslatorHeaderRepository;

    @Autowired
    public ExcelTranslatorHeaderService(
        ExcelTranslatorHeaderRepository excelTranslatorHeaderRepository
    ) {
        this.excelTranslatorHeaderRepository = excelTranslatorHeaderRepository;
    }

    /**
     * <b>DB Insert Related Method</b>
     * <p>
     * ExcelTranslatorHeader 내용을 한개 등록한다.
     * 
     * @param entity : ExcelTranslatorHeaderEntity
     * @see ExcelTranslatorHeaderRepository#save
     */
    public ExcelTranslatorHeaderEntity saveOne(ExcelTranslatorHeaderEntity entity) {
        return excelTranslatorHeaderRepository.save(entity);
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ExcelTranslatorHeader id 값과 상응되는 데이터를 조회한다.
     *
     * @param headerId : UUID
     * @return ExcelTranslatorHeaderEntity
     * @see ExcelTranslatorHeaderRepository#findById
     */
    public ExcelTranslatorHeaderEntity searchOne(UUID headerId) {
        Optional<ExcelTranslatorHeaderEntity> headerEntityOpt = excelTranslatorHeaderRepository.findById(headerId);

        if (headerEntityOpt.isPresent()) {
            return headerEntityOpt.get();
        } else {
            throw new CustomNotFoundDataException("데이터를 찾을 수 없습니다.");
        }
    }

    /**
     * <b>DB Select Related Method</b>
     * <p>
     * ExcelTranslatorHeader 데이터를 모두 조회한다.
     *
     * @return List::ExcelTranslatorHeaderEntity::
     * @see ExcelTranslatorHeaderRepository#findAll
     */
    public List<ExcelTranslatorHeaderEntity> searchList() {
        return excelTranslatorHeaderRepository.findAll();
    }

    /**
     * <b>DB Delete Related Method</b>
     * <p>
     * ExcelTranslatorHeader id 값과 상응되는 데이터를 삭제한다.
     * 
     * @param headerId : UUID
     * @see ExcelTranslatorHeaderRepository#findById
     * @see ExcelTranslatorHeaderRepository#delete
     */
    public void destroyOne(UUID headerId) {
        excelTranslatorHeaderRepository.findById(headerId).ifPresent(header -> {
            excelTranslatorHeaderRepository.delete(header);
        });
    }
}
