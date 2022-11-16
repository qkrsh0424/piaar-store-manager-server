package com.piaar_store_manager.server.domain.product_receive.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductReceiveGetDto {
    private Integer cid;
    private UUID id;
    private Integer receiveUnit;
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
     * ProductReceiveEntity => ProductReceiveGetDto
     * 
     * @param entity : ProductReceiveEntity
     * @return ProductReceiveGetDto
     */
    public static ProductReceiveGetDto toDto(ProductReceiveEntity entity) {
        ProductReceiveGetDto dto = ProductReceiveGetDto.builder()
            .cid(entity.getCid())
            .id(entity.getId())
            .receiveUnit(entity.getReceiveUnit())
            .memo(entity.getMemo())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .productOptionCid(entity.getProductOptionCid())
            .productOptionId(entity.getProductOptionId())
            .erpOrderItemId(entity.getErpOrderItemId())
            .build();

        return dto;
    }

    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedProductAndProductOption {
        ProductReceiveGetDto productReceive;
        ProductOptionGetDto productOption;
        ProductGetDto product;

        public static RelatedProductAndProductOption toDto(ProductReceiveProjection.RelatedProductAndProductOption proj) {
            ProductReceiveGetDto productReceive = ProductReceiveGetDto.toDto(proj.getProductReceive());
            ProductOptionGetDto productOption = ProductOptionGetDto.toDto(proj.getProductOption());
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());

            RelatedProductAndProductOption dto = RelatedProductAndProductOption.builder()
                .productReceive(productReceive)
                .productOption(productOption)
                .product(product)
                .build();

            return dto;
        }
    }
}
