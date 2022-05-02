package com.piaar_store_manager.server.domain.product_release.dto;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProductReleaseJoinOptionDto {
    ProductReleaseGetDto release;
    ProductOptionGetDto option;
    ProductGetDto product;
    
    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReleaseProj => ProductReleaseJoinOptionDto
     * 
     * @param projs : ProductReleaseProj
     * @return ProductReleaseJoinOptionDto
     */
    public static ProductReleaseJoinOptionDto toDto(ProductReleaseProj proj) {
        ProductReleaseJoinOptionDto dto = ProductReleaseJoinOptionDto.builder()
            .release(ProductReleaseGetDto.toDto(proj.getProductRelease()))
            .option(ProductOptionGetDto.toDto(proj.getProductOption()))
            .product(ProductGetDto.toDto(proj.getProduct()))
            .build();

        return dto;
    }
}
