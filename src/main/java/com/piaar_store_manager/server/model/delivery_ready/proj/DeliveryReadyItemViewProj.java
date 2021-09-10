package com.piaar_store_manager.server.model.delivery_ready.proj;

import java.util.ArrayList;
import java.util.List;

import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemDto;
import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyItemEntity;

public interface DeliveryReadyItemViewProj {
    DeliveryReadyItemEntity getDeliveryReadyItem();
    String getOptionDefaultName();
    String getOptionManagementName();
    Integer getOptionStockUnit();
    String getProdManagementName();

    public static List<DeliveryReadyItemViewResDto> toResDto(List<DeliveryReadyItemViewProj> itemViewProj) {
        List<DeliveryReadyItemViewResDto> itemViewResDto = new ArrayList<>();

        for(DeliveryReadyItemViewProj proj : itemViewProj){
            DeliveryReadyItemViewResDto dto = new DeliveryReadyItemViewResDto();

            dto.setDeliveryReadyItem(DeliveryReadyItemDto.toDto(proj.getDeliveryReadyItem()))
                .setOptionDefaultName(proj.getOptionDefaultName())
                .setOptionManagementName(proj.getOptionManagementName())
                .setOptionStockUnit(proj.getOptionStockUnit())
                .setProdManagementName(proj.getProdManagementName());

            itemViewResDto.add(dto);
        }
        return itemViewResDto;
    }
}