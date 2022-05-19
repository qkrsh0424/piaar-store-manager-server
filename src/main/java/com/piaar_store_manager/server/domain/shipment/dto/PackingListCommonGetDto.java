package com.piaar_store_manager.server.domain.shipment.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PackingListCommonGetDto {
    private String prodName;
    private Integer unitSum;
    private List<PackingListOptionGetDto> optionInfos;

    public PackingListCommonGetDto(String prodName){
        this.prodName = prodName;
        this.unitSum = 0;
        this.optionInfos = new ArrayList<>();
    }
}
