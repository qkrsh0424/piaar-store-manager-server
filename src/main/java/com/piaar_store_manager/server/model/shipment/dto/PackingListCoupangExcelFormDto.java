package com.piaar_store_manager.server.model.shipment.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PackingListCoupangExcelFormDto implements Comparable<PackingListCoupangExcelFormDto>{
    private String orderNo; // 주문번호 3
    private String buyer; // 구매자명 24
    private String receiver; // 수취인명 27
    private String paidTime; // 결제일 10
    private String prodNo; // 상품번호 13 (노출상품아이디)
    private String prodName; // 상품명 11
    private String optionInfo; // 옵션정보 12
    private int unit; // 수량 23
    private String prodCode; // 판매자상품코드 16
    private String receiverContact1; // 수취인 연락처1 28
    private String destination; // 배송지 30
    private String buyerContact; // 구매자 연락처 26
    private String zipcode; // 우편번호 29
    private String deliveryMessage; // 배송메세지 31
    
    @Override
    public int compareTo(PackingListCoupangExcelFormDto data){
        return receiver.compareTo(data.getReceiver());
    }
}
