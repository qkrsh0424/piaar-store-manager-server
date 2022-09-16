package com.piaar_store_manager.server.domain.erp_order_item.service.strategy.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.piaar_store_manager.server.domain.erp_order_item.service.ErpOrderItemService;
import com.piaar_store_manager.server.domain.erp_order_item.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.erp_order_item.type.ErpOrderItemObjectType;
import com.piaar_store_manager.server.domain.erp_order_item.vo.ErpOrderItemVo;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ErpOrderItemReleaseBasicStrategyImpl implements SearchStrategy {
    private final ErpOrderItemService erpOrderItemService;
    private final ProductOptionService productOptionService;
    
    @Override
    public ErpOrderItemObjectType findObjectType() {
        return ErpOrderItemObjectType.ReleaseBasic;
    }

    @Override
    public <T> List<T> searchBatch(Map<String, Object> params) {
        List<ErpOrderItemProj> itemProjs = erpOrderItemService.findAllM2OJByReleaseItem(params);   // 페이징 처리 x
        return this.setReleaseOptionStockUnitAndToVos(itemProjs);
    }

    @Override
    public <T> Page<T> searchBatchByPaging(Map<String, Object> params, Pageable pageable) {
        Page<ErpOrderItemProj> itemPages = erpOrderItemService.findReleaseItemM2OJByPage(params, pageable);

        /*
        조건별 페이지별 ErpOrderItemProj Page 데이터를 가져온다.
         */
        List<ErpOrderItemProj> itemProjs = itemPages.getContent();    // 페이징 처리 o

        // 옵션재고수량 추가 및 vos 변환
        List<ErpOrderItemVo> erpOrderItemVos = this.setReleaseOptionStockUnitAndToVos(itemProjs);

        return new PageImpl(erpOrderItemVos, pageable, itemPages.getTotalElements());
    }

    @Override
    public <T> List<T> searchBatchByIds(List<UUID> ids, Map<String, Object> params) {
        List<ErpOrderItemProj> itemProjs = erpOrderItemService.findAllM2OJByReleasedItem(ids, params); // 페이징 처리 x
        return this.setReleaseOptionStockUnitAndToVos(itemProjs);
    }

    private <T> List<T> setReleaseOptionStockUnitAndToVos(List<ErpOrderItemProj> itemProjs) {
        /*
        ErpOrderItemProj 에서 옵션 엔터티가 존재하는 것들만 리스트를 가져온다.
         */
        List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);
        /*
        옵션 엔터티 리스트에 received unit, released unit, stockSum unit 을 셋 해준다.
         */
        productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);
        /*
        ErpOrderItemProjs -> ErpOrderItemVos 변환
         */
        List<ErpOrderItemVo> erpOrderItemVos = itemProjs.stream().map(ErpOrderItemVo::toVo).collect(Collectors.toList());
        /*
        ErpOrderItemVos 에 옵션 재고 수량을 셋 해준다.
         */
        ErpOrderItemVo.setReleaseOptionStockUnitForList(erpOrderItemVos, optionEntities);
        return erpOrderItemVos.stream().map(vo -> (T)vo).collect(Collectors.toList());
    }
}
