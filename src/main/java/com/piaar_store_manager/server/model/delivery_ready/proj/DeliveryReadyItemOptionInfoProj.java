package com.piaar_store_manager.server.model.delivery_ready.proj;

import java.util.ArrayList;
import java.util.List;

import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemOptionInfoResDto;

public interface DeliveryReadyItemOptionInfoProj {
    String getOptionCode();
    String getOptionDefaultName();
    String getOptionManagementName();
    String getProdDefaultName();

    public static List<DeliveryReadyItemOptionInfoResDto> toResDto(List<DeliveryReadyItemOptionInfoProj> projs) {
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDto = new ArrayList<>();

        for (DeliveryReadyItemOptionInfoProj proj : projs) {
            DeliveryReadyItemOptionInfoResDto dto = new DeliveryReadyItemOptionInfoResDto();

            dto.setOptionCode(proj.getOptionCode()).setOptionDefaultName(proj.getOptionDefaultName())
                    .setOptionManagementName(proj.getOptionManagementName())
                    .setProdDefaultName(proj.getProdDefaultName());

            optionInfoDto.add(dto);
        }

        return optionInfoDto;
    }
}
