package com.piaar_store_manager.server.model.delivery_ready.coupang.proj;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * <b>Convert Method</b>
     * <p>
     * List::DeliveryReadyCoupangItemViewProj:: => List::DeliveryReadyCoupangItemViewResDto::
     * 
     * @param itemViewProjs : List::DeliveryReadyCoupangItemViewProj::
     * @return List::DeliveryReadyCoupangItemViewResDto::
     */
    public static List<DeliveryReadyCoupangItemViewResDto> toResDtos(List<DeliveryReadyCoupangItemViewProj> itemViewProjs) {
        List<DeliveryReadyCoupangItemViewResDto> dtos = itemViewProjs.stream().map(proj -> {
            DeliveryReadyCoupangItemViewResDto dto = new DeliveryReadyCoupangItemViewResDto();

            dto.setDeliveryReadyItem(DeliveryReadyCoupangItemDto.toDto(proj.getDeliveryReadyItem()))
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