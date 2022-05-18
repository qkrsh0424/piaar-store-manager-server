package com.piaar_store_manager.server.domain.order_confirm.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderConfirmGetDto implements Comparable<OrderConfirmGetDto>{
    private String Uuid;
    private String prodName;
    private String optionInfo;
    private String prodFullName;
    private int unit;
    private List<OrdererDto> ordererList;
    @Override
    public int compareTo(OrderConfirmGetDto o) {
        // TODO Auto-generated method stub
        return this.prodFullName.compareTo(o.prodFullName);
    }
}
