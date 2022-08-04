package com.piaar_store_manager.server.domain.erp_order_item.proj;


import com.piaar_store_manager.server.domain.erp_order_item.entity.ErpOrderItemEntity;
import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErpOrderItemProj {
    ErpOrderItemEntity erpOrderItem;
    ProductEntity product;
    ProductOptionEntity productOption;
    ProductCategoryEntity productCategory;
}
