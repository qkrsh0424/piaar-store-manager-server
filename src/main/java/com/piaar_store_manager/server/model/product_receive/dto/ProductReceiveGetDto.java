package com.piaar_store_manager.server.model.product_receive.dto;

import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewDto;

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

    public static ProductReceiveGetDto toDto(DeliveryReadyCoupangItemViewDto reqDto, int optionCid){
        ProductReceiveGetDto dto = ProductReceiveGetDto.builder()
            .id(UUID.randomUUID())
            .receiveUnit(reqDto.getDeliveryReadyItem().getUnit())
            .memo(reqDto.getReceiveMemo())
            .productOptionCid(optionCid)
            .build();
        
        return dto;
    }

    public static ProductReceiveGetDto toDto(DeliveryReadyNaverItemViewDto reqDto, int optionCid){
        ProductReceiveGetDto dto = ProductReceiveGetDto.builder()
            .id(UUID.randomUUID())
            .receiveUnit(reqDto.getDeliveryReadyItem().getUnit())
            .memo(reqDto.getReceiveMemo())
            .productOptionCid(optionCid)
            .build();
        
        return dto;
    }
}
