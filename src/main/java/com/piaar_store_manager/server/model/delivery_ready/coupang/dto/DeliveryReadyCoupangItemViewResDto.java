package com.piaar_store_manager.server.model.delivery_ready.coupang.dto;

import com.piaar_store_manager.server.model.delivery_ready.coupang.proj.DeliveryReadyCoupangItemViewProj;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeliveryReadyCoupangItemViewResDto {
    DeliveryReadyCoupangItemDto deliveryReadyItem;
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

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyCoupangItemViewProj => DeliveryReadyCoupangItemViewResDto
     * 
     * @param itemViewProj : DeliveryReadyCoupangItemViewProj
     * @return DeliveryReadyCoupangItemViewResDto
     */
    public static DeliveryReadyCoupangItemViewResDto toResDto(DeliveryReadyCoupangItemViewProj itemViewProj) {
        DeliveryReadyCoupangItemViewResDto dto = new DeliveryReadyCoupangItemViewResDto();

        dto.setDeliveryReadyItem(DeliveryReadyCoupangItemDto.toDto(itemViewProj.getDeliveryReadyItem()))
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
