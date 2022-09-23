package com.piaar_store_manager.server.domain.erp_return_item.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter @Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ErpReturnItemDto {
    private UUID id;
    private String waybillNumber;
    private String courier;
    private String transportType;
    private String deliveryChargeReturnType;
    private String receiveLocation;
    private String returnReasonType;
    private String returnReasonDetail;
    private String managementMemo1;
    private String managementMemo2;
    private String managementMemo3;
    private String managementMemo4;
    private String managementMemo5;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private String collectYn;
    private LocalDateTime collectAt;
    private String collectCompleteYn;
    private LocalDateTime collectCompleteAt;
    private String returnCompleteYn;
    private LocalDateTime returnCompleteAt;
    private String returnRejectYn;
    private LocalDateTime returnRejectAt;
    private String defectiveYn;
    private String stockReflectYn;
    private UUID erpOrderItemId;
}
