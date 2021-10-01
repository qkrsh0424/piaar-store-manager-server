package com.piaar_store_manager.server.model.delivery_ready.dto;

import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemViewDto;
import com.piaar_store_manager.server.model.delivery_ready.naver.dto.DeliveryReadyItemViewDto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class DeliveryReadyItemHansanExcelFormDto {
    private String receiver; // 수취인명
    private String receiverContact1; // 수취인연락처1
    private String zipCode; // 우편번호
    private String destination; // 배송지
    private String transportNumber; // 운송장번호
    private String prodName; // 상품명
    private String sender; // 보내는사람(지정)
    private String senderContact1; // 전화번호1(지정)
    private String optionInfo; // 상품상세1
    private Integer unit; // 내품수량1
    private String deliveryMessage; // 배송메시지
    private String unitA; // 수량(A타입)
    private String orderNumber; // 주문번호
    private String prodOrderNumber; // 상품주문번호
    private String prodManufacturingCode;   // 상품제조번호
    private String storeProdName; // 스토어 상품명
    private String storeOptionName; // 스토어 옵션명
    private String optionManagementCode; // 옵션관리코드
    private String allProdOrderNumber; // 총 상품주문번호

    private boolean duplication; // 받는사람 + 번호 + 주소 : 중복 여부

    public static DeliveryReadyItemHansanExcelFormDto toFormDto(DeliveryReadyItemViewDto viewDto) {

        DeliveryReadyItemHansanExcelFormDto formDto = DeliveryReadyItemHansanExcelFormDto.builder()
                .receiver(viewDto.getDeliveryReadyItem().getReceiver())
                .receiverContact1(viewDto.getDeliveryReadyItem().getReceiverContact1())
                .zipCode(viewDto.getDeliveryReadyItem().getZipCode())
                .destination(viewDto.getDeliveryReadyItem().getDestination())
                .transportNumber("")
                .prodName(viewDto.getDeliveryReadyItem().getProdName())
                .sender(viewDto.getSender())
                .senderContact1(viewDto.getSenderContact1())
                .optionInfo(viewDto.getDeliveryReadyItem().getOptionInfo())
                .unit(viewDto.getDeliveryReadyItem().getUnit())
                .deliveryMessage(viewDto.getDeliveryReadyItem().getDeliveryMessage())
                .unitA("")
                .orderNumber(viewDto.getDeliveryReadyItem().getOrderNumber())
                .prodOrderNumber(viewDto.getDeliveryReadyItem().getProdOrderNumber())
                .prodManufacturingCode(viewDto.getProdManufacturingCode() != null ? viewDto.getProdManufacturingCode() : "")
                .storeProdName(viewDto.getProdManagementName())
                .storeOptionName(viewDto.getOptionManagementName())
                .optionManagementCode(viewDto.getDeliveryReadyItem().getOptionManagementCode())
                .allProdOrderNumber(viewDto.getDeliveryReadyItem().getProdOrderNumber())
                .duplication(false)
                .build();

        return formDto;

    }

    public static DeliveryReadyItemHansanExcelFormDto toFormDto(DeliveryReadyCoupangItemViewDto viewDto) {

        DeliveryReadyItemHansanExcelFormDto formDto = DeliveryReadyItemHansanExcelFormDto.builder()
                .receiver(viewDto.getDeliveryReadyItem().getReceiver())
                .receiverContact1(viewDto.getDeliveryReadyItem().getReceiverContact1())
                .zipCode(viewDto.getDeliveryReadyItem().getZipCode())
                .destination(viewDto.getDeliveryReadyItem().getDestination())
                .transportNumber("")
                .prodName(viewDto.getDeliveryReadyItem().getProdName())
                .sender(viewDto.getSender())
                .senderContact1(viewDto.getSenderContact1())
                .optionInfo(viewDto.getDeliveryReadyItem().getOptionInfo())
                .unit(viewDto.getDeliveryReadyItem().getUnit())
                .deliveryMessage(viewDto.getDeliveryReadyItem().getDeliveryMessage())
                .unitA("")
                .orderNumber(viewDto.getDeliveryReadyItem().getOrderNumber())
                .prodOrderNumber(viewDto.getDeliveryReadyItem().getProdOrderNumber())
                .prodManufacturingCode(viewDto.getProdManufacturingCode() != null ? viewDto.getProdManufacturingCode() : "")
                .storeProdName(viewDto.getProdManagementName())
                .storeOptionName(viewDto.getOptionManagementName())
                .optionManagementCode(viewDto.getDeliveryReadyItem().getOptionManagementCode())
                .allProdOrderNumber(viewDto.getDeliveryReadyItem().getProdOrderNumber())
                .duplication(false)
                .build();

        return formDto;

    }
}
