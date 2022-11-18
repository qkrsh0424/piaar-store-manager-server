package com.piaar_store_manager.server.domain.product_detail_page.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piaar_store_manager.server.domain.product_detail_page.dto.ProductDetailPageDto;
import com.piaar_store_manager.server.domain.product_detail_page.entity.ProductDetailPageEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductDetailPageBusinessService {
    private final ProductDetailPageService productDetailPageService;
    private final UserService userService;

    public List<ProductDetailPageDto> searchBatch(UUID productId) {
        List<ProductDetailPageEntity> entities = productDetailPageService.searchBatch(productId);
        List<ProductDetailPageDto> dtos = entities.stream().map(entity -> ProductDetailPageDto.toDto(entity)).collect(Collectors.toList());
        return dtos;
    }

    @Transactional
    public void createOne(ProductDetailPageDto dto) {
        UUID USER_ID = userService.getUserId();

        ProductDetailPageEntity entity = ProductDetailPageEntity.builder()
            .id(UUID.randomUUID())
            .title(dto.getTitle() != null ? dto.getTitle().strip() : null)
            .imageUrl(dto.getImageUrl())
            .imageFileName(dto.getImageFileName())
            .productId(dto.getProductId())
            .createdAt(CustomDateUtils.getCurrentDateTime())
            .createdBy(USER_ID)
            .updatedAt(CustomDateUtils.getCurrentDateTime())
            .updatedBy(USER_ID)
            .build();

        productDetailPageService.saveAndModify(entity);
    }

    @Transactional
    public void deleteOne(UUID pageId) {
        ProductDetailPageEntity entity = productDetailPageService.searchOne(pageId);
        productDetailPageService.deleteOne(entity);
    }

    @Transactional
    public void updateOne(ProductDetailPageDto dto) {
        UUID USER_ID = userService.getUserId();

        ProductDetailPageEntity entity = productDetailPageService.searchOne(dto.getId());
        entity.setTitle(dto.getTitle() != null ? dto.getTitle().strip() : null)
            .setImageUrl(dto.getImageUrl())
            .setImageFileName(dto.getImageFileName())
            .setUpdatedAt(CustomDateUtils.getCurrentDateTime())
            .setUpdatedBy(USER_ID);

        productDetailPageService.saveAndModify(entity);
    }
}
