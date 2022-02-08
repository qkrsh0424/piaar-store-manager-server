package com.piaar_store_manager.server.model.delivery_ready.piaar.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public class DeliveryReadyPiaarItemExcelFormDto {
    private String uniqueCode;      // 피아르 고유코드
    private String orderNumber1;        // 주문번호1
    private String orderNumber2;        // 주문번호2
    private String orderNumber3;        // 주문번호3
    private String prodName;        // 상품명 / 필수값
    private String optionName;      // 옵션명 / 필수값
    private Integer unit;       // 수량 / 필수값
    private String receiver;        // 수취인명 / 필수값
    private String receiverContact1;        // 전화번호1 / 필수값
    private String receiverContact2;        // 전화번호2
    private String destination;     // 주소 / 필수값
    private String zipCode;     // 우편번호
    private String transportType;       // 배송방식
    private String deliveryMessage;     // 배송메세지
    private String prodUniqueNumber1;       // 상품고유번호1
    private String prodUniqueNumber2;       // 상품고유번호2
    private String optionUniqueNumber1;     // 옵션고유번호1
    private String optionUniqueNumber2;     // 옵션고유번호2
    private String prodCode;        // 피아르 상품코드
    private String optionCode;      // 피아르 옵션코드
    private String managementMemo1;     // 관리메모1
    private String managementMemo2;     // 관리메모2
    private String managementMemo3;     // 관리메모3
    private String managementMemo4;     // 관리메모4
    private String managementMemo5;     // 관리메모5
    private String managementMemo6;     // 관리메모6
    private String managementMemo7;     // 관리메모7
    private String managementMemo8;     // 관리메모8
    private String managementMemo9;     // 관리메모9
    private String managementMemo10;     // 관리메모10
    private String managementMemo11;     // 관리메모11
    private String managementMemo12;     // 관리메모12
    private String managementMemo13;     // 관리메모13
    private String managementMemo14;     // 관리메모14
    private String managementMemo15;     // 관리메모15
    private String managementMemo16;     // 관리메모16
    private String managementMemo17;     // 관리메모17
    private String managementMemo18;     // 관리메모18
    private String managementMemo19;     // 관리메모19
    private String managementMemo20;     // 관리메모20
    private Boolean released;
    private LocalDateTime releasedAt;

    public static DeliveryReadyPiaarItemExcelFormDto toPiaarFormDto(DeliveryReadyPiaarItemViewDto vewDto) {
        DeliveryReadyPiaarItemExcelFormDto formDto = DeliveryReadyPiaarItemExcelFormDto.builder()
                .uniqueCode(vewDto.getDeliveryReadyItem().getUniqueCode())
                .orderNumber1(vewDto.getDeliveryReadyItem().getOrderNumber1())
                .orderNumber2(vewDto.getDeliveryReadyItem().getOrderNumber2())
                .orderNumber3(vewDto.getDeliveryReadyItem().getOrderNumber3())
                .prodName(vewDto.getDeliveryReadyItem().getProdName())
                .optionName(vewDto.getDeliveryReadyItem().getOptionName())
                .unit(vewDto.getDeliveryReadyItem().getUnit())
                .receiver(vewDto.getDeliveryReadyItem().getReceiver())
                .receiverContact1(vewDto.getDeliveryReadyItem().getReceiverContact1())
                .receiverContact2(vewDto.getDeliveryReadyItem().getReceiverContact2())
                .destination(vewDto.getDeliveryReadyItem().getDestination())
                .zipCode(vewDto.getDeliveryReadyItem().getZipCode())
                .transportType(vewDto.getDeliveryReadyItem().getTransportType())
                .deliveryMessage(vewDto.getDeliveryReadyItem().getDeliveryMessage())
                .prodUniqueNumber1(vewDto.getDeliveryReadyItem().getProdUniqueNumber1())
                .prodUniqueNumber2(vewDto.getDeliveryReadyItem().getProdUniqueNumber2())
                .optionUniqueNumber1(vewDto.getDeliveryReadyItem().getOptionUniqueNumber1())
                .optionUniqueNumber2(vewDto.getDeliveryReadyItem().getOptionUniqueNumber2())
                .prodCode(vewDto.getDeliveryReadyItem().getProdCode())
                .optionCode(vewDto.getDeliveryReadyItem().getOptionCode())
                .managementMemo1(vewDto.getDeliveryReadyItem().getManagementMemo1())
                .managementMemo2(vewDto.getDeliveryReadyItem().getManagementMemo2())
                .managementMemo3(vewDto.getDeliveryReadyItem().getManagementMemo3())
                .managementMemo4(vewDto.getDeliveryReadyItem().getManagementMemo4())
                .managementMemo5(vewDto.getDeliveryReadyItem().getManagementMemo5())
                .managementMemo6(vewDto.getDeliveryReadyItem().getManagementMemo6())
                .managementMemo7(vewDto.getDeliveryReadyItem().getManagementMemo7())
                .managementMemo8(vewDto.getDeliveryReadyItem().getManagementMemo8())
                .managementMemo9(vewDto.getDeliveryReadyItem().getManagementMemo9())
                .managementMemo10(vewDto.getDeliveryReadyItem().getManagementMemo10())
                .managementMemo11(vewDto.getDeliveryReadyItem().getManagementMemo11())
                .managementMemo12(vewDto.getDeliveryReadyItem().getManagementMemo12())
                .managementMemo13(vewDto.getDeliveryReadyItem().getManagementMemo13())
                .managementMemo14(vewDto.getDeliveryReadyItem().getManagementMemo14())
                .managementMemo15(vewDto.getDeliveryReadyItem().getManagementMemo15())
                .managementMemo16(vewDto.getDeliveryReadyItem().getManagementMemo16())
                .managementMemo17(vewDto.getDeliveryReadyItem().getManagementMemo17())
                .managementMemo18(vewDto.getDeliveryReadyItem().getManagementMemo18())
                .managementMemo19(vewDto.getDeliveryReadyItem().getManagementMemo19())
                .managementMemo20(vewDto.getDeliveryReadyItem().getManagementMemo20())
                .released(vewDto.getDeliveryReadyItem().getReleased())
                .releasedAt(vewDto.getDeliveryReadyItem().getReleasedAt())
                .build();

        return formDto;
    }
}
