package com.piaar_store_manager.server.domain.product_release.proj;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;

import lombok.Getter;
import lombok.ToString;

public class ProductReleaseProjection {
    
    @Getter
    @ToString
    public static class RelatedProductAndProductOption {
        ProductReleaseEntity productRelease;
        ProductOptionEntity productOption;
        ProductEntity product;
    }
}
