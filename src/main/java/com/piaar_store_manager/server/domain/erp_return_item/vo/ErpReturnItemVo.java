package com.piaar_store_manager.server.domain.erp_return_item.vo;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.piaar_store_manager.server.domain.erp_order_item.dto.ErpOrderItemDto;
import com.piaar_store_manager.server.domain.erp_order_item.entity.ErpOrderItemEntity;
import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.piaar_store_manager.server.domain.erp_order_item.vo.ErpOrderItemVo;
import com.piaar_store_manager.server.domain.erp_return_item.proj.ErpReturnItemProj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Getter @Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErpReturnItemVo {
    private UUID id;
    private String waybillNumber;
    private String courier;
    private String transportType;
    private String deliveryChargeReturnYn;
    private String deliveryChargeReturnType;
    private String receiveLocation;
    private String returnReasonType;
    private String returnReasonDetail;
    private String managementMemo1;
    private String managementMemo2;
    private String managementMemo3;
    private String managementMemo4;
    private String managementMemo5;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    private UUID createdBy;
    private String collectYn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime collectAt;
    private String collectCompleteYn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime collectCompleteAt;
    private String returnCompleteYn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime returnCompleteAt;
    private String holdYn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime holdAt;
    private String returnRejectYn;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime returnRejectAt;
    private UUID erpOrderItemId;
    private ErpOrderItemVo erpOrderItem;

    public static ErpReturnItemVo toVo(ErpReturnItemProj proj) {
        if(proj == null) return null;

        ErpOrderItemProj erpOrderItemProj = ErpOrderItemProj.toOrderProj(proj);
        ErpOrderItemVo erpOrderItemVo = ErpOrderItemVo.toVo(erpOrderItemProj);

        ErpReturnItemVo itemVo = ErpReturnItemVo.builder()
            .id(proj.getErpReturnItem().getId())
            .waybillNumber(proj.getErpReturnItem().getWaybillNumber())
            .courier(proj.getErpReturnItem().getCourier())
            .transportType(proj.getErpReturnItem().getTransportType())
            .deliveryChargeReturnYn(proj.getErpReturnItem().getDeliveryChargeReturnYn())
            .deliveryChargeReturnType(proj.getErpReturnItem().getDeliveryChargeReturnType())
            .receiveLocation(proj.getErpReturnItem().getReceiveLocation())
            .returnReasonType(proj.getErpReturnItem().getReturnReasonType())
            .returnReasonDetail(proj.getErpReturnItem().getReturnReasonDetail())
            .managementMemo1(proj.getErpReturnItem().getManagementMemo1())
            .managementMemo2(proj.getErpReturnItem().getManagementMemo2())
            .managementMemo3(proj.getErpReturnItem().getManagementMemo3())
            .managementMemo4(proj.getErpReturnItem().getManagementMemo4())
            .managementMemo5(proj.getErpReturnItem().getManagementMemo5())
            .createdAt(proj.getErpReturnItem().getCreatedAt())
            .createdBy(proj.getErpReturnItem().getCreatedBy())
            .collectYn(proj.getErpReturnItem().getCollectYn())
            .collectAt(proj.getErpReturnItem().getCollectAt())
            .collectCompleteYn(proj.getErpReturnItem().getCollectCompleteYn())
            .collectCompleteAt(proj.getErpReturnItem().getCollectCompleteAt())
            .returnCompleteYn(proj.getErpReturnItem().getReturnCompleteYn())
            .returnCompleteAt(proj.getErpReturnItem().getReturnCompleteAt())
            .holdYn(proj.getErpReturnItem().getHoldYn())
            .holdAt(proj.getErpReturnItem().getHoldAt())
            .returnRejectYn(proj.getErpReturnItem().getReturnRejectYn())
            .returnRejectAt(proj.getErpReturnItem().getReturnRejectAt())
            .erpOrderItemId(proj.getErpReturnItem().getErpOrderItemId())
            .erpOrderItem(erpOrderItemVo)
            .build();
        
        return itemVo;
    }
}
