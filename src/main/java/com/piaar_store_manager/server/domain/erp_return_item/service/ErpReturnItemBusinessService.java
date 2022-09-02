package com.piaar_store_manager.server.domain.erp_return_item.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.erp_return_item.dto.ErpReturnItemDto;
import com.piaar_store_manager.server.domain.erp_return_item.entity.ErpReturnItemEntity;
import com.piaar_store_manager.server.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErpReturnItemBusinessService {
    private final ErpReturnItemService erpReturnItemService;
    private final UserService userService;

    public void createBatch(List<ErpReturnItemDto> erpReturnItemDtos) {
        UUID USER_ID = userService.getUserId();

        List<ErpReturnItemEntity> entities = erpReturnItemDtos.stream().map(dto -> {
            ErpReturnItemEntity entity = ErpReturnItemEntity.builder()
                .id(UUID.randomUUID())
                .waybillNumber(dto.getWaybillNumber())
                .courier(dto.getCourier())
                .transportType(dto.getTransportType())
                .deliveryChargeReturnYn("n")
                .deliveryChargeReturnType(dto.getDeliveryChargeReturnType())
                .receiveLocation(dto.getReceiveLocation())
                .returnReasonTypeCid(dto.getReturnReasonTypeCid())
                .returnReasonDetail(dto.getReturnReasonDetail())
                .managementMemo1(dto.getManagementMemo1())
                .managementMemo2(dto.getManagementMemo2())
                .managementMemo3(dto.getManagementMemo3())
                .managementMemo4(dto.getManagementMemo4())
                .managementMemo5(dto.getManagementMemo5())
                .createdAt(LocalDateTime.now())
                .createdBy(USER_ID)
                .collectYn("n")
                .collectAt(dto.getCollectAt())
                .collectCompleteYn("n")
                .collectCompleteAt(dto.getCollectCompleteAt())
                .returnCompleteYn("n")
                .returnCompleteAt(dto.getReturnCompleteAt())
                .holdYn("n")
                .holdAt(dto.getHoldAt())
                .returnRejectYn("n")
                .returnRejectAt(dto.getReturnRejectAt())
                .erpOrderItemId(dto.getErpOrderItemId())
                .build();

            return entity;
        }).collect(Collectors.toList());

        erpReturnItemService.bulkInsert(entities);
    }
    
}
