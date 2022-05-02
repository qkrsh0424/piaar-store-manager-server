package com.piaar_store_manager.server.domain.product_receive.dto;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProj;

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
public class ProductReceiveJoinOptionDto {
    ProductReceiveGetDto receive;
    ProductOptionGetDto option;
    ProductGetDto product;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReceiveProj => ProductReceiveJoinOptionDto
     * 
     * @param projs : ProductReceiveProj
     * @return ProductReceiveJoinOptionDto
     */
    public static ProductReceiveJoinOptionDto toDto(ProductReceiveProj proj) {
        ProductReceiveJoinOptionDto dto = ProductReceiveJoinOptionDto.builder()
            .receive(ProductReceiveGetDto.toDto(proj.getProductReceive()))
            .option(ProductOptionGetDto.toDto(proj.getProductOption()))
            .product(ProductGetDto.toDto(proj.getProduct()))
            .build();

        return dto;
    }
}
