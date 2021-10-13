package com.piaar_store_manager.server.model.product_category.dto;

import com.piaar_store_manager.server.model.product_category.entity.ProductCategoryEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryGetDto {
    private Integer cid;
    private String id;
    private String name;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductCategoryEntity => ProductCategoryGetDto
     * 
     * @param reqDto : ProductCategoryGetDto
     * @return ProductCategoryGetDto
     */
    public static ProductCategoryGetDto toDto(ProductCategoryEntity reqDto) {
        ProductCategoryGetDto entity = ProductCategoryGetDto.builder()
                .cid(reqDto.getCid())
                .id(reqDto.getId())
                .name(reqDto.getName())
                .build();
    
        return entity;
    }
}
