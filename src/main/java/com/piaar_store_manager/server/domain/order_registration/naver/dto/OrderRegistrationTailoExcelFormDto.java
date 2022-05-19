package com.piaar_store_manager.server.domain.order_registration.naver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRegistrationTailoExcelFormDto {
    private String receiver; // 받는분 이름
    private String receiverContact1; // 전화번호1
    private String prodUniqueCode; // 상품고유코드
    private String salesProdName; // 판매상품명
    private Integer unit; // 수량
    private String destination1; // 주소1
    private String orderNumber; // 주문번호
    private String prodMemo1;   // 상품별메모1(상품주문번호)
    private String prodMemo3;   // 상품별메모3(옵션관리코드)
    private String managementMemo3; // 관리메모3
    private String transportType; // 배송방식
    private String deliveryService; // 택배사
    private String transportNumber; // 송장번호
    
    // private String managementMemo1; // 관리메모1 -> 삭제
    // private String managementMemo2;   // 관리메모2 -> 삭제
}
