package com.piaar_store_manager.server.domain.product_release.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReleaseGetDto {
    private Integer cid;
    private UUID id;
    
    @NotNull
    @Positive(message = "'출고수량'은 0보다 큰 값을 입력해주세요.")
    private Integer releaseUnit;
    private String memo;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Integer productOptionCid;
    private UUID productOptionId;
    private UUID erpOrderItemId;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductReleaseEntity => ProductReleaseGetDto
     * 
     * @param entity : ProductReleaseEntity
     * @return ProductReleaseGetDto
     */
    public static ProductReleaseGetDto toDto(ProductReleaseEntity entity) {
        ProductReleaseGetDto dto = ProductReleaseGetDto.builder()
           .cid(entity.getCid())
           .id(entity.getId())
           .releaseUnit(entity.getReleaseUnit())
           .memo(entity.getMemo())
           .createdAt(entity.getCreatedAt())
           .createdBy(entity.getCreatedBy())
           .productOptionCid(entity.getProductOptionCid())
           .productOptionId(entity.getProductOptionId())
           .erpOrderItemId(entity.getErpOrderItemId())
           .build();

        return dto;
    }

    public static void removeBlank(ProductReleaseGetDto releaseDto) {
        if(releaseDto == null) return;

        releaseDto.setMemo(releaseDto.getMemo() != null ? releaseDto.getMemo().strip() : null);
    }

    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedProductAndProductOption {
        ProductReleaseGetDto productRelease;
        ProductOptionGetDto productOption;
        ProductGetDto product;

        public static RelatedProductAndProductOption toDto(ProductReleaseProjection.RelatedProductAndProductOption proj) {
            ProductReleaseGetDto productRelease = ProductReleaseGetDto.toDto(proj.getProductRelease());
            ProductOptionGetDto productOption = ProductOptionGetDto.toDto(proj.getProductOption());
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());

            RelatedProductAndProductOption dto = RelatedProductAndProductOption.builder()
                .productRelease(productRelease)
                .productOption(productOption)
                .product(product)
                .build();

            return dto;
        }
    }
}
