package com.piaar_store_manager.server.domain.delivery_ready.common.proj;

import com.piaar_store_manager.server.domain.delivery_ready.common.dto.DeliveryReadyItemOptionInfoResDto;

public interface DeliveryReadyItemOptionInfoProj {
    String getOptionCode();
    String getOptionDefaultName();
    String getOptionManagementName();
    String getProdDefaultName();

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyItemOptionInfoProj => DeliveryReadyItemOptionInfoResDto
     * 
     * @param proj : DeliveryReadyItemOptionInfoProj
     * @return DeliveryReadyItemOptionInfoResDto
     */
    public static DeliveryReadyItemOptionInfoResDto toResDto(DeliveryReadyItemOptionInfoProj proj) {
        DeliveryReadyItemOptionInfoResDto dto = DeliveryReadyItemOptionInfoResDto.builder()
            .optionCode(proj.getOptionCode())
            .optionDefaultName(proj.getOptionDefaultName())
            .optionManagementName(proj.getOptionManagementName())
            .prodDefaultName(proj.getProdDefaultName())
            .build();

        return dto;
    }
}
