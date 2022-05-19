package com.piaar_store_manager.server.model.delivery_ready.naver.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryReadyNaverItemExcelFormDto {
    private String prodOrderNumber;     // 상품주문번호
    private String orderNumber;      // 주문번호
    private String buyer;           // 구매자명
    private String buyerId;     // 구매자ID
    private String receiver;        // 수취인명
    private String prodManagementName;      // 피아르 상품관리명
    private String prodManufacturingCode;   // 피아르 상품제조번호
    private String optionManagementCode;    // 피아르 옵션관리코드
    private String optionDefaultName;       // 피아르 옵션관리명1
    private String optionManagementName;    // 피아르 옵션관리명2
    private String unit;        // 수량
    private String optionStockUnit;        // 재고수량
    private Date paymentDate;     // 결제일
    private Date orderConfirmationDate;        // 발주확인일
    private Date shipmentDueDate;        // 발송기한
    private String shipmentCostBundleNumber;        // 배송비 묶음번호
    private String prodNumber;        // 상품번호
    private String sellerProdCode;        // 판매자 상품코드
    private String prodName;        // 상품명
    private String optionInfo;        // 옵션정보
    private String optionNosUniqueCode;     // 노스노스 고유번호
    private String receiverContact1;        // 수취인연락처1
    private String receiverContact2;        // 수취인연락처2
    private String zipCode;        // 우편번호
    private String destination;        // 배송지
    private String buyerContact;        // 구매자연락처
    private String deliveryMessage;        // 배송메세지
    private Date orderDateTime;        // 주문일시
    private Boolean released;   // 출고여부
    private Date releasedAt;    // 출고일시

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyNaverItemViewDto => DeliveryReadyNaverItemExcelFormDto
     * 
     * @param viewDto : DeliveryReadyNaverItemViewDto
     * @return DeliveryReadyNaverItemExcelFormDto
     */
    public static DeliveryReadyNaverItemExcelFormDto toNaverFormDto(DeliveryReadyNaverItemViewDto viewDto) {
        DeliveryReadyNaverItemExcelFormDto formDto = DeliveryReadyNaverItemExcelFormDto.builder()
                .prodOrderNumber(viewDto.getDeliveryReadyItem().getProdOrderNumber())
                .orderNumber(viewDto.getDeliveryReadyItem().getOrderNumber())
                .buyer(viewDto.getDeliveryReadyItem().getBuyer())
                .buyerId(viewDto.getDeliveryReadyItem().getBuyerId())
                .receiver(viewDto.getDeliveryReadyItem().getReceiver())
                .prodManagementName(viewDto.getProdManagementName())
                .prodManufacturingCode(viewDto.getProdManufacturingCode())
                .optionManagementCode(viewDto.getDeliveryReadyItem().getOptionManagementCode())
                .optionDefaultName(viewDto.getOptionDefaultName())
                .optionManagementName(viewDto.getOptionManagementName())
                .unit(viewDto.getDeliveryReadyItem().getUnit() != null ? viewDto.getDeliveryReadyItem().getUnit().toString() : "")
                .optionStockUnit(viewDto.getOptionStockUnit() != null ? viewDto.getOptionStockUnit().toString() : "")
                .paymentDate(viewDto.getDeliveryReadyItem().getPaymentDate())
                .orderConfirmationDate(viewDto.getDeliveryReadyItem().getOrderConfirmationDate())
                .shipmentDueDate(viewDto.getDeliveryReadyItem().getShipmentDueDate())
                .shipmentCostBundleNumber(viewDto.getDeliveryReadyItem().getShipmentCostBundleNumber())
                .prodNumber(viewDto.getDeliveryReadyItem().getProdNumber())
                .sellerProdCode(viewDto.getDeliveryReadyItem().getSellerProdCode())
                .prodName(viewDto.getDeliveryReadyItem().getProdName())
                .optionInfo(viewDto.getDeliveryReadyItem().getOptionInfo())
                .optionNosUniqueCode(viewDto.getOptionNosUniqueCode())
                .receiverContact1(viewDto.getDeliveryReadyItem().getReceiverContact1())
                .receiverContact2(viewDto.getDeliveryReadyItem().getReceiverContact2())
                .zipCode(viewDto.getDeliveryReadyItem().getZipCode())
                .destination(viewDto.getDeliveryReadyItem().getDestination())
                .buyerContact(viewDto.getDeliveryReadyItem().getBuyerContact())
                .deliveryMessage(viewDto.getDeliveryReadyItem().getDeliveryMessage())
                .orderDateTime(viewDto.getDeliveryReadyItem().getOrderDateTime())
                .released(viewDto.getDeliveryReadyItem().getReleased())
                .releasedAt(viewDto.getDeliveryReadyItem().getReleasedAt())
                .build();

        return formDto;
    }
}
