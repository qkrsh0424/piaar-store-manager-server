package com.piaar_store_manager.server.model.sales_rate.dto;

import lombok.Data;

@Data
public class SalesRateOptionGetDto implements Comparable<SalesRateOptionGetDto>{
    private String prodName;
    private String optionInfo;
    private String fullName;
    private Integer unitSum;
    private Integer salesCount;

    public SalesRateOptionGetDto(String prodName, String optionInfo){
        this.prodName = prodName;
        this.optionInfo = optionInfo;
        this.fullName = prodName + "::" +optionInfo;
        this.unitSum = 0;
        this.salesCount = 0;
    }

    @Override
    public int compareTo(SalesRateOptionGetDto data){
        String str = prodName + optionInfo;
        return str.compareTo(data.getProdName() + data.getOptionInfo());
    }

}
