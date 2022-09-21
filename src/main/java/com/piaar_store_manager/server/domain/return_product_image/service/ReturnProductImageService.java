package com.piaar_store_manager.server.domain.return_product_image.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piaar_store_manager.server.domain.return_product_image.entity.ReturnProductImageEntity;
import com.piaar_store_manager.server.domain.return_product_image.repository.ReturnProductImageRepository;
import com.piaar_store_manager.server.exception.CustomNotFoundDataException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnProductImageService {
    private final ReturnProductImageRepository returnProductImageRepository;

    public ReturnProductImageEntity searchOne(UUID id) {
        Optional<ReturnProductImageEntity> entityOpt = returnProductImageRepository.findById(id);

        if(entityOpt.isPresent()) {
            return entityOpt.get();
        }else {
            throw new CustomNotFoundDataException("존재하지 않는 데이터입니다.");
        }
    }

    @Transactional
    public void saveListAndModify(List<ReturnProductImageEntity> entities) {
        returnProductImageRepository.saveAll(entities);
    }

    public List<ReturnProductImageEntity> findAllByErpReturnItemId(UUID erpReturnItemId) {
        return returnProductImageRepository.findByErpReturnItemId(erpReturnItemId);
    }

    public void deleteOne(ReturnProductImageEntity entity) {
        returnProductImageRepository.delete(entity);
    }
}
