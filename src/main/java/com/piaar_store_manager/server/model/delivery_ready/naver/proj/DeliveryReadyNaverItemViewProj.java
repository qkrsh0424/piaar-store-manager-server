package com.piaar_store_manager.server.model.delivery_ready.naver.proj;

import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemViewResDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyNaverItemDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;

public interface DeliveryReadyNaverItemViewProj {
    DeliveryReadyNaverItemEntity getDeliveryReadyItem();
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

    /**
     * <b>Convert Method</b>
     * <p>
     * List::DeliveryReadyNaverItemViewProj:: => List::DeliveryReadyNaverItemViewResDto::
     * 
     * @param itemViewProjs : List::DeliveryReadyNaverItemViewProj::
     * @return List::DeliveryReadyNaverItemViewResDto::
     */
    public static List<DeliveryReadyNaverItemViewResDto> toResDtos(List<DeliveryReadyNaverItemViewProj> itemViewProjs) {
        List<DeliveryReadyNaverItemViewResDto> dtos = itemViewProjs.stream().map(proj -> {
            DeliveryReadyNaverItemViewResDto dto = new DeliveryReadyNaverItemViewResDto();

            dto.setDeliveryReadyItem(DeliveryReadyNaverItemDto.toDto(proj.getDeliveryReadyItem()))
                    .setProdManufacturingCode(proj.getProdManufacturingCode())
                    .setProdManagementName(proj.getProdManagementName())
                    .setOptionDefaultName(proj.getOptionDefaultName())
                    .setOptionManagementName(proj.getOptionManagementName())
                    .setOptionStockUnit(proj.getOptionStockUnit())
                    .setOptionMemo(proj.getOptionMemo())
                    .setOptionNosUniqueCode(proj.getOptionNosUniqueCode());

            return dto;
        }).collect(Collectors.toList());

        return dtos;
    }
}