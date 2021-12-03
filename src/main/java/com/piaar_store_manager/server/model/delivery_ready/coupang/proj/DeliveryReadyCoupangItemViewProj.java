package com.piaar_store_manager.server.model.delivery_ready.coupang.proj;

import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemDto;
import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;

public interface DeliveryReadyCoupangItemViewProj {
    DeliveryReadyCoupangItemEntity getDeliveryReadyItem();
    String getProdManufacturingCode();
    String getOptionDefaultName();
    String getOptionManagementName();
    Integer getOptionStockUnit();
    String getOptionMemo();
    String getProdManagementName();
    String getOptionNosUniqueCode();

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