package com.piaar_store_manager.server.domain.product.dto;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product.proj.ProductProjection;
import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductGetDto {

    private Integer cid;
    private UUID id;
    private String code;

    @NotBlank(message = "'상품명'을 입력해주세요.")
    @Size(max = 100, message = "'상품명'은 100자 이내로 입력해주세요.")
    private String defaultName;

    @Size(max = 100, message = "'상품설명'은 100자 이내로 입력해주세요.")
    private String managementName;

    private String imageUrl;
    
    @Size(max = 100, message = "'이미지 파일명'은 100자 이내로 입력해주세요.")
    private String imageFileName;

    private String purchaseUrl;
    private String memo;

    private Boolean stockManagement;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;
    private Integer productCategoryCid;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductEntity => ProductGetDto
     *
     * @param entity : ProductEntity
     * @return ProductGetDto
     */
    public static ProductGetDto toDto(ProductEntity entity) {
        if(entity == null) return null;
        
        ProductGetDto productDto = ProductGetDto.builder()
            .cid(entity.getCid())
            .id(entity.getId())
            .code(entity.getCode() != null ? entity.getCode().trim() : null)
            .defaultName(entity.getDefaultName())
            .managementName(entity.getManagementName())
            .imageUrl(entity.getImageUrl())
            .purchaseUrl(entity.getPurchaseUrl())
            .imageFileName(entity.getImageFileName())
            .memo(entity.getMemo())
            .stockManagement(entity.getStockManagement())
            .productCategoryCid(entity.getProductCategoryCid())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .updatedBy(entity.getUpdatedBy())
            .build();

        return productDto;
    }

    /*
     * 필수값 항목은 null인지 검사하지 않고 앞뒤공백제거 실행
     * 필수값이 아닌 항목은 null이 아니라면 앞뒤공백제거를 실행한다
     */
    public static void removeBlank(ProductGetDto productDto) {
        if(productDto == null) return;

        productDto.setDefaultName(productDto.getDefaultName().strip())
            .setManagementName(productDto.getManagementName() != null ? productDto.getManagementName().strip() : null)
            .setPurchaseUrl(productDto.getPurchaseUrl() != null ? productDto.getPurchaseUrl().strip() : null)
            .setMemo(productDto.getMemo() != null ? productDto.getMemo().strip() : null);
    }

    /**
     * product, product와 Full Join(fj) 연관관계에 놓여있는 category, options으로 구성된 객체
     */
    @Getter @Setter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedCategoryAndOptions {
        ProductGetDto product;
        ProductCategoryGetDto category;
        List<ProductOptionGetDto> options;

        public static RelatedCategoryAndOptions toDto(ProductProjection.RelatedCategoryAndOptions proj) {
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());
            ProductCategoryGetDto category = ProductCategoryGetDto.toDto(proj.getCategory());
            List<ProductOptionGetDto> options = proj.getOptions().stream().map(option -> ProductOptionGetDto.toDto(option)).collect(Collectors.toList());

            RelatedCategoryAndOptions dto = RelatedCategoryAndOptions.builder()
                    .product(product)
                    .category(category)
                    .options(options)
                    .build();

            return dto;
        }
    }

    /*
     * 상품&옵션으로 구성된 객체. product, product의 하위 options
     */
    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedOptions {
        @Valid
        ProductGetDto product;
        @Valid
        List<ProductOptionGetDto> options;
    }
}
