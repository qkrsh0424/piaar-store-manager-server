package com.piaar_store_manager.server.domain.product_receive.proj;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;

import lombok.Getter;
import lombok.ToString;

public class ProductReceiveProjection {

    @Getter
    @ToString
    public static class RelatedProductAndProductOption {
        ProductReceiveEntity productReceive;
        ProductOptionEntity productOption;
        ProductEntity product;
    }
}
