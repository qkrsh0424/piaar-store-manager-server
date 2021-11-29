package com.piaar_store_manager.server.model.delivery_ready.proj;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.model.delivery_ready.dto.DeliveryReadyItemOptionInfoResDto;

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
    
    /**
     * <b>Convert Method</b>
     * <p>
     * List::DeliveryReadyItemOptionInfoProj:: => List::DeliveryReadyItemOptionInfoResDto::
     * 
     * @param projs : List::DeliveryReadyItemOptionInfoProj::
     * @return List::DeliveryReadyItemOptionInfoResDto::
     */
    public static List<DeliveryReadyItemOptionInfoResDto> toResDtos(List<DeliveryReadyItemOptionInfoProj> projs) {
        List<DeliveryReadyItemOptionInfoResDto> optionInfoDtos = projs.stream().map(proj -> {
            DeliveryReadyItemOptionInfoResDto dto = new DeliveryReadyItemOptionInfoResDto();

            dto.setOptionCode(proj.getOptionCode())
                    .setOptionDefaultName(proj.getOptionDefaultName())
                    .setOptionManagementName(proj.getOptionManagementName())
                    .setProdDefaultName(proj.getProdDefaultName());

            return dto;
        }).collect(Collectors.toList());
        return optionInfoDtos;
    }
}
