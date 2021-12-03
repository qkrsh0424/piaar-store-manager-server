package com.piaar_store_manager.server.model.product_receive.dto;

import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProductReceiveJoinResDto {
    ProductReceiveGetDto receive;
    ProductGetDto product;
    ProductCategoryGetDto category;
    UserGetDto user;
    ProductOptionGetDto option;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReceiveProj => ProductReceiveJoinResDto
     * 
     * @param proj : ProductReceiveProj
     * @return ProductReceiveJoinResDto
     */
    public static ProductReceiveJoinResDto toDto(ProductReceiveProj proj) {
        ProductReceiveJoinResDto dto = ProductReceiveJoinResDto.builder()
            .receive(ProductReceiveGetDto.toDto(proj.getProductReceive()))
            .option(ProductOptionGetDto.toDto(proj.getProductOption()))
            .product(ProductGetDto.toDto(proj.getProduct()))
            .category(ProductCategoryGetDto.toDto(proj.getCategory()))
            .user(UserGetDto.toDto(proj.getUser()))
            .build();

        return dto;
    }
}
