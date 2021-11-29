package com.piaar_store_manager.server.model.product_option.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.proj.ProductOptionProj;
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

    /**
     * <b>Convert Method</b>
     * <p>
     * List::ProductOptionProj:: => List::ProductOptionJoinResDto::
     * 
     * @param entities : List::ProductOptionProj::
     * @return List::ProductOptionJoinResDto::
     */
    public static List<ProductOptionJoinResDto> toDtos(List<ProductOptionProj> projs){
        List<ProductOptionJoinResDto> dtos = projs.stream().map(proj -> {
            ProductOptionJoinResDto dto = ProductOptionJoinResDto.builder()
                .product(ProductGetDto.toDto(proj.getProduct()))
                .category(ProductCategoryGetDto.toDto(proj.getCategory()))
                .user(UserGetDto.toDto(proj.getUser()))
                .option(ProductOptionGetDto.toDto(proj.getProductOption()))
                .build();

            return dto;
        }).collect(Collectors.toList());
        
        return dtos;
    }
}
