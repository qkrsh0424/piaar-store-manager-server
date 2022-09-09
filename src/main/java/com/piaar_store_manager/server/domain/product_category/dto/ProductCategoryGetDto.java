package com.piaar_store_manager.server.domain.product_category.dto;

import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;

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
     * @param entity : ProductCategoryEntity
     * @return ProductCategoryGetDto
     */
    public static ProductCategoryGetDto toDto(ProductCategoryEntity entity) {
        if(entity == null) return null;

        ProductCategoryGetDto dto = ProductCategoryGetDto.builder()
                .cid(entity.getCid())
                .id(entity.getId())
                .name(entity.getName())
                .build();
    
        return dto;
    }
}
