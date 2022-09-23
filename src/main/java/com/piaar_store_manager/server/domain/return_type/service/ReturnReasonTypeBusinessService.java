package com.piaar_store_manager.server.domain.return_type.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.return_type.dto.ReturnReasonTypeDto;
import com.piaar_store_manager.server.domain.return_type.entity.ReturnReasonTypeEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnReasonTypeBusinessService {
    private final ReturnReasonTypeService returnTypeService;

    public List<ReturnReasonTypeDto> searchAll() {
        List<ReturnReasonTypeEntity> entities = returnTypeService.searchAll();
        List<ReturnReasonTypeDto> dtos = entities.stream().map(entity -> ReturnReasonTypeDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }
}
