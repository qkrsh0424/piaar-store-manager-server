package com.piaar_store_manager.server.domain.product.dto;

import com.piaar_store_manager.server.domain.option_package.dto.OptionPackageDto;
import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product.proj.ProductManagementProj;
import com.piaar_store_manager.server.domain.product.proj.ProductProj;
import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.user.dto.UserGetDto;

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

    @Size(max = 20, message = "'상품코드'는 20자 이내로 입력해주세요.")
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
            .code(entity.getCode())
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

        public static FullJoin toDto(ProductManagementProj proj) {
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());
            ProductCategoryGetDto category = ProductCategoryGetDto.toDto(proj.getCategory());
            List<ProductOptionGetDto> options = proj.getOptions().stream().map(option -> ProductOptionGetDto.toDto(option)).collect(Collectors.toList());

            FullJoin dto = FullJoin.builder()
                    .product(product)
                    .category(category)
                    .options(options)
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

    /*
     * [221005] FEAT  
     * 상품 & 옵션 생성 시 넘어오는 객체. product, product 하위 데이터 option.
     */
    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductAndOptions {
        @Valid
        ProductGetDto product;
        @Valid
        List<ProductOptionGetDto> options;

        public static ProductAndOptions toDto(ProductManagementProj proj) {
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());
            List<ProductOptionGetDto> options = proj.getOptions().stream().map(option -> ProductOptionGetDto.toDto(option)).collect(Collectors.toList());

            ProductAndOptions dto = ProductAndOptions.builder()
                    .product(product)
                    .options(options)
                    .build();

            return dto;
        }
    }
}
