package com.piaar_store_manager.server.domain.erp_return_item.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Getter
@Table(name = "erp_return_item")
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErpReturnItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid")
    private Integer cid;

    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Setter
    @Column(name = "waybill_number")
    private String waybillNumber;

    @Setter
    @Column(name = "courier")
    private String courier;

    @Setter
    @Column(name = "transport_type")
    private String transportType;
    
    @Setter
    @Column(name = "delivery_charge_return_yn")
    private String deliveryChargeReturnYn;
    
    @Setter
    @Column(name = "delivery_charge_return_type")
    private String deliveryChargeReturnType;

    @Column(name = "receive_location")
    private String receiveLocation;

    @Setter
    @Column(name = "return_reason_type")
    private String returnReasonType;

    @Setter
    @Column(name = "return_reason_detail")
    private String returnReasonDetail;

    @Setter
    @Column(name = "management_memo1")
    private String managementMemo1;

    @Setter
    @Column(name = "management_memo2")
    private String managementMemo2;

    @Setter
    @Column(name = "management_memo3")
    private String managementMemo3;

    @Setter
    @Column(name = "management_memo4")
    private String managementMemo4;

    @Setter
    @Column(name = "management_memo5")
    private String managementMemo5;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Type(type = "uuid-char")
    @Column(name = "created_by")
    private UUID createdBy;

    @Setter
    @Column(name = "collect_yn", columnDefinition = "n")
    private String collectYn;

    @Setter
    @Column(name = "collect_at")
    private LocalDateTime collectAt;

    @Setter
    @Column(name = "collect_complete_yn", columnDefinition = "n")
    private String collectCompleteYn;

    @Setter
    @Column(name = "collect_complete_at")
    private LocalDateTime collectCompleteAt;

    @Setter
    @Column(name = "return_complete_yn", columnDefinition = "n")
    private String returnCompleteYn;

    @Setter
    @Column(name = "return_complete_at")
    private LocalDateTime returnCompleteAt;

    @Setter
    @Column(name = "hold_yn", columnDefinition = "n")
    private String holdYn;

    @Setter
    @Column(name = "hold_at")
    private LocalDateTime holdAt;

    @Setter
    @Column(name = "return_reject_yn", columnDefinition = "n")
    private String returnRejectYn;

    @Setter
    @Column(name = "return_reject_at")
    private LocalDateTime returnRejectAt;

    @Type(type = "uuid-char")
    @Column(name = "erp_order_item_id")
    private UUID erpOrderItemId;
}
