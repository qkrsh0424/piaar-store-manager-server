package com.piaar_store_manager.server.model.delivery_ready.dto;

import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.entity.DeliveryReadyItemEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
public class DeliveryReadyItemDto {
    private Integer cid;
    private UUID id;
    private String prodOrderNumber;     // 상품주문번호(0)
    private String orderNumber;      // 주문번호(1)
    private String salesChannel;        // 판매채널(7)
    private String buyer;       // 구매자명(8)
    private String buyerId;     // 구매자ID(9)
    private String receiver;        // 수취인명(10)
    private Date paymentDate;     // 결제일(14)
    private String prodNumber;        // 상품번호(15)
    private String prodName;        // 상품명(16)
    private String optionInfo;        // 옵션정보(18)
    private String optionManagementCode;        // 옵션관리코드(19)
    private Integer unit;        // 수량(20)
    private Date orderConfirmationDate;        // 발주확인일(27)
    private Date shipmentDueDate;        // 발송기한(28)
    private String shipmentCostBundleNumber;        // 배송비 묶음번호(32)
    private String sellerProdCode;        // 판매자 상품코드(37)
    private String sellerInnerCode1;        // 판매자 내부코드1(38)
    private String sellerInnerCode2;        // 판매자 내부코드2(39)
    private String receiverContact1;        // 수취인연락처1(40)
    private String receiverContact2;        // 수취인연락처2(41)
    private String destination;        // 배송지(42)
    private String buyerContact;        // 구매자연락처(43)
    private String zipCode;        // 우편번호(44)
    private String deliveryMessage;        // 배송메세지(45)
    private String releaseArea;        // 출고지(46)
    private Date orderDateTime;        // 주문일시(56)
    private Boolean released;   // 출고여부
    private Date releasedAt;
    private Date createdAt;
    private Integer deliveryReadyFileCid;

    public static DeliveryReadyItemDto toDto(DeliveryReadyItemEntity entity) {
        DeliveryReadyItemDto itemDto = DeliveryReadyItemDto.builder()
            .cid(entity.getCid())
            .id(entity.getId())
            .prodOrderNumber(entity.getProdOrderNumber())
            .orderNumber(entity.getOrderNumber())
            .salesChannel(entity.getSalesChannel())
            .buyer(entity.getBuyer())
            .buyerId(entity.getBuyerId())
            .receiver(entity.getReceiver())
            .paymentDate(entity.getPaymentDate())
            .prodNumber(entity.getProdNumber())
            .prodName(entity.getProdName())
            .optionInfo(entity.getOptionInfo())
            .optionManagementCode(entity.getOptionManagementCode())
            .unit(entity.getUnit())
            .orderConfirmationDate(entity.getOrderConfirmationDate())
            .shipmentDueDate(entity.getShipmentDueDate())
            .shipmentCostBundleNumber(entity.getShipmentCostBundleNumber())
            .sellerProdCode(entity.getSellerProdCode())
            .sellerInnerCode1(entity.getSellerInnerCode1())
            .sellerInnerCode2(entity.getSellerInnerCode2())
            .receiverContact1(entity.getReceiverContact1())
            .receiverContact2(entity.getReceiverContact2())
            .destination(entity.getDestination())
            .buyerContact(entity.getBuyerContact())
            .zipCode(entity.getZipCode())
            .deliveryMessage(entity.getDeliveryMessage())
            .releaseArea(entity.getReleaseArea())
            .orderDateTime(entity.getOrderDateTime())
            .released(entity.getReleased())
            .releasedAt(entity.getReleasedAt())
            .createdAt(entity.getCreatedAt())
            .deliveryReadyFileCid(entity.getDeliveryReadyFileCid())
            .build();

        return itemDto;
    }
}
