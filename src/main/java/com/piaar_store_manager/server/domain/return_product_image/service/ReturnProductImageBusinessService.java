package com.piaar_store_manager.server.domain.return_product_image.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piaar_store_manager.server.domain.return_product_image.dto.ReturnProductImageDto;
import com.piaar_store_manager.server.domain.return_product_image.entity.ReturnProductImageEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnProductImageBusinessService {
    private final ReturnProductImageService returnProductImageService;
    private final UserService userService;

    public List<ReturnProductImageDto> searchBatchByErpReturnId(UUID erpReturnItemId) {
        List<ReturnProductImageEntity> entities = returnProductImageService.findAllByErpReturnItemId(erpReturnItemId);
        List<ReturnProductImageDto> dtos = entities.stream().map(entity -> ReturnProductImageDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    @Transactional
    public void createBatch(List<ReturnProductImageDto> dtos) {
        UUID USER_ID = userService.getUserId();
        
        List<ReturnProductImageEntity> imageEntities = dtos.stream().map(dto -> {
            ReturnProductImageEntity entity = ReturnProductImageEntity.builder()
                .id(UUID.randomUUID())
                .imageUrl(dto.getImageUrl())
                .imageFileName(dto.getImageFileName())
                .createdAt(LocalDateTime.now())
                .createdBy(USER_ID)
                .productOptionId(dto.getProductOptionId())
                .erpReturnItemId(dto.getErpReturnItemId())
                .build();

            return entity;
        }).collect(Collectors.toList());

        returnProductImageService.saveListAndModify(imageEntities);
    }

    @Transactional
    public void deleteOne(UUID imageId) {
        ReturnProductImageEntity entity = returnProductImageService.searchOne(imageId);
        returnProductImageService.deleteOne(entity);
    }

}
