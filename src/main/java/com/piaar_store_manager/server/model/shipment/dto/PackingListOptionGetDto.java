package com.piaar_store_manager.server.model.shipment.dto;

import lombok.Data;

@Data
public class PackingListOptionGetDto {
    private String prodName;
    private String optionInfo;
    private String fullName;
    private Integer unitSum;
    public PackingListOptionGetDto(String prodName, String optionInfo){
        this.prodName = prodName;
        this.optionInfo = optionInfo;
        this.fullName = prodName + "::" + optionInfo;
        this.unitSum = 0;
    }
}
