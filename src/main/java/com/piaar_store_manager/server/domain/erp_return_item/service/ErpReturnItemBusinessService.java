package com.piaar_store_manager.server.domain.erp_return_item.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piaar_store_manager.server.annotation.RequiredLogin;
import com.piaar_store_manager.server.domain.erp_order_item.vo.ErpOrderItemVo;
import com.piaar_store_manager.server.domain.erp_return_item.dto.ErpReturnItemDto;
import com.piaar_store_manager.server.domain.erp_return_item.entity.ErpReturnItemEntity;
import com.piaar_store_manager.server.domain.erp_return_item.proj.ErpReturnItemProj;
import com.piaar_store_manager.server.domain.erp_return_item.vo.ErpReturnItemVo;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;
import com.piaar_store_manager.server.domain.user.service.UserService;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@RequiredLogin
public class ErpReturnItemBusinessService {
    private final ErpReturnItemService erpReturnItemService;
    private final ProductOptionService productOptionService;
    private final UserService userService;

    /*
     * 반품 데이터는 직접 등록이 존재하지 않는다. erp 출고데이터 -> 반품데이터 생성.
     */
    @Transactional
    public void createBatch(List<ErpReturnItemDto> erpReturnItemDtos) {
        UUID USER_ID = userService.getUserId();

        // 반품데이터
        erpReturnItemDtos.forEach(dto -> {
            if(dto.getReturnReasonType() == null || dto.getReturnReasonType().isBlank()) {
                throw new CustomInvalidDataException("반품 요청 사유는 필수값입니다.");
            }
        });

        List<ErpReturnItemEntity> entities = erpReturnItemDtos.stream().map(dto -> {
            ErpReturnItemEntity entity = ErpReturnItemEntity.builder()
                .id(UUID.randomUUID())
                .waybillNumber(dto.getWaybillNumber())
                .courier(dto.getCourier())
                .transportType(dto.getTransportType())
                .deliveryChargeReturnYn("n")
                .deliveryChargeReturnType(dto.getDeliveryChargeReturnType())
                .receiveLocation(dto.getReceiveLocation())
                .returnReasonType(dto.getReturnReasonType())
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

    public Page<ErpReturnItemVo> searchBatchByPaging(Map<String, Object> params, Pageable pageable) {
        Page<ErpReturnItemProj> itemPages = erpReturnItemService.findAllM2OJByPage(params, pageable);

        List<ErpReturnItemProj> itemProjs = itemPages.getContent();

        List<ErpReturnItemVo> erpReturnItemVos = this.setOptionStockUnitAndToVos(itemProjs);
        return new PageImpl<>(erpReturnItemVos, pageable, itemPages.getTotalElements());
    }

    private List<ErpReturnItemVo> setOptionStockUnitAndToVos(List<ErpReturnItemProj> itemProjs) {
        /*
        ErpOrderItemProj 에서 옵션 엔터티가 존재하는 것들만 리스트를 가져온다.
         */
        List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistListByReturnItem(itemProjs);
        /*
        옵션 엔터티 리스트에 received unit, released unit, stockSum unit 을 셋 해준다.
         */
        productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);
        /*
        ErpOrderItemProjs -> ErpOrderItemVos 변환
         */
        List<ErpReturnItemVo> erpReturnItemVos = itemProjs.stream().map(ErpReturnItemVo::toVo).collect(Collectors.toList());
        /*
        ErpOrderItemVos 에 옵션 재고 수량을 셋 해준다.
         */
        List<ErpOrderItemVo> erpOrderItemVos = erpReturnItemVos.stream().map(vo -> vo.getErpOrderItem()).collect(Collectors.toList());
        ErpOrderItemVo.setOptionStockUnitForList(erpOrderItemVos, optionEntities);
        return erpReturnItemVos;
    }
    
}
