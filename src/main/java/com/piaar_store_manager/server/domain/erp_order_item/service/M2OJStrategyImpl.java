package com.piaar_store_manager.server.domain.erp_order_item.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class M2OJStrategyImpl implements SearchStrategy {
    private final ErpOrderItemService erpOrderItemService;
    private final ProductOptionService productOptionService;

    @Override
    public ErpOrderItemObjectType findObjectType() {
        return ErpOrderItemObjectType.M2OJ;
    }

    @Override
    public <T> List<T> searchList(Map<String, Object> params) {
        String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";
        
        List<ErpOrderItemProj> itemProjs = new ArrayList<>();
        if (matchedCode.equals("optionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJ(params);
        } else if (matchedCode.equals("releaseOptionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJByReleaseItem(params);
        }
        return this.setOptionStockUnitAndToM2OJVos(itemProjs);
    }

    @Override
    public <T> Page<T> searchListByPage(Map<String, Object> params, Pageable pageable) {
        String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";        // 기본값은 옵션관리코드로 지정
        Page<ErpOrderItemProj> itemPages = null;

        if(matchedCode.equals("optionCode")) {
            itemPages = erpOrderItemService.findAllM2OJByPage(params, pageable);
        }else if(matchedCode.equals("releaseOptionCode")) {
            itemPages = erpOrderItemService.findReleaseItemM2OJByPage(params, pageable);
        }

        /*
        조건별 페이지별 ErpOrderItemProj Page 데이터를 가져온다.
         */
        List<ErpOrderItemProj> itemProjs = itemPages.getContent();    // 페이징 처리 o

        // 옵션재고수량 추가 및 vos 변환
        List<ErpOrderItemVo> erpOrderItemVos = this.setOptionStockUnitAndToVos(itemProjs);

        return new PageImpl(erpOrderItemVos, pageable, itemPages.getTotalElements());
    }

    @Override
    public <T> List<T> searchBatchByIds(List<UUID> ids, Map<String, Object> params) {
        String matchedCode = params.get("matchedCode") != null ? params.get("matchedCode").toString() : "releaseOptionCode";
        
        List<ErpOrderItemProj> itemProjs = new ArrayList<>();
        if(matchedCode.equals("optionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJ(ids, params);       // 페이징 처리 x
        } else if(matchedCode.equals("releaseOptionCode")) {
            itemProjs = erpOrderItemService.findAllM2OJByReleasedItem(ids, params); // 페이징 처리 x
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

    private <T> List<T> setOptionStockUnitAndToM2OJVos(List<ErpOrderItemProj> itemProjs) {
        List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);
        productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);

        List<ErpOrderItemVo.ManyToOneJoin> erpOrderItemM2OJVos = itemProjs.stream().map(ErpOrderItemVo.ManyToOneJoin::toVo).collect(Collectors.toList());
        ErpOrderItemVo.setOptionStockUnitForM2OJList(erpOrderItemM2OJVos, optionEntities);

        return erpOrderItemM2OJVos.stream().map(vo -> (T)vo).collect(Collectors.toList());
    }
}
