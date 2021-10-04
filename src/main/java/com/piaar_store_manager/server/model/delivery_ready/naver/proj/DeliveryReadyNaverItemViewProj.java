package com.piaar_store_manager.server.model.delivery_ready.naver.proj;

import java.util.ArrayList;
import java.util.List;

import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;

public interface DeliveryReadyNaverItemViewProj {
    DeliveryReadyNaverItemEntity getDeliveryReadyItem();
    String getProdManufacturingCode();
    String getOptionDefaultName();
    String getOptionManagementName();
    Integer getOptionStockUnit();
    String getProdManagementName();
    String getOptionNosUniqueCode();

    public static DeliveryReadyNaverItemViewResDto toResDto(DeliveryReadyNaverItemViewProj itemViewProj) {
        DeliveryReadyNaverItemViewResDto dto = new DeliveryReadyNaverItemViewResDto();

        dto.setDeliveryReadyItem(DeliveryReadyNaverItemDto.toDto(itemViewProj.getDeliveryReadyItem()))
            .setProdManufacturingCode(itemViewProj.getProdManufacturingCode())
            .setProdManagementName(itemViewProj.getProdManagementName())
            .setOptionDefaultName(itemViewProj.getOptionDefaultName())
            .setOptionManagementName(itemViewProj.getOptionManagementName())
            .setOptionStockUnit(itemViewProj.getOptionStockUnit())
            .setOptionNosUniqueCode(itemViewProj.getOptionNosUniqueCode());

        return dto;
    }

    public static List<DeliveryReadyNaverItemViewResDto> toResDtos(List<DeliveryReadyNaverItemViewProj> itemViewProj) {
        List<DeliveryReadyNaverItemViewResDto> dtos = new ArrayList<>();

        for(DeliveryReadyNaverItemViewProj proj : itemViewProj) {
            dtos.add(toResDto(proj));
        }
        
        return dtos;
    }
}