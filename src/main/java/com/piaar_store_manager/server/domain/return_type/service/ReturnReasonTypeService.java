package com.piaar_store_manager.server.domain.return_type.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.return_type.entity.ReturnReasonTypeEntity;
import com.piaar_store_manager.server.domain.return_type.repository.ReturnReasonTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnReasonTypeService {
    private final ReturnReasonTypeRepository returnReasonTypeRepository;

    public List<ReturnReasonTypeEntity> searchAll() {
        return returnReasonTypeRepository.findAll();
    }
}
