package com.piaar_store_manager.server.model.product_detail.dto;

import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.model.product_detail.entity.ProductDetailEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailGetDto {
    private Integer cid;
    private UUID id;
    private Integer detailWidth;
    private Integer detailLength;
    private Integer detailHeight;
    private Integer detailQuantity;
    private Integer detailWeight;
    private String detailCbm;
    private Date createdAt;
    private UUID createdBy;
    private Date updatedAt;
    private UUID updatedBy;
    private Integer productOptionCid;

    public static ProductDetailGetDto toDto(ProductDetailEntity entity) {
        ProductDetailGetDto dto = ProductDetailGetDto.builder()
            .cid(entity.getCid())
            .id(entity.getId())
            .detailWidth(entity.getDetailWidth())
            .detailLength(entity.getDetailLength())
            .detailHeight(entity.getDetailHeight())
            .detailQuantity(entity.getDetailQuantity())
            .detailWeight(entity.getDetailWeight())
            .detailCbm(entity.getDetailCbm())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .updatedBy(entity.getUpdatedBy())
            .productOptionCid(entity.getProductOptionCid())
            .build();

        return dto;
    }
}
