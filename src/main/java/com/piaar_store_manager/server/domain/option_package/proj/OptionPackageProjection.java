package com.piaar_store_manager.server.domain.option_package.proj;

import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;

import lombok.*;

public class OptionPackageProjection {

    @Getter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RelatedProductOption {
        OptionPackageEntity optionPackage;
        ProductOptionEntity productOption;
        ProductEntity product;
    }
}

