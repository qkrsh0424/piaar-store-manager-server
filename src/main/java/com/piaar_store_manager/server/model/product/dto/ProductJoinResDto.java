package com.piaar_store_manager.server.model.product.dto;


import java.util.List;

import com.piaar_store_manager.server.model.product.proj.ProductProj;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;

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
public class ProductJoinResDto {
    ProductGetDto product;
    ProductCategoryGetDto category;
    UserGetDto user;
    List<ProductOptionGetDto> options;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductProj => ProductJoinResDto
     * 
     * @param proj : ProductProj
     * @return ProductJoinResDto
     */
    public static ProductJoinResDto toDto(ProductProj proj){
        ProductJoinResDto dto = ProductJoinResDto.builder()
            .product(ProductGetDto.toDto(proj.getProduct()))
            .category(ProductCategoryGetDto.toDto(proj.getCategory()))
            .user(UserGetDto.toDto(proj.getUser()))
            // .options(proj.getOptions().stream().map(r->ProductOptionGetDto.toDto(r)).collect(Collectors.toList()))
            .build();

        return dto;
    }


}
