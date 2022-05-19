package com.piaar_store_manager.server.domain.delivery_ready.naver.dto;

import com.piaar_store_manager.server.domain.delivery_ready.naver.proj.DeliveryReadyNaverItemViewProj;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeliveryReadyNaverItemViewResDto {
    DeliveryReadyNaverItemDto deliveryReadyItem;
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
     * DeliveryReadyNaverItemViewProj => DeliveryReadyNaverItemViewResDto
     * 
     * @param itemViewProj : DeliveryReadyNaverItemViewProj
     * @return DeliveryReadyNaverItemViewResDto
     */
    public static DeliveryReadyNaverItemViewResDto toResDto(DeliveryReadyNaverItemViewProj itemViewProj) {
        DeliveryReadyNaverItemViewResDto dto = new DeliveryReadyNaverItemViewResDto();

        dto.setDeliveryReadyItem(DeliveryReadyNaverItemDto.toDto(itemViewProj.getDeliveryReadyItem()))
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
