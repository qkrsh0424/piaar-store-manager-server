package com.piaar_store_manager.server.domain.product_category.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomUniqueKeyUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCategoryBusinessService {
    private final ProductCategoryService productCategoryService;
    private final UserService userService;

    public List<ProductCategoryGetDto> searchAll(){
        List<ProductCategoryEntity> productCategoryEntities = productCategoryService.searchAll();
        List<ProductCategoryGetDto> productCategoryGetDtos = productCategoryEntities.stream().map(entity -> ProductCategoryGetDto.toDto(entity)).collect(Collectors.toList());
        return productCategoryGetDtos;
    }

    @Transactional
    public void createOne(ProductCategoryGetDto dto) {
        UUID USER_ID = userService.getUserId();
        ProductCategoryGetDto.removeBlank(dto);

        ProductCategoryEntity entity = ProductCategoryEntity.builder()
            .id(UUID.randomUUID())
            .code(CustomUniqueKeyUtils.generateCode18())
            .name(dto.getName())
            .createdAt(LocalDateTime.now())
            .createdBy(USER_ID)
            .updatedAt(LocalDateTime.now())
            .updatedBy(USER_ID)
            .build();

        productCategoryService.saveAndModify(entity);
    }

    @Transactional
    public void changeOne(ProductCategoryGetDto dto) {
        UUID USER_ID = userService.getUserId();
        ProductCategoryGetDto.removeBlank(dto);

        ProductCategoryEntity entity = productCategoryService.searchOne(dto.getId());

        if(dto.getName() != null) {
            entity.setName(dto.getName());
        }

        entity.setUpdatedAt(LocalDateTime.now()).setUpdatedBy(USER_ID);
    }

    @Transactional
    public void deleteOne(UUID id) {
        ProductCategoryEntity entity = productCategoryService.searchOne(id);
        productCategoryService.deleteOne(entity);
    }
}
