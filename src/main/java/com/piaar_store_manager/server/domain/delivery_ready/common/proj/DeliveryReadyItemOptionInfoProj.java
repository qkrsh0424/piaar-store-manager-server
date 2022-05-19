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
        DeliveryReadyItemOptionInfoResDto dto = new DeliveryReadyItemOptionInfoResDto();

        dto.setOptionCode(proj.getOptionCode())
            .setOptionDefaultName(proj.getOptionDefaultName())
            .setOptionManagementName(proj.getOptionManagementName())
            .setProdDefaultName(proj.getProdDefaultName());

        return dto;
    }
}
