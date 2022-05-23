package com.piaar_store_manager.server.domain.delivery_ready.naver.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.piaar_store_manager.server.domain.delivery_ready.naver.dto.DeliveryReadyNaverItemDto;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Entity
@Builder
@Getter @Setter
@ToString
@Table(name = "delivery_ready_naver_item")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryReadyNaverItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "prod_order_number")
    private String prodOrderNumber; // 상품주문번호(0)

    @Column(name = "order_number")
    private String orderNumber; // 주문번호(1)

    @Column(name = "sales_channel")
    private String salesChannel; // 판매채널(7)

    @Column(name = "buyer")
    private String buyer; // 구매자명(8)

    @Column(name = "buyer_id")
    private String buyerId; // 구매자ID(9)

    @Column(name = "receiver")
    private String receiver; // 수취인명(10)

    @Column(name = "payment_date")
    private LocalDateTime paymentDate; // 결제일(14)

    @Column(name = "prod_number")
    private String prodNumber; // 상품번호(15)

    @Column(name = "prod_name")
    private String prodName; // 상품명(16)

    @Column(name = "option_info")
    private String optionInfo; // 옵션정보(18)

    @Column(name = "option_management_code")
    private String optionManagementCode; // 옵션관리코드(19) / null 체크

    @Column(name = "unit")
    private Integer unit; // 수량(20)

    @Column(name = "order_confirmation_date")
    private LocalDateTime orderConfirmationDate; // 발주확인일(27)

    @Column(name = "shipment_due_date")
    private LocalDateTime shipmentDueDate; // 발송기한(28)

    @Column(name = "shipment_cost_bundle_number")
    private String shipmentCostBundleNumber; // 배송비 묶음번호(32)

    @Column(name = "seller_prod_code")
    private String sellerProdCode; // 판매자 상품코드(37) / null 체크

    @Column(name = "seller_inner_code1")
    private String sellerInnerCode1; // 판매자 내부코드1(38) / null 체크

    @Column(name = "seller_inner_code2")
    private String sellerInnerCode2; // 판매자 내부코드2(39) / null 체크

    @Column(name = "receiver_contact1")
    private String receiverContact1; // 수취인연락처1(40)

    @Column(name = "receiver_contact2")
    private String receiverContact2; // 수취인연락처2(41) / null 체크

    @Column(name = "destination")
    private String destination; // 배송지(42)

    @Column(name = "buyer_contact")
    private String buyerContact; // 구매자연락처(43)

    @Column(name = "zip_code")
    private String zipCode; // 우편번호(44)

    @Column(name = "delivery_message")
    private String deliveryMessage; // 배송메세지(45) / null 체크

    @Column(name = "release_area")
    private String releaseArea; // 출고지(46)

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime; // 주문일시(56)

    @Column(name = "release_option_code")
    private String releaseOptionCode;

    @Column(name = "piaar_memo1")
    private String piaarMemo1;

    @Column(name = "piaar_memo2")
    private String piaarMemo2;

    @Column(name = "piaar_memo3")
    private String piaarMemo3;

    @Column(name = "piaar_memo4")
    private String piaarMemo4;

    @Column(name = "piaar_memo5")
    private String piaarMemo5;

    @Column(name = "piaar_memo6")
    private String piaarMemo6;

    @Column(name = "piaar_memo7")
    private String piaarMemo7;

    @Column(name = "piaar_memo8")
    private String piaarMemo8;

    @Column(name = "piaar_memo9")
    private String piaarMemo9;

    @Column(name = "piaar_memo10")
    private String piaarMemo10;

    @Column(name = "released")
    private Boolean released; // 출고여부

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "release_completed")
    private Boolean releaseCompleted; // 재고반영 여부

    @Column(name = "delivery_ready_file_cid")
    private Integer deliveryReadyFileCid;

    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyNaverItemDto => DeliveryReadyNaverItemEntity
     * 
     * @param dto : DeliveryReadyNaverItemDto
     * @return  DeliveryReadyNaverItemEntity
     */
    public static DeliveryReadyNaverItemEntity toEntity(DeliveryReadyNaverItemDto dto){
        DeliveryReadyNaverItemEntity entity = DeliveryReadyNaverItemEntity.builder()
            .cid(dto.getCid())
            .id(dto.getId())
            .prodOrderNumber(dto.getProdOrderNumber())
            .orderNumber(dto.getOrderNumber())
            .salesChannel(dto.getSalesChannel())
            .buyer(dto.getBuyer())
            .buyerId(dto.getBuyerId())
            .receiver(dto.getReceiver())
            .paymentDate(dto.getPaymentDate())
            .prodNumber(dto.getProdNumber())
            .prodName(dto.getProdName())
            .optionInfo(dto.getOptionInfo())
            .optionManagementCode(dto.getOptionManagementCode())
            .unit(dto.getUnit())
            .orderConfirmationDate(dto.getOrderConfirmationDate())
            .shipmentDueDate(dto.getShipmentDueDate())
            .shipmentCostBundleNumber(dto.getShipmentCostBundleNumber())
            .sellerProdCode(dto.getSellerProdCode())
            .sellerInnerCode1(dto.getSellerInnerCode1())
            .sellerInnerCode2(dto.getSellerInnerCode2())
            .receiverContact1(dto.getReceiverContact1())
            .receiverContact2(dto.getReceiverContact2())
            .destination(dto.getDestination())
            .buyerContact(dto.getBuyerContact())
            .zipCode(dto.getZipCode())
            .deliveryMessage(dto.getDeliveryMessage())
            .releaseArea(dto.getReleaseArea())
            .orderDateTime(dto.getOrderDateTime())
            .releaseOptionCode(dto.getReleaseOptionCode())
            .piaarMemo1(dto.getPiaarMemo1())
            .piaarMemo2(dto.getPiaarMemo2())
            .piaarMemo3(dto.getPiaarMemo3())
            .piaarMemo4(dto.getPiaarMemo4())
            .piaarMemo5(dto.getPiaarMemo5())
            .piaarMemo6(dto.getPiaarMemo6())
            .piaarMemo7(dto.getPiaarMemo7())
            .piaarMemo8(dto.getPiaarMemo8())
            .piaarMemo9(dto.getPiaarMemo9())
            .piaarMemo10(dto.getPiaarMemo10())
            .released(dto.getReleased())
            .releasedAt(dto.getReleasedAt())
            .createdAt(dto.getCreatedAt())
            .releaseCompleted(dto.getReleaseCompleted())
            .deliveryReadyFileCid(dto.getDeliveryReadyFileCid())
            .build();

        return entity;
    }
}
