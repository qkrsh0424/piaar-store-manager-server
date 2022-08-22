package com.piaar_store_manager.server.domain.erp_order_item.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.piaar_store_manager.server.domain.erp_order_item.service.strategy.search.SearchStrategy;
import com.piaar_store_manager.server.domain.erp_order_item.type.ErpOrderItemObjectType;
import com.piaar_store_manager.server.domain.erp_order_item.vo.ErpOrderItemVo;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.service.ProductOptionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ErpOrderItemBasicStrategyImpl implements SearchStrategy {
    private final ErpOrderItemService erpOrderItemService;
    private final ProductOptionService productOptionService;
    
    @Override
    public ErpOrderItemObjectType findObjectType() {
        return ErpOrderItemObjectType.Basic;
    }

    @Override
    public <T> List<T> searchBatch(Map<String, Object> params) {
        String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";
        
        List<ErpOrderItemProj> itemProjs = new ArrayList<>();
        if(matchedCode.equals("optionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJ(params);       // 페이징 처리 x
        }else if(matchedCode.equals("releaseOptionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJByReleaseItem(params);   // 페이징 처리 x
        }
        return this.setOptionStockUnitAndToVos(itemProjs);
    }

    private <T> List<T> setOptionStockUnitAndToVos(List<ErpOrderItemProj> itemProjs) {
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
        ErpOrderItemVo.setOptionStockUnitForList(erpOrderItemVos, optionEntities);
        return erpOrderItemVos.stream().map(vo -> (T)vo).collect(Collectors.toList());
    }
}