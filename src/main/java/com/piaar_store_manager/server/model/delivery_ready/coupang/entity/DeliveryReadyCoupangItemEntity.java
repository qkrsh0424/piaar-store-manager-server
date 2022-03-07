package com.piaar_store_manager.server.model.delivery_ready.coupang.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.piaar_store_manager.server.model.delivery_ready.coupang.dto.DeliveryReadyCoupangItemDto;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@Table(name = "delivery_ready_coupang_item")
@Accessors(chain=true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryReadyCoupangItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "prod_order_number")
    private String prodOrderNumber;     // 상품주문번호 : 주문번호 | 노출상품ID | 옵션ID

    @Column(name = "order_number")
    private String orderNumber;     // 주문번호(2)

    @Column(name = "buyer")
    private String buyer;       // 구매자(24)

    @Column(name = "receiver")
    private String receiver;        // 수취인이름(26)

    @Column(name = "prod_number")
    private String prodNumber;        // 노출상품ID(13)

    @Column(name = "prod_name")
    private String prodName;        // 등록상품명(10)

    @Column(name = "prod_exposure_name")
    private String prodExposureName;        // 노출상품명(12)

    @Column(name = "option_info")
    private String optionInfo;        // 등록옵션명(11)

    @Column(name = "option_management_code")
    private String optionManagementCode;        // 업체상품코드(16)

    @Column(name = "coupang_option_id")
    private String coupangOptionId;        // 옵션ID(14)

    @Column(name = "unit")
    private Integer unit;        // 수량(22)

    @Column(name = "shipment_due_date")
    private Date shipmentDueDate;        // 주문시 출고예정일(7)

    @Column(name = "shipment_cost_bundle_number")
    private String shipmentCostBundleNumber;        // 묶음배송번호(1)
    
    @Column(name = "receiver_contact1")
    private String receiverContact1;        // 수취인전화번호(27)

    @Column(name = "destination")
    private String destination;        // 수취인 주소(29)

    @Column(name = "buyer_contact")
    private String buyerContact;        // 구매자전화번호(25)

    @Column(name = "zip_code")
    private String zipCode;        // 우편번호(28)

    @Column(name = "delivery_message")
    private String deliveryMessage;        // 배송메세지(30)

    @Column(name = "order_date_time")
    private Date orderDateTime;        // 주문일(9)

    @Column(name = "release_option_code")
    private String releaseOptionCode;

    @Column(name = "released")
    private Boolean released;   // 출고여부

    @Column(name = "released_at")
    private Date releasedAt;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "release_completed")
    private Boolean releaseCompleted;   // 재고반영 여부

    @Column(name = "delivery_ready_file_cid")
    private Integer deliveryReadyFileCid;
    
    /**
     * <b>Convert Method</b>
     * <p>
     * DeliveryReadyCoupangItemDto => DeliveryReadyCoupangItemEntity
     * 
     * @param dto : DeliveryReadyCoupangItemDto
     * @return DeliveryReadyCoupangItemEntity
     */
    public static DeliveryReadyCoupangItemEntity toEntity(DeliveryReadyCoupangItemDto dto){
        DeliveryReadyCoupangItemEntity entity = DeliveryReadyCoupangItemEntity.builder()
            .cid(dto.getCid())
            .id(dto.getId())
            .prodOrderNumber(dto.getProdOrderNumber())
            .orderNumber(dto.getOrderNumber())
            .buyer(dto.getBuyer())
            .receiver(dto.getReceiver())
            .prodNumber(dto.getProdNumber())
            .prodName(dto.getProdName())
            .prodExposureName(dto.getProdExposureName())
            .optionInfo(dto.getOptionInfo())
            .optionManagementCode(dto.getOptionManagementCode())
            .coupangOptionId(dto.getCoupangOptionId())
            .unit(dto.getUnit())
            .shipmentDueDate(dto.getShipmentDueDate())
            .shipmentCostBundleNumber(dto.getShipmentCostBundleNumber())
            .receiverContact1(dto.getReceiverContact1())
            .destination(dto.getDestination())
            .buyerContact(dto.getBuyerContact())
            .zipCode(dto.getZipCode())
            .deliveryMessage(dto.getDeliveryMessage())
            .orderDateTime(dto.getOrderDateTime())
            .releaseOptionCode(dto.getReleaseOptionCode())
            .released(dto.getReleased())
            .releasedAt(dto.getReleasedAt())
            .createdAt(dto.getCreatedAt())
            .releaseCompleted(dto.getReleaseCompleted())
            .deliveryReadyFileCid(dto.getDeliveryReadyFileCid())
            .build();

        return entity;
    }
}
