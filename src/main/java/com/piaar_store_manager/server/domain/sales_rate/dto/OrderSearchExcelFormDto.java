package com.piaar_store_manager.server.domain.sales_rate.dto;

import java.util.Date;

import lombok.Data;

@Data
public class OrderSearchExcelFormDto implements Comparable<OrderSearchExcelFormDto>{
    private String prodOrderNo;
    private String orderNo;
    private Date orderTime;
    private String orderStatus;
    private String claimStatus;
    private String prodNo;
    private String prodName;
    private String optionInfo;
    private Integer unit;
    private String buyerName;
    private String buyerId;
    private String receiverName;

    @Override
    public int compareTo(OrderSearchExcelFormDto data){
        String str = prodName + optionInfo;
        return str.compareTo(data.getProdName() + data.getOptionInfo());
    }
}
