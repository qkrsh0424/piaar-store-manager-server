package com.piaar_store_manager.server.domain.product_release.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_release.entity.ProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProj;
import com.piaar_store_manager.server.domain.user.dto.UserGetDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    /**
     * release, release와 Many To One Join(m2oj) 연관관계에 놓여있는 product, category, user, option으로 구성된 객체
     */
    @Getter @Setter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManyToOneJoin {
        ProductReleaseGetDto release;
        ProductGetDto product;
        ProductCategoryGetDto category;
        UserGetDto user;
        ProductOptionGetDto option;

        public static ManyToOneJoin toDto(ProductReleaseProj proj) {
            ProductReleaseGetDto release = ProductReleaseGetDto.toDto(proj.getProductRelease());
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());
            ProductCategoryGetDto category = ProductCategoryGetDto.toDto(proj.getCategory());
            UserGetDto user = UserGetDto.toDto(proj.getUser());
            ProductOptionGetDto option = ProductOptionGetDto.toDto(proj.getProductOption());

            ManyToOneJoin dto = ManyToOneJoin.builder()
                    .release(release)
                    .product(product)
                    .category(category)
                    .user(user)
                    .option(option)
                    .build();

            return dto;
        }
    }

    /**
     * releaes, releaes 데이터에 대응되는 option, product를 포함한 객체
     */
    @Getter @Setter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinProdAndOption {
        ProductReleaseGetDto release;
        ProductOptionGetDto option;
        ProductGetDto product;

        public static JoinProdAndOption toDto(ProductReleaseProj proj) {
            ProductReleaseGetDto release = ProductReleaseGetDto.toDto(proj.getProductRelease());
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());
            ProductOptionGetDto option = ProductOptionGetDto.toDto(proj.getProductOption());

            JoinProdAndOption dto = JoinProdAndOption.builder()
                    .release(release)
                    .product(product)
                    .option(option)
                    .build();

            return dto;
        }
    }
}
