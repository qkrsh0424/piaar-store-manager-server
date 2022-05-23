package com.piaar_store_manager.server.domain.product_detail.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product_detail.entity.ProductDetailEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
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
    private Float detailCbm;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;
    private Integer productOptionCid;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductDetailEntity => ProductDetailGetDto
     * 
     * @param entity : ProductDetailEntity
     * @return ProductDetailGetDto
     */
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
