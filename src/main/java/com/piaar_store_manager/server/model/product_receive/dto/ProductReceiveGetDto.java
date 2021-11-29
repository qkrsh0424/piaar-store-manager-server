package com.piaar_store_manager.server.model.product_receive.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private Date createdAt;
    private UUID createdBy;
    private Integer productOptionCid;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReceiveEntity => ProductReceiveGetDto
     * 
     * @param productReceiveEntity : ProductReceiveEntity
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

    /**
     * <b>Convert Method</b>
     * <p>
     * List::ProductReceiveEntity:: => List::ProductReceiveGetDto::
     * 
     * @param entities : List::ProductReceiveEntity::
     * @return List::ProductReceiveGetDto::
     */
    public static List<ProductReceiveGetDto> toDtos(List<ProductReceiveEntity> entities) {
        List<ProductReceiveGetDto> dtos = entities.stream().map(entity -> {
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
        }).collect(Collectors.toList());
        
        return dtos;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyCoupangItemViewDto => ProductReceiveGetDto
     * 
     * @param reqDto : DeliveryReadyCoupangItemViewDto
     * @param optionCid : Integer
     * @return ProductReceiveGetDto
     */
    public static ProductReceiveGetDto toDto(DeliveryReadyCoupangItemViewDto reqDto, Integer optionCid){
        ProductReceiveGetDto dto = ProductReceiveGetDto.builder()
            .id(UUID.randomUUID())
            .receiveUnit(reqDto.getDeliveryReadyItem().getUnit())
            .memo(reqDto.getReceiveMemo())
            .productOptionCid(optionCid)
            .build();
        
        return dto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyNaverItemViewDto => ProductReceiveGetDto
     * 
     * @param reqDto : DeliveryReadyNaverItemViewDto
     * @param optionCid : Integer
     * @return ProductReceiveGetDto
     */
    public static ProductReceiveGetDto toDto(DeliveryReadyNaverItemViewDto reqDto, Integer optionCid){
        ProductReceiveGetDto dto = ProductReceiveGetDto.builder()
            .id(UUID.randomUUID())
            .receiveUnit(reqDto.getDeliveryReadyItem().getUnit())
            .memo(reqDto.getReceiveMemo())
            .productOptionCid(optionCid)
            .build();
        
        return dto;
    }
}
