package com.piaar_store_manager.server.model.product_release.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewDto;
import com.piaar_store_manager.server.model.product_release.entity.ProductReleaseEntity;

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
public class ProductReleaseGetDto {
    private Integer cid;
    private UUID id;
    private Integer releaseUnit;
    private String memo;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Integer productOptionCid;
    private UUID productOptionId;
    private UUID erpOrderItemId;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReleaseEntity => ProductReleaseGetDto
     * 
     * @param entity : ProductReleaseEntity
     * @return ProductReleaseGetDto
     */
    public static ProductReleaseGetDto toDto(ProductReleaseEntity entity) {
        ProductReleaseGetDto dto = ProductReleaseGetDto.builder()
           .cid(entity.getCid())
           .id(entity.getId())
           .releaseUnit(entity.getReleaseUnit())
           .memo(entity.getMemo())
           .createdAt(entity.getCreatedAt())
           .createdBy(entity.getCreatedBy())
           .productOptionCid(entity.getProductOptionCid())
           .productOptionId(entity.getProductOptionId())
           .erpOrderItemId(entity.getErpOrderItemId())
           .build();

        return dto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyCoupangItemViewDto => ProductReleaseGetDto
     * 
     * @param reqDto : DeliveryReadyCoupangItemViewDto
     * @param optionCid : int
     * @return ProductReleaseGetDto
     */
    public static ProductReleaseGetDto toDto(DeliveryReadyCoupangItemViewDto reqDto, int optionCid){
        ProductReleaseGetDto dto = ProductReleaseGetDto.builder()
            .id(UUID.randomUUID())
            .releaseUnit(reqDto.getDeliveryReadyItem().getUnit())
            .memo(reqDto.getReleaseMemo())
            .productOptionCid(optionCid)
            .build();
        
        return dto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyNaverItemViewDto => ProductReleaseGetDto
     * 
     * @param reqDto : DeliveryReadyNaverItemViewDto
     * @param optionCid : int
     * @return ProductReleaseGetDto
     */
    public static ProductReleaseGetDto toDto(DeliveryReadyNaverItemViewDto reqDto, int optionCid){
        ProductReleaseGetDto dto = ProductReleaseGetDto.builder()
            .id(UUID.randomUUID())
            .releaseUnit(reqDto.getDeliveryReadyItem().getUnit())
            .memo(reqDto.getReleaseMemo())
            .productOptionCid(optionCid)
            .build();
        
        return dto;
    }
}
