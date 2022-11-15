package com.piaar_store_manager.server.domain.sub_option_code.proj;

import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.sub_option_code.entity.SubOptionCodeEntity;

import lombok.Getter;
import lombok.ToString;

public class SubOptionCodeProjection {
    
    @Getter
    @ToString
    public static class RelatedProductOption {
        SubOptionCodeEntity subOptionCode;
        ProductOptionEntity productOption;
    }
}
