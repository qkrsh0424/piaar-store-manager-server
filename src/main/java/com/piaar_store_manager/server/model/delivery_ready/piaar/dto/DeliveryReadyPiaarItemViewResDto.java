package com.piaar_store_manager.server.model.delivery_ready.piaar.dto;

import com.piaar_store_manager.server.model.delivery_ready.piaar.proj.DeliveryReadyPiaarItemViewProj;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class DeliveryReadyPiaarItemViewResDto {
    DeliveryReadyPiaarItemDto deliveryReadyItem;
    String prodDefaultName;
    String prodManagementName;
    String optionDefaultName;
    String optionManagementName;
    String categoryName;
    Integer optionStockUnit;

    public static DeliveryReadyPiaarItemViewResDto toResDto(DeliveryReadyPiaarItemViewProj proj) {
        DeliveryReadyPiaarItemViewResDto dto = DeliveryReadyPiaarItemViewResDto.builder()
            .deliveryReadyItem(DeliveryReadyPiaarItemDto.toDto(proj.getDeliveryReadyItem()))
            .prodDefaultName(proj.getProdDefaultName())
            .prodManagementName(proj.getProdManagementName())
            .optionDefaultName(proj.getOptionDefaultName())
            .optionManagementName(proj.getOptionManagementName())
            .categoryName(proj.getCategoryName())
            .build();

        return dto;
    }
}
