package com.piaar_store_manager.server.domain.product.proj;

import java.util.Set;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;

import lombok.Getter;
import lombok.ToString;

public class ProductProjection {
    
    @Getter
    @ToString
    public static class RelatedCategoryAndOptions {
        ProductEntity product;
        ProductCategoryEntity category;
        Set<ProductOptionEntity> options;
    }
}
