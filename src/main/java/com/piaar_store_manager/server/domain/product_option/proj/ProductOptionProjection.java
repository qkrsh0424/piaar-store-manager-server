package com.piaar_store_manager.server.domain.product_option.proj;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;

import lombok.Getter;
import lombok.ToString;

public class ProductOptionProjection {
    
    @Getter
    @ToString
    public static class RelatedProduct {
        ProductOptionEntity option;
        ProductEntity product;
    }
}
