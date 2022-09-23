package com.piaar_store_manager.server.domain.erp_order_item.proj;


import com.piaar_store_manager.server.domain.erp_order_item.entity.ErpOrderItemEntity;
import com.piaar_store_manager.server.domain.erp_return_item.proj.ErpReturnItemProj;
import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErpOrderItemProj {
    private ErpOrderItemEntity erpOrderItem;
    private ProductEntity product;
    private ProductOptionEntity productOption;
    private ProductCategoryEntity productCategory;

    public static ErpOrderItemProj toOrderProj(ErpReturnItemProj returnProj) {
        ErpOrderItemProj proj = ErpOrderItemProj.builder()
            .erpOrderItem(returnProj.getErpOrderItem())
            .product(returnProj.getProduct())
            .productOption(returnProj.getProductOption())
            .productCategory(returnProj.getProductCategory())
            .build();

        return proj;
    }
}
