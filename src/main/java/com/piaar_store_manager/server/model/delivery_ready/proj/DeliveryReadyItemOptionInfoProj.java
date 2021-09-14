package com.piaar_store_manager.server.model.delivery_ready.proj;

import java.util.ArrayList;
import java.util.List;

import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemOptionInfoResDto;

public interface DeliveryReadyItemOptionInfoProj {
    String getOptionCode();
    String getOptionDefaultName();
    String getOptionManagementName();
    String getProdDefaultName();

    public static DeliveryReadyItemOptionInfoResDto toResDto(DeliveryReadyItemOptionInfoProj proj) {
        DeliveryReadyItemOptionInfoResDto dto = new DeliveryReadyItemOptionInfoResDto();

        dto.setOptionCode(proj.getOptionCode()).setOptionDefaultName(proj.getOptionDefaultName())
                .setOptionManagementName(proj.getOptionManagementName()).setProdDefaultName(proj.getProdDefaultName());

        return dto;
    }
    
    public static List<DeliveryReadyItemOptionInfoResDto> toResDtos(List<DeliveryReadyItemOptionInfoProj> projs) {
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDto = new ArrayList<>();

        for (DeliveryReadyItemOptionInfoProj proj : projs) {
            optionInfoDto.add(toResDto(proj));
        }
        return optionInfoDto;
    }
}
