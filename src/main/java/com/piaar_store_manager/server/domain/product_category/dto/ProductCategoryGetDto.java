package com.piaar_store_manager.server.domain.product_category.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    private UUID id;
    private String code;

    @NotBlank(message = "'카테고리명'을 입력해주세요.")
    @Size(max = 100, message = "'카테고리명'은 100자 이내로 입력해주세요.")
    private String name;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;


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
                .code(entity.getCode())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    
        return dto;
    }

    /*
     * 필수값 항목은 null인지 검사하지 않고 앞뒤공백제거 실행
     * 필수값이 아닌 항목은 null이 아니라면 앞뒤공백제거를 실행한다
     */
    public static void removeBlank(ProductCategoryGetDto categoryDto) {
        if(categoryDto == null) return;

        categoryDto.setName(categoryDto.getName() != null ? categoryDto.getName().strip() : null);
    }
}
