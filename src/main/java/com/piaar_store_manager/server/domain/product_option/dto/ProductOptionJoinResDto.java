package com.piaar_store_manager.server.domain.product_option.dto;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.user.dto.UserGetDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ProductOptionJoinResDto {
    ProductGetDto product;
    ProductCategoryGetDto category;
    UserGetDto user;
    ProductOptionGetDto option;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductOptionProj => ProductOptionJoinResDto
     * 
     * @param proj : ProductOptionProj
     * @return ProductOptionJoinResDto
     */
    public static ProductOptionJoinResDto toDto(ProductOptionProj proj){
        ProductOptionJoinResDto dto = ProductOptionJoinResDto.builder()
            .product(ProductGetDto.toDto(proj.getProduct()))
            .category(ProductCategoryGetDto.toDto(proj.getCategory()))
            .user(UserGetDto.toDto(proj.getUser()))
            .option(ProductOptionGetDto.toDto(proj.getProductOption()))
            .build();

        return dto;
    }
}
