package com.piaar_store_manager.server.domain.sales_analysis.proj;

import com.piaar_store_manager.server.model.product.entity.ProductEntity;
import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.model.product_option.entity.ProductOptionEntity;

public interface SalesAnalysisItemProj {
    ProductEntity getProduct();
    ProductOptionEntity getProductOption();
    ProductCategoryEntity getProductCategory();
    
    Integer getDeliveryReadyNaverSalesUnit();
    Integer getDeliveryReadyCoupangSalesUnit();
}
