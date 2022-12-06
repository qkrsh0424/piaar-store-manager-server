package com.piaar_store_manager.server.domain.erp_order_item.proj;

import com.piaar_store_manager.server.domain.erp_order_item.entity.ErpOrderItemEntity;
import com.piaar_store_manager.server.domain.product.entity.ProductEntity;

import lombok.Getter;
import lombok.ToString;

public class ErpOrderItemProjection {
    
    @Getter
    @ToString
    public static class RelatedProduct {
        ErpOrderItemEntity erpOrderItem;
        ProductEntity product;
    }
}
