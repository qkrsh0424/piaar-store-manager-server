package com.piaar_store_manager.server.domain.product_detail_page.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.piaar_store_manager.server.domain.product_detail_page.entity.ProductDetailPageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailPageDto {
    private UUID id;

    @Setter
    @NotNull
    @Size(max = 50, message = "'상세페이지 제목'은 50자 이내로 입력해주세요.")
    private String title;
    private String imageUrl;

    @NotNull
    @Size(max = 100, message = "'이미지 파일명'은 100자 이내로 입력해주세요.")
    private String imageFileName;
    private UUID productId;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;

    public static ProductDetailPageDto toDto(ProductDetailPageEntity entity) {
        ProductDetailPageDto dto = ProductDetailPageDto.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .imageUrl(entity.getImageUrl())
            .imageFileName(entity.getImageFileName())
            .productId(entity.getProductId())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .updatedBy(entity.getUpdatedBy())
            .build();

        return dto;
    }

    public static void removeBlank(ProductDetailPageDto detailPageDto) {
        if(detailPageDto == null) return;

        detailPageDto.setTitle(detailPageDto.getTitle().strip());
    }
}
