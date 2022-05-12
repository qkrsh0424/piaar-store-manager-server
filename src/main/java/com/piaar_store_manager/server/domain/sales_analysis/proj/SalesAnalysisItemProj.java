package com.piaar_store_manager.server.domain.sales_analysis.proj;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;

public interface SalesAnalysisItemProj {
    ProductEntity getProduct();
    ProductOptionEntity getProductOption();
    ProductCategoryEntity getProductCategory();
    
    Integer getDeliveryReadyNaverSalesUnit();
    Integer getDeliveryReadyCoupangSalesUnit();
    Integer getErpSalesUnit();
}
