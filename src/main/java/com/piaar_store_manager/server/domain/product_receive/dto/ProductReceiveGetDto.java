package com.piaar_store_manager.server.domain.product_receive.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.product_receive.entity.ProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProj;
import com.piaar_store_manager.server.domain.user.dto.UserGetDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Integer productOptionCid;

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
            .build();

        return dto;
    }

    /**
     * receive, receive와 Many To One Join(m2oj) 연관관계에 놓여있는 product, category, user, option으로 구성된 객체
     */
    @Data
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManyToOneJoin {
        ProductReceiveGetDto receive;
        ProductGetDto product;
        ProductCategoryGetDto category;
        UserGetDto user;
        ProductOptionGetDto option;

        public static ManyToOneJoin toDto(ProductReceiveProj proj) {
            ProductReceiveGetDto receive = ProductReceiveGetDto.toDto(proj.getProductReceive());
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());
            ProductCategoryGetDto category = ProductCategoryGetDto.toDto(proj.getCategory());
            UserGetDto user = UserGetDto.toDto(proj.getUser());
            ProductOptionGetDto option = ProductOptionGetDto.toDto(proj.getProductOption());

            ManyToOneJoin dto = ManyToOneJoin.builder()
                    .receive(receive)
                    .product(product)
                    .category(category)
                    .user(user)
                    .option(option)
                    .build();

            return dto;
        }
    }

    /**
     * receive, receive 데이터에 대응되는 option, product를 포함한 객체
     */
    @Data
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinProdAndOption {
        ProductReceiveGetDto receive;
        ProductOptionGetDto option;
        ProductGetDto product;

        public static JoinProdAndOption toDto(ProductReceiveProj proj) {
            ProductReceiveGetDto receive = ProductReceiveGetDto.toDto(proj.getProductReceive());
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());
            ProductOptionGetDto option = ProductOptionGetDto.toDto(proj.getProductOption());

            JoinProdAndOption dto = JoinProdAndOption.builder()
                    .receive(receive)
                    .product(product)
                    .option(option)
                    .build();

            return dto;
        }
    }
}
