package com.piaar_store_manager.server.model.delivery_ready.piaar.dto;

import com.piaar_store_manager.server.model.delivery_ready.piaar.proj.DeliveryReadyPiaarItemViewProj;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeliveryReadyPiaarItemViewResDto {
    DeliveryReadyPiaarItemDto deliveryReadyItem;
    String prodManufacturingCode;
    String prodManagementName;
    String optionDefaultName;
    String optionManagementName;
    Integer optionStockUnit;
    String optionMemo;
    String sender;
    String senderContact1;
    String optionNosUniqueCode;
    String releaseMemo;
    String receiveMemo;

    public static DeliveryReadyPiaarItemViewResDto toResDto(DeliveryReadyPiaarItemViewProj itemViewProj) {
        DeliveryReadyPiaarItemViewResDto dto = new DeliveryReadyPiaarItemViewResDto();

        dto.setDeliveryReadyItem(DeliveryReadyPiaarItemDto.toDto(itemViewProj.getDeliveryReadyItem()))
            .setProdManufacturingCode(itemViewProj.getProdManufacturingCode())
            .setProdManagementName(itemViewProj.getProdManagementName())
            .setOptionDefaultName(itemViewProj.getOptionDefaultName())
            .setOptionManagementName(itemViewProj.getOptionManagementName())
            .setOptionStockUnit(itemViewProj.getOptionStockUnit())
            .setOptionMemo(itemViewProj.getOptionMemo())
            .setOptionNosUniqueCode(itemViewProj.getOptionNosUniqueCode());

        return dto;
    }
}
