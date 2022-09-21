package com.piaar_store_manager.server.domain.return_product_image.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.domain.return_product_image.entity.ReturnProductImageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter @Setter
@ToString
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProductImageDto {
    private UUID id;
    private String imageUrl;
    private String imageFileName;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private UUID productOptionId;
    private UUID erpReturnItemId;

    public static ReturnProductImageDto toDto(ReturnProductImageEntity entity) {
        ReturnProductImageDto dto = ReturnProductImageDto.builder()
            .id(entity.getId())
            .imageUrl(entity.getImageUrl())
            .imageFileName(entity.getImageFileName())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .productOptionId(entity.getProductOptionId())
            .erpReturnItemId(entity.getErpReturnItemId())
            .build();

        return dto;
    }
}
