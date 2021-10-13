package com.piaar_store_manager.server.model.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.model.product.entity.ProductEntity;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductGetDto {
    private Integer cid;
    private UUID id;
    private String code;
    private String manufacturingCode;
    private String naverProductCode;
    private String defaultName;
    private String managementName;
    private String imageUrl;
    private String imageFileName;
    private String memo;
    private Date createdAt;
    private UUID createdBy;
    private Date updatedAt;
    private UUID updatedBy;
    private Integer productCategoryCid;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductEntity => ProductGetDto
     * 
     * @param productEntity : ProductEntity
     * @return ProductGetDto
     */
    public static ProductGetDto toDto(ProductEntity entity) {
        ProductGetDto productDto = ProductGetDto.builder()
            .cid(entity.getCid())
            .id(entity.getId())
            .code(entity.getCode())
            .manufacturingCode(entity.getManufacturingCode())
            .naverProductCode(entity.getNaverProductCode())
            .defaultName(entity.getDefaultName())
            .managementName(entity.getManagementName())
            .imageUrl(entity.getImageUrl())
            .imageFileName(entity.getImageFileName())
            .memo(entity.getMemo())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .updatedBy(entity.getUpdatedBy())
            .productCategoryCid(entity.getProductCategoryCid())
            .build();

        return productDto;
    }
}
