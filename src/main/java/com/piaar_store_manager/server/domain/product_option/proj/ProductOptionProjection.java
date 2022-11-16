package com.piaar_store_manager.server.domain.product_option.proj;

import java.util.List;

import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProjection;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

// TODO :: 제거
public class ProductOptionProjection {

    @Getter
    @ToString
    @Builder
    public static class RelatedProductReceiveAndProductRelease {
        List<ProductReceiveProjection.RelatedProductAndProductOption> productReceive;
        List<ProductReleaseProjection.RelatedProductAndProductOption> productRelease;
    }
}
