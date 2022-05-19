package com.piaar_store_manager.server.model.waybill;

import java.util.Date;

import lombok.Data;

@Data
public class WaybillFormReadDto implements Comparable<WaybillFormReadDto>{
    private String buyer; // 구매자명 9
    private String receiver; // 수취인명 11
    private Date paidTime; // 결제일 15
    private String prodNo; // 상품번호 16
    private String prodName; // 상품명 17
    private String optionInfo; // 옵션정보 19
    private int unit; // 수량 21
    private String prodCode; // 판매자상품코드 38
    private String receiverContact1; // 수취인 연락처1 41 
    private String receiverContact2; // 수취인 연락처2 42
    private String destination; // 배송지 43
    private String buyerContact; // 구매자 연락처 44
    private String zipcode; // 우편번호 45
    private String deliveryMessage; // 배송메세지 46
    
    @Override
    public int compareTo(WaybillFormReadDto data){
        return receiver.compareTo(data.getReceiver());
    }
}
