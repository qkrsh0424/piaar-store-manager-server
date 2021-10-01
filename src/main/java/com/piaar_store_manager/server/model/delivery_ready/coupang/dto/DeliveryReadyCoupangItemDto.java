package com.piaar_store_manager.server.model.delivery_ready.coupang.dto;

import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.model.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DeliveryReadyCoupangItemDto {
    private Integer cid;
    private UUID id;
    private String prodOrderNumber;     // 상품주문번호 : 주문번호 | 노출상품ID | 옵션ID
    private String orderNumber;     // 주문번호(2)
    private String buyer;       // 구매자(24)
    private String receiver;        // 수취인이름(26)
    private String prodNumber;        // 노출상품ID(13)
    private String prodName;        // 등록상품명(10)
    private String prodExposureName;        // 노출상품명(12)
    private String optionInfo;        // 등록옵션명(11)
    private String optionManagementCode;        // 업체상품코드(16)
    private String coupangOptionId;        // 옵션ID(14)
    private Integer unit;        // 수량(22)
    private Date shipmentDueDate;        // 주문시 출고예정일(7)
    private String shipmentCostBundleNumber;        // 묶음배송번호(1)
    private String receiverContact1;        // 수취인전화번호(27)
    private String destination;        // 수취인 주소(29)
    private String buyerContact;        // 구매자전화번호(25)
    private String zipCode;        // 우편번호(28)
    private String deliveryMessage;        // 배송메세지(30)
    private Date orderDateTime;        // 주문일(9)
    private Boolean released;   // 출고여부
    private Date releasedAt;
    private Date createdAt;
    private Integer deliveryReadyFileCid;

    public static DeliveryReadyCoupangItemDto toDto(DeliveryReadyCoupangItemEntity entity) {
        DeliveryReadyCoupangItemDto itemDto = DeliveryReadyCoupangItemDto.builder()
            .cid(entity.getCid())
            .id(entity.getId())
            .prodOrderNumber(entity.getProdOrderNumber())
            .orderNumber(entity.getOrderNumber())
            .buyer(entity.getBuyer())
            .receiver(entity.getReceiver())
            .prodNumber(entity.getProdNumber())
            .prodName(entity.getProdName())
            .prodExposureName(entity.getProdExposureName())
            .optionInfo(entity.getOptionInfo())
            .optionManagementCode(entity.getOptionManagementCode())
            .coupangOptionId(entity.getCoupangOptionId())
            .unit(entity.getUnit())
            .shipmentDueDate(entity.getShipmentDueDate())
            .shipmentCostBundleNumber(entity.getShipmentCostBundleNumber())
            .receiverContact1(entity.getReceiverContact1())
            .destination(entity.getDestination())
            .buyerContact(entity.getBuyerContact())
            .zipCode(entity.getZipCode())
            .deliveryMessage(entity.getDeliveryMessage())
            .orderDateTime(entity.getOrderDateTime())
            .released(entity.getReleased())
            .releasedAt(entity.getReleasedAt())
            .createdAt(entity.getCreatedAt())
            .deliveryReadyFileCid(entity.getDeliveryReadyFileCid())
            .build();

        return itemDto;
    }
}
