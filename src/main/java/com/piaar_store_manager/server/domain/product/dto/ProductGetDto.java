package com.piaar_store_manager.server.domain.product.dto;

import com.piaar_store_manager.server.domain.option_package.dto.OptionPackageDto;
import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product.proj.ProductProj;
import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.user.dto.UserGetDto;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductGetDto {
    private Integer cid;
    private UUID id;
    private String code;
    private String manufacturingCode;
    private String managementNumber;
    private String defaultName;
    private String managementName;
    private String imageUrl;
    private String imageFileName;
    private String purchaseUrl;
    private String memo;
    // private String hsCode;
    // private String style;
    // private String tariffRate;
    // private Integer defaultWidth;
    // private Integer defaultLength;
    // private Integer defaultHeight;
    // private Integer defaultQuantity;
    // private Integer defaultWeight;
    private Integer defaultTotalPurchasePrice;
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
            .code(entity.getCode())
            .manufacturingCode(entity.getManufacturingCode())
            .managementNumber(entity.getManagementNumber())
            .defaultName(entity.getDefaultName())
            .managementName(entity.getManagementName())
            .imageUrl(entity.getImageUrl())
            .purchaseUrl(entity.getPurchaseUrl())
            .imageFileName(entity.getImageFileName())
            .memo(entity.getMemo())
            // .hsCode(entity.getHsCode())
            // .tariffRate(entity.getTariffRate())
            // .style(entity.getStyle())
            // .tariffRate(entity.getTariffRate())
            // .defaultWidth(entity.getDefaultWidth())
            // .defaultLength(entity.getDefaultLength())
            // .defaultHeight(entity.getDefaultHeight())
            // .defaultQuantity(entity.getDefaultQuantity())
            // .defaultWeight(entity.getDefaultWeight())
            .defaultTotalPurchasePrice(entity.getDefaultTotalPurchasePrice())
            .stockManagement(entity.getStockManagement())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .updatedBy(entity.getUpdatedBy())
            .productCategoryCid(entity.getProductCategoryCid())
            .build();

        return productDto;
    }

    /**
     * product, product와 Many To One Join(m2oj) 연관관계에 놓여있는 user, category로 구성된 객체
     */
    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManyToOneJoin {
        ProductGetDto product;
        ProductCategoryGetDto category;
        UserGetDto user;

        public static ManyToOneJoin toDto(ProductProj proj){
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());
            ProductCategoryGetDto category = ProductCategoryGetDto.toDto(proj.getCategory());
            UserGetDto user = UserGetDto.toDto(proj.getUser());

            ManyToOneJoin dto = ManyToOneJoin.builder()
                    .product(product)
                    .category(category)
                    .user(user)
                    .build();

            return dto;
        }
    }

    /**
     * product, product와 Full Join(fj) 연관관계에 놓여있는 user, category, option으로 구성된 객체
     */
    @Getter @Setter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FullJoin {
        ProductGetDto product;
        ProductCategoryGetDto category;
        UserGetDto user;
        List<ProductOptionGetDto> options;

        // require option
        public static FullJoin toDto(ProductProj proj){
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());
            ProductCategoryGetDto category = ProductCategoryGetDto.toDto(proj.getCategory());
            UserGetDto user = UserGetDto.toDto(proj.getUser());

            FullJoin dto = FullJoin.builder()
                    .product(product)
                    .category(category)
                    .user(user)
                    .build();

            return dto;
        }
    }

    /**
     * 상품&옵션&패키지 생성 시 넘어오는 객체. product, product 하위 데이터 option, option의 하위 데이터 package로 구성된 객체
     */
    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReq {
        ProductGetDto productDto;
        List<ProductOptionGetDto> optionDtos;
        List<OptionPackageDto> packageDtos;
    }
}
