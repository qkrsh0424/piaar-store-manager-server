package com.piaar_store_manager.server.model.delivery_ready.coupang.proj;

import java.util.ArrayList;
import java.util.List;

import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemDto;
import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;

public interface DeliveryReadyCoupangItemViewProj {
    DeliveryReadyCoupangItemEntity getDeliveryReadyItem();
    String getProdManufacturingCode();
    String getOptionDefaultName();
    String getOptionManagementName();
    Integer getOptionStockUnit();
    String getProdManagementName();
    String getOptionNosUniqueCode();

    public static DeliveryReadyCoupangItemViewResDto toResDto(DeliveryReadyCoupangItemViewProj itemViewProj) {
        DeliveryReadyCoupangItemViewResDto dto = new DeliveryReadyCoupangItemViewResDto();

        dto.setDeliveryReadyItem(DeliveryReadyCoupangItemDto.toDto(itemViewProj.getDeliveryReadyItem()))
            .setProdManufacturingCode(itemViewProj.getProdManufacturingCode())
            .setProdManagementName(itemViewProj.getProdManagementName())
            .setOptionDefaultName(itemViewProj.getOptionDefaultName())
            .setOptionManagementName(itemViewProj.getOptionManagementName())
            .setOptionStockUnit(itemViewProj.getOptionStockUnit())
            .setOptionNosUniqueCode(itemViewProj.getOptionNosUniqueCode());

        return dto;
    }

    public static List<DeliveryReadyCoupangItemViewResDto> toResDtos(List<DeliveryReadyCoupangItemViewProj> itemViewProj) {
        List<DeliveryReadyCoupangItemViewResDto> dtos = new ArrayList<>();

        for(DeliveryReadyCoupangItemViewProj proj : itemViewProj) {
            dtos.add(toResDto(proj));
        }
        
        return dtos;
    }
}