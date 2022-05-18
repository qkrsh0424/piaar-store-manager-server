package com.piaar_store_manager.server.domain.order_registration.naver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRegistrationHansanExcelFormDto {
    private String receiver; // 수취인명
    private String receiverContact1; // 수취인연락처1
    private String prodName; // 상품명
    private String optionInfo; // 상품상세1
    private Integer unit; // 내품수량1
    private String destination; // 배송지
    private String orderNumber; // 주문번호
    private String optionManagementCode; // 옵션관리코드
    private String prodOrderNumber; // 상품주문번호
    private String platformName;    // 플랫폼명
    private String transportType;   // 배송방식
    private String deliveryService; // 택배사
    private String transportNumber; // 운송장번호
    private String allProdOrderNumber; // 총 상품주문번호
}
