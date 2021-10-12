package com.piaar_store_manager.server.model.product_release.dto;

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
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReleaseGetDto {
    private Integer cid;
    private UUID id;
    private Integer releaseUnit;
    private String memo;
    private Date createdAt;
    private UUID createdBy;
    private Integer productOptionCid;

    public static ProductReleaseGetDto toDto(DeliveryReadyCoupangItemViewDto reqDto, int optionCid){
        ProductReleaseGetDto dto = ProductReleaseGetDto.builder()
            .id(UUID.randomUUID())
            .releaseUnit(reqDto.getDeliveryReadyItem().getUnit())
            .memo(reqDto.getReleaseMemo())
            .productOptionCid(optionCid)
            .build();
        
        return dto;
    }

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
