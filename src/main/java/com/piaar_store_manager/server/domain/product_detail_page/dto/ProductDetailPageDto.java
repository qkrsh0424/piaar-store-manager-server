package com.piaar_store_manager.server.domain.product_detail_page.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_detail_page.entity.ProductDetailPageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailPageDto {
    private UUID id;
    private String title;
    private String imageUrl;
    private String imageFileName;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private UUID productId;

    public static ProductDetailPageDto toDto(ProductDetailPageEntity entity) {
        ProductDetailPageDto dto = ProductDetailPageDto.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .imageUrl(entity.getImageUrl())
            .imageFileName(entity.getImageFileName())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .productId(entity.getProductId())
            .build();

        return dto;
    }
}
