package com.piaar_store_manager.server.model.product_receive.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewDto;
import com.piaar_store_manager.server.model.product_receive.entity.ProductReceiveEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductReceiveGetDto {
    private Integer cid;
    private UUID id;
    private Integer receiveUnit;
    private String memo;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Integer productOptionCid;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReceiveEntity => ProductReceiveGetDto
     * 
     * @param entity : ProductReceiveEntity
     * @return ProductReceiveGetDto
     */
    public static ProductReceiveGetDto toDto(ProductReceiveEntity entity) {
        ProductReceiveGetDto dto = ProductReceiveGetDto.builder()
            .cid(entity.getCid())
            .id(entity.getId())
            .receiveUnit(entity.getReceiveUnit())
            .memo(entity.getMemo())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .productOptionCid(entity.getProductOptionCid())
            .build();

        return dto;
    }
}
