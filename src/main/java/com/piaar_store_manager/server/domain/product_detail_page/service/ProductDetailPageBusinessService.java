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
            .title(dto.getTitle())
            .imageUrl(dto.getImageUrl())
            .imageFileName(dto.getImageFileName())
            .createdAt(LocalDateTime.now())
            .createdBy(USER_ID)
            .productId(dto.getProductId())
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
        ProductDetailPageEntity entity = productDetailPageService.searchOne(dto.getId());
        entity.setTitle(dto.getTitle())
            .setImageUrl(dto.getImageUrl())
            .setImageFileName(dto.getImageFileName());

        productDetailPageService.saveAndModify(entity);
    }
}
