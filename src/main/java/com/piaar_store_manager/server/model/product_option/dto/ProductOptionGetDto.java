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
     * @param productOptionEntity : ProductOptionEntity
     * @return ProductOptionGetDto
     */
    public static ProductOptionGetDto toDto(ProductOptionEntity productOptionEntity) {
        ProductOptionGetDto productOptionDto = ProductOptionGetDto.builder()
                .cid(productOptionEntity.getCid())
                .id(productOptionEntity.getId())
                .code(productOptionEntity.getCode())
                .nosUniqueCode(productOptionEntity.getNosUniqueCode())
                .defaultName(productOptionEntity.getDefaultName())
                .managementName(productOptionEntity.getManagementName())
                .salesPrice(productOptionEntity.getSalesPrice())
                .stockUnit(productOptionEntity.getStockUnit())
                .status(productOptionEntity.getStatus())
                .memo(productOptionEntity.getMemo())
                .imageUrl(productOptionEntity.getImageUrl())
                .imageFileName(productOptionEntity.getImageFileName())
                .color(productOptionEntity.getColor())
                .unitCny(productOptionEntity.getUnitCny())
                .unitKrw(productOptionEntity.getUnitKrw())
                .createdAt(productOptionEntity.getCreatedAt())
                .createdBy(productOptionEntity.getCreatedBy())
                .updatedAt(productOptionEntity.getUpdatedAt())
                .updatedBy(productOptionEntity.getUpdatedBy())
                .productCid(productOptionEntity.getProductCid())
                .build();

        return productOptionDto;
    }
}
