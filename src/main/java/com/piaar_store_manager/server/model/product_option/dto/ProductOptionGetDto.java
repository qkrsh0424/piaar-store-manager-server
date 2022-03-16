package com.piaar_store_manager.server.model.product_option.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionGetDto {
    private Integer cid;
    private UUID id;
    private String code;
    private String nosUniqueCode;
    private String defaultName;
    private String managementName;
    private Integer salesPrice;
    private Integer stockUnit;
    private Integer totalPurchasePrice;
    private String status;
    private String memo;
    private String imageUrl;
    private String imageFileName;
    private String color;
    private String unitCny;
    private String unitKrw;
    private Date createdAt;
    private UUID createdBy;
    private Date updatedAt;
    private UUID updatedBy;
    private Integer productCid;
    private UUID productId;
    private Integer releasedSumUnit;
    private Integer receivedSumUnit;
    private Integer stockSumUnit;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductOptionEntity => ProductOptionGetDto
     * 
     * @param entity : ProductOptionEntity
     * @return ProductOptionGetDto
     */
    public static ProductOptionGetDto toDto(ProductOptionEntity entity) {
        ProductOptionGetDto dto = ProductOptionGetDto.builder()
                .cid(entity.getCid())
                .id(entity.getId())
                .code(entity.getCode())
                .nosUniqueCode(entity.getNosUniqueCode())
                .defaultName(entity.getDefaultName())
                .managementName(entity.getManagementName())
                .salesPrice(entity.getSalesPrice())
                .totalPurchasePrice(entity.getTotalPurchasePrice())
                .stockUnit(entity.getStockUnit())
                .status(entity.getStatus())
                .memo(entity.getMemo())
                .imageUrl(entity.getImageUrl())
                .imageFileName(entity.getImageFileName())
                .color(entity.getColor())
                .unitCny(entity.getUnitCny())
                .unitKrw(entity.getUnitKrw())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .productCid(entity.getProductCid())
                .build();

        return dto;
    }
}