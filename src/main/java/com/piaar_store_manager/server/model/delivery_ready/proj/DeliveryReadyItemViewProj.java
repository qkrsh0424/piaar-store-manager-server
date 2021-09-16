package com.piaar_store_manager.server.model.delivery_ready.proj;

import java.util.ArrayList;
import java.util.List;

import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyItemEntity;

public interface DeliveryReadyItemViewProj {
    DeliveryReadyItemEntity getDeliveryReadyItem();
    String getProdManufacturingCode();
    String getOptionDefaultName();
    String getOptionManagementName();
    Integer getOptionStockUnit();
    String getProdManagementName();
    String getOptionNosUniqueCode();

    public static DeliveryReadyItemViewResDto toResDto(DeliveryReadyItemViewProj itemViewProj) {
        DeliveryReadyItemViewResDto dto = new DeliveryReadyItemViewResDto();

        dto.setDeliveryReadyItem(DeliveryReadyItemDto.toDto(itemViewProj.getDeliveryReadyItem()))
            .setProdManufacturingCode(itemViewProj.getProdManufacturingCode())
            .setProdManagementName(itemViewProj.getProdManagementName())
            .setOptionDefaultName(itemViewProj.getOptionDefaultName())
            .setOptionManagementName(itemViewProj.getOptionManagementName())
            .setOptionStockUnit(itemViewProj.getOptionStockUnit())
            .setOptionNosUniqueCode(itemViewProj.getOptionNosUniqueCode());


        return dto;
    }

    public static List<DeliveryReadyItemViewResDto> toResDtos(List<DeliveryReadyItemViewProj> itemViewProj) {
        List<DeliveryReadyItemViewResDto> dtos = new ArrayList<>();

        for(DeliveryReadyItemViewProj proj : itemViewProj) {
            dtos.add(toResDto(proj));
        }
        
        return dtos;
    }
}