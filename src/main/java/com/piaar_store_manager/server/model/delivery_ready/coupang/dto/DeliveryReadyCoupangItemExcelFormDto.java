package com.piaar_store_manager.server.model.delivery_ready.coupang.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryReadyCoupangItemExcelFormDto {
    private String shipmentCostBundleNumber;    //  묶음배송번호
    private String orderNumber;      // 주문번호
    private String buyer;           // 구매자
    private String receiver;        // 수취인이름
    private String prodManagementName;      // 피아르 상품관리명
    private String prodManufacturingCode;   // 피아르 상품제조번호
    private String optionManagementCode;    // 피아르 옵션관리코드
    private String optionDefaultName;       // 피아르 옵션관리명1
    private String optionManagementName;    // 피아르 옵션관리명2
    private String unit;        // 수량
    private String optionStockUnit;        // 재고수량
    private String prodNumber;          // 노출상품ID
    private String prodName;            // 등록상품명
    private String prodExposureName;        // 노출상품명(옵션명)
    private String coupangOptionId;         // 옵션ID
    private String optionInfo;              // 등록옵션명
    private String optionNosUniqueCode;     // 노스노스 고유번호
    private String receiverContact1;        // 수취인 전화번호
    private String zipCode;        // 우편번호
    private String destination;        // 수취인 주소
    private String buyerContact;        // 구매자전화번호
    private String deliveryMessage;        // 배송메세지
    private Date shipmentDueDate;         // 주문시 출고예정일
    private Date orderDateTime;        // 주문일
    private Boolean released;   // 출고여부
    private Date releasedAt;    // 출고일시

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyCoupangItemViewDto => DeliveryReadyCoupangItemExcelFormDto
     * 
     * @param viewDto : DeliveryReadyCoupangItemViewDto
     * @return DeliveryReadyCoupangItemExcelFormDto
     */
    public static DeliveryReadyCoupangItemExcelFormDto toCoupangFormDto(DeliveryReadyCoupangItemViewDto viewDto) {

        DeliveryReadyCoupangItemExcelFormDto formDto = DeliveryReadyCoupangItemExcelFormDto.builder()
                .shipmentCostBundleNumber(viewDto.getDeliveryReadyItem().getShipmentCostBundleNumber())
                .orderNumber(viewDto.getDeliveryReadyItem().getOrderNumber())
                .buyer(viewDto.getDeliveryReadyItem().getBuyer())
                .receiver(viewDto.getDeliveryReadyItem().getReceiver())
                .prodManagementName(viewDto.getProdManagementName())
                .prodManufacturingCode(viewDto.getProdManufacturingCode())
                .optionManagementCode(viewDto.getDeliveryReadyItem().getOptionManagementCode())
                .optionDefaultName(viewDto.getOptionDefaultName())
                .optionManagementName(viewDto.getOptionManagementName())
                .unit(viewDto.getDeliveryReadyItem().getUnit() != null ? viewDto.getDeliveryReadyItem().getUnit().toString() : "")
                .optionStockUnit(viewDto.getOptionStockUnit() != null ? viewDto.getOptionStockUnit().toString() : "")
                .prodNumber(viewDto.getDeliveryReadyItem().getProdNumber())
                .prodName(viewDto.getDeliveryReadyItem().getProdName())
                .prodExposureName(viewDto.getDeliveryReadyItem().getProdExposureName())
                .coupangOptionId(viewDto.getDeliveryReadyItem().getCoupangOptionId())
                .optionInfo(viewDto.getDeliveryReadyItem().getOptionInfo())
                .optionNosUniqueCode(viewDto.getOptionNosUniqueCode())
                .receiverContact1(viewDto.getDeliveryReadyItem().getReceiverContact1())
                .zipCode(viewDto.getDeliveryReadyItem().getZipCode())
                .destination(viewDto.getDeliveryReadyItem().getDestination())
                .buyerContact(viewDto.getDeliveryReadyItem().getBuyerContact())
                .deliveryMessage(viewDto.getDeliveryReadyItem().getDeliveryMessage())
                .shipmentDueDate(viewDto.getDeliveryReadyItem().getShipmentDueDate())
                .orderDateTime(viewDto.getDeliveryReadyItem().getOrderDateTime())
                .released(viewDto.getDeliveryReadyItem().getReleased())
                .releasedAt(viewDto.getDeliveryReadyItem().getReleasedAt())
                .build();

        return formDto;
    }
}
