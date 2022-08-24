package com.piaar_store_manager.server.domain.erp_order_item.service;

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
public class ErpOrderItemM2OJStrategyImpl implements SearchStrategy {
    private final ErpOrderItemService erpOrderItemService;
    private final ProductOptionService productOptionService;

    @Override
    public ErpOrderItemObjectType findObjectType() {
        return ErpOrderItemObjectType.M2OJ;
    }

    @Override
    public <T> List<T> searchBatch(Map<String, Object> params) {
        List<ErpOrderItemProj> itemProjs = erpOrderItemService.findAllM2OJ(params);
        return this.setOptionStockUnitAndToM2OJVos(itemProjs);
    }

    private <T> List<T> setOptionStockUnitAndToM2OJVos(List<ErpOrderItemProj> itemProjs) {
        List<ProductOptionEntity> optionEntities = ProductOptionEntity.getExistList(itemProjs);
        productOptionService.setReceivedAndReleasedAndStockSum(optionEntities);

        List<ErpOrderItemVo.ManyToOneJoin> erpOrderItemM2OJVos = itemProjs.stream().map(ErpOrderItemVo.ManyToOneJoin::toVo).collect(Collectors.toList());
        ErpOrderItemVo.setOptionStockUnitForM2OJList(erpOrderItemM2OJVos, optionEntities);

        return erpOrderItemM2OJVos.stream().map(vo -> (T)vo).collect(Collectors.toList());
    }
}
