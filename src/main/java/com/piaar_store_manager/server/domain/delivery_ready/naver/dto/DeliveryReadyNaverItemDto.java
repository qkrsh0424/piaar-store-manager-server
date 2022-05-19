package com.piaar_store_manager.server.domain.delivery_ready.naver.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import com.piaar_store_manager.server.domain.delivery_ready.naver.entity.DeliveryReadyNaverItemEntity;
import com.piaar_store_manager.server.domain.delivery_ready.naver.proj.DeliveryReadyNaverItemViewProj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReadyNaverItemDto {
    private Integer cid;
    private UUID id;
    private String prodOrderNumber;     // 상품주문번호(0)
    private String orderNumber;      // 주문번호(1)
    private String salesChannel;        // 판매채널(7)
    private String buyer;       // 구매자명(8)
    private String buyerId;     // 구매자ID(9)
    private String receiver;        // 수취인명(10)
    private LocalDateTime paymentDate;     // 결제일(14)
    private String prodNumber;        // 상품번호(15)
    private String prodName;        // 상품명(16)
    private String optionInfo;        // 옵션정보(18)
    private String optionManagementCode;        // 옵션관리코드(19)
    private Integer unit;        // 수량(20)
    private LocalDateTime orderConfirmationDate;        // 발주확인일(27)
    private LocalDateTime shipmentDueDate;        // 발송기한(28)
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
    private LocalDateTime orderDateTime;        // 주문일시(56)
    private String releaseOptionCode;       // 출고 옵션코드

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
    private LocalDateTime releasedAt;    // 출고일시
    private LocalDateTime createdAt;     // 업로드 일시
    private Boolean releaseCompleted;       // 재고반영 여부
    private Integer deliveryReadyFileCid;       // 파일 cid

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyNaverItemEntity => DeliveryReadyNaverItemDto
     * 
     * @param entity : DeliveryReadyNaverItemEntity
     * @return DeliveryReadyNaverItemDto
     */
    public static DeliveryReadyNaverItemDto toDto(DeliveryReadyNaverItemEntity entity) {
        DeliveryReadyNaverItemDto itemDto = DeliveryReadyNaverItemDto.builder()
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
     * delivery ready naver item, 엑셀 다운로드 및 view 결과로 필요한 데이터를 포함하는 객체
     */
    @Getter @Setter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewReqAndRes {
        DeliveryReadyNaverItemDto deliveryReadyItem;
        private String prodManufacturingCode;
        private String prodManagementName;
        private String optionDefaultName;
        private String optionManagementName;
        private Integer optionStockUnit;
        private String optionMemo;  // viewResDto에만 있는 항목
        private String sender;
        private String senderContact1;
        private String optionNosUniqueCode;
        private String releaseMemo;
        private String receiveMemo;

        public static ViewReqAndRes toDto(DeliveryReadyNaverItemViewProj itemViewProj) {
            ViewReqAndRes dto = ViewReqAndRes.builder()
                .deliveryReadyItem(DeliveryReadyNaverItemDto.toDto(itemViewProj.getDeliveryReadyItem()))
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
