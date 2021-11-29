package com.piaar_store_manager.server.model.product_release.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.model.product.dto.ProductGetDto;
import com.piaar_store_manager.server.model.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.model.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.model.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.model.user.dto.UserGetDto;

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
public class ProductReleaseJoinResDto {
    ProductReleaseGetDto release;
    ProductGetDto product;
    ProductCategoryGetDto category;
    UserGetDto user;
    ProductOptionGetDto option;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReleaseProj => ProductReleaseJoinResDto
     * 
     * @param projs : ProductReleaseProj
     * @return ProductReleaseJoinResDto
     */
    public static ProductReleaseJoinResDto toDto(ProductReleaseProj proj) {
        ProductReleaseJoinResDto dto = ProductReleaseJoinResDto.builder()
            .release(ProductReleaseGetDto.toDto(proj.getProductRelease()))
            .option(ProductOptionGetDto.toDto(proj.getProductOption()))
            .product(ProductGetDto.toDto(proj.getProduct()))
            .category(ProductCategoryGetDto.toDto(proj.getCategory()))
            .user(UserGetDto.toDto(proj.getUser()))
            .build();

        return dto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * List::ProductReleaseProj:: => List::ProductReleaseJoinResDto::
     * 
     * @param projs : List::ProductReleaseProj::
     * @return List::ProductReleaseJoinResDto::
     */
    public static List<ProductReleaseJoinResDto> toDtos(List<ProductReleaseProj> projs) {
        List<ProductReleaseJoinResDto> dtos = projs.stream().map(proj -> {
            ProductReleaseJoinResDto dto = ProductReleaseJoinResDto.builder()
                .release(ProductReleaseGetDto.toDto(proj.getProductRelease()))
                .option(ProductOptionGetDto.toDto(proj.getProductOption()))
                .product(ProductGetDto.toDto(proj.getProduct()))
                .category(ProductCategoryGetDto.toDto(proj.getCategory()))
                .user(UserGetDto.toDto(proj.getUser()))
                .build();

            return dto;
        }).collect(Collectors.toList());
        
        return dtos;
    }
}
