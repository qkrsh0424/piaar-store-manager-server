package com.piaar_store_manager.server.model.sales_rate.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SalesRateCommonGetDto implements Comparable<SalesRateCommonGetDto>{
    private String prodName;
    private Integer unitSum;
    private List<SalesRateOptionGetDto> optionInfos;

    public SalesRateCommonGetDto(String prodName){
        this.prodName = prodName;
        this.unitSum = 0;
        this.optionInfos = new ArrayList<>();

    }

    @Override
    public int compareTo(SalesRateCommonGetDto data){
        String str = prodName;
        return str.compareTo(data.getProdName());
    }

}
