package com.piaar_store_manager.server.model.product_category.dto;

import java.util.List;
import java.util.stream.Collectors;

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
     * @param entity : ProductCategoryEntity
     * @return ProductCategoryGetDto
     */
    public static ProductCategoryGetDto toDto(ProductCategoryEntity entity) {
        ProductCategoryGetDto dto = ProductCategoryGetDto.builder()
                .cid(entity.getCid())
                .id(entity.getId())
                .name(entity.getName())
                .build();
    
        return dto;
    }

    /**
     * <b>Convert Method</b>
     * <p>
     * List::ProductCategoryEntity:: => List::ProductCategoryGetDto::
     * 
     * @param entities : List::ProductCategoryEntity::
     * @return List::ProductCategoryGetDto::
     */
    public static List<ProductCategoryGetDto> toDtos(List<ProductCategoryEntity> entities) {
        List<ProductCategoryGetDto> dtos = entities.stream().map(entity -> {
            ProductCategoryGetDto dto = ProductCategoryGetDto.builder()
                .cid(entity.getCid())
                .id(entity.getId())
                .name(entity.getName())
                .build();
    
            return dto;
        }).collect(Collectors.toList());
    
        return dtos;
    }
}
