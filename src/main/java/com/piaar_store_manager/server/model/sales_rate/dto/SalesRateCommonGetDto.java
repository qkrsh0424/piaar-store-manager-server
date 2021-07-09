package com.piaar_store_manager.server.model.sales_rate.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SalesRateCommonGetDto implements Comparable<SalesRateCommonGetDto>{
    private String prodName;
    private String prodNo;
    private Integer unitSum;
    private List<SalesRateOptionGetDto> optionInfos;
    private Integer salesCount;

    public SalesRateCommonGetDto(String prodName, String prodNo){
        this.prodName = prodName;
        this.prodNo = prodNo;
        this.unitSum = 0;
        this.optionInfos = new ArrayList<>();
        this.salesCount = 0;
    }

    @Override
    public int compareTo(SalesRateCommonGetDto data){
        String str = prodName;
        return str.compareTo(data.getProdName());
    }

}
