package com.piaar_store_manager.server.domain.delivery_ready.coupang.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.domain.delivery_ready.coupang.entity.DeliveryReadyCoupangItemEntity;
import com.piaar_store_manager.server.domain.delivery_ready.coupang.proj.DeliveryReadyCoupangItemViewProj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
    private LocalDateTime shipmentDueDate;        // 주문시 출고예정일(7)
    private String shipmentCostBundleNumber;        // 묶음배송번호(1)
    private String receiverContact1;        // 수취인전화번호(27)
    private String destination;        // 수취인 주소(29)
    private String buyerContact;        // 구매자전화번호(25)
    private String zipCode;        // 우편번호(28)
    private String deliveryMessage;        // 배송메세지(30)
    private LocalDateTime orderDateTime;        // 주문일(9)
    private String releaseOptionCode;

    // 커스텀 메모
    private String piaarMemo1;
    private String piaarMemo2;
    private String piaarMemo3;
    private String piaarMemo4;
    private String piaarMemo5;
    private String piaarMemo6;
    private String piaarMemo7;
    private String piaarMemo8;
    private String piaarMemo9;
    private String piaarMemo10;

    private Boolean released;   // 출고여부
    private LocalDateTime releasedAt;
    private LocalDateTime createdAt;
    private Boolean releaseCompleted;       // 재고반영 여부
    private Integer deliveryReadyFileCid;

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyCoupangItemEntity => DeliveryReadyCoupangItemDto
     * 
     * @param entity : DeliveryReadyCoupangItemEntity
     * @return  DeliveryReadyCoupangItemDto
     */
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
            .releaseOptionCode(entity.getReleaseOptionCode())
            .piaarMemo1(entity.getPiaarMemo1())
            .piaarMemo2(entity.getPiaarMemo2())
            .piaarMemo3(entity.getPiaarMemo3())
            .piaarMemo4(entity.getPiaarMemo4())
            .piaarMemo5(entity.getPiaarMemo5())
            .piaarMemo6(entity.getPiaarMemo6())
            .piaarMemo7(entity.getPiaarMemo7())
            .piaarMemo8(entity.getPiaarMemo8())
            .piaarMemo9(entity.getPiaarMemo9())
            .piaarMemo10(entity.getPiaarMemo10())
            .released(entity.getReleased())
            .releasedAt(entity.getReleasedAt())
            .createdAt(entity.getCreatedAt())
            .releaseCompleted(entity.getReleaseCompleted())
            .deliveryReadyFileCid(entity.getDeliveryReadyFileCid())
            .build();

        return itemDto;
    }

    /**
     * delivery ready coupang item, 엑셀 다운로드 및 view 결과로 필요한 데이터를 포함하는 객체
     */
    @Getter @Setter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewReqAndRes {
        private DeliveryReadyCoupangItemDto deliveryReadyItem;
        private String prodManufacturingCode;
        private String prodManagementName;
        private String optionDefaultName;
        private String optionManagementName;
        private Integer optionStockUnit;
        private String optionMemo;
        private String sender;
        private String senderContact1;
        private String optionNosUniqueCode;
        private String releaseMemo;
        private String receiveMemo;

        public static ViewReqAndRes toDto(DeliveryReadyCoupangItemViewProj itemViewProj) {
            ViewReqAndRes dto = ViewReqAndRes.builder()
                .deliveryReadyItem(DeliveryReadyCoupangItemDto.toDto(itemViewProj.getDeliveryReadyItem()))
                .prodManufacturingCode(itemViewProj.getProdManufacturingCode())
                .prodManagementName(itemViewProj.getProdManagementName())
                .optionDefaultName(itemViewProj.getOptionDefaultName())
                .optionManagementName(itemViewProj.getOptionManagementName())
                .optionStockUnit(itemViewProj.getOptionStockUnit())
                .optionMemo(itemViewProj.getOptionMemo())
                .optionNosUniqueCode(itemViewProj.getOptionNosUniqueCode())
                .build();
            return dto;
        }
    }
}
