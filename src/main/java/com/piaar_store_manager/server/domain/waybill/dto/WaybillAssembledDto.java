package com.piaar_store_manager.server.domain.waybill.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class WaybillAssembledDto {
    private String buyer; // 구매자명 9
    private String receiver; // 수취인명 11
    private Date paidTime; // 결제일 15
    private String prodNo; // 상품번호 16
    private String prodName; // 상품명 17
    private String prodCode; // 판매자상품코드 38
    private String receiverContact1; // 수취인 연락처1 41 
    private String receiverContact2; // 수취인 연락처2 42
    private String destination; // 배송지 43
    private String buyerContact; // 구매자 연락처 44
    private String zipcode; // 우편번호 45
    private String deliveryMessage; // 배송메세지 46
    private List<WaybillOptionInfo> optionInfos;
}
