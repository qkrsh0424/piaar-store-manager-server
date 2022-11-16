package com.piaar_store_manager.server.domain.product_option.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.piaar_store_manager.server.domain.option_package.dto.OptionPackageDto;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_category.dto.ProductCategoryGetDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection;
import com.piaar_store_manager.server.domain.product_receive.dto.ProductReceiveGetDto;
import com.piaar_store_manager.server.domain.product_release.dto.ProductReleaseGetDto;
import com.piaar_store_manager.server.domain.user.dto.UserGetDto;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionGetDto {
    // private Integer cid;
    // private UUID id;
    // private String code;
    // // private String nosUniqueCode;
    // private String defaultName;
    // private String managementName;
    // private Integer salesPrice;
    // // private Integer stockUnit;
    // private Integer totalPurchasePrice;     // 추가된 항목
    // private String status;
    // private String memo;
    // private String releaseLocation;
    // // private String imageUrl;
    // // private String imageFileName;
    // // private String color;
    // // private String unitCny;
    // // private String unitKrw;
    // private LocalDateTime createdAt;
    // private UUID createdBy;
    // private LocalDateTime updatedAt;
    // private UUID updatedBy;
    // private Integer productCid;
    // private UUID productId;
    // private String packageYn;
    // private Integer safetyStockUnit;
    
    // private Integer releasedSumUnit;
    // private Integer receivedSumUnit;
    // private Integer stockSumUnit;

    private Integer cid;
    private UUID id;

    @Size(max = 20, message = "'옵션코드'는 20자 이내로 입력해주세요.")
    private String code;

    @NotBlank(message = "'옵션명'을 입력해주세요.")
    @Size(max = 100, message = "'옵션명'은 100자 이내로 입력해주세요.")
    private String defaultName;

    @Size(max = 100, message = "'옵션설명'은 100자 이내로 입력해주세요.")
    private String managementName;

    @PositiveOrZero(message = "'판매가'는 0보다 작은 값을 입력할 수 없습니다.")
    private Integer salesPrice;

    @PositiveOrZero(message = "'매입총합계'는 0보다 작은 값을 입력할 수 없습니다.")
    private Integer totalPurchasePrice;     // 추가된 항목

    @Size(max = 20, message = "'상태'는 20자 이내로 입력해주세요.")
    private String status;

    private String memo;

    @Size(max = 20, message = "'출고지'는 50자 이내로 입력해주세요.")
    private String releaseLocation;

    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;
    private UUID updatedBy;
    private Integer productCid;
    private UUID productId;

    @Pattern(regexp = "^[n|y]$", message = "'패키지 여부'에 올바른 값을 입력해주세요.")
    @Size(min = 1, max = 1)
    private String packageYn;

    @PositiveOrZero(message = "'안전재고수량'은 0보다 작은 값을 입력할 수 없습니다.")
    private Integer safetyStockUnit;
    
    private Integer releasedSumUnit;
    private Integer receivedSumUnit;
    private Integer stockSumUnit;

    /**
     * <b>Convert Method</b>
     * <p>
     * ProductOptionEntity => ProductOptionGetDto
     * 
     * @param entity : ProductOptionEntity
     * @return ProductOptionGetDto
     */
    public static ProductOptionGetDto toDto(ProductOptionEntity entity) {
        if(entity == null) return null;
        
        ProductOptionGetDto dto = ProductOptionGetDto.builder()
                .cid(entity.getCid())
                .id(entity.getId())
                .code(entity.getCode())
                // .nosUniqueCode(entity.getNosUniqueCode())
                .defaultName(entity.getDefaultName())
                .managementName(entity.getManagementName())
                .salesPrice(entity.getSalesPrice())
                .totalPurchasePrice(entity.getTotalPurchasePrice())
                // .stockUnit(entity.getStockUnit())
                .status(entity.getStatus())
                .memo(entity.getMemo())
                .releaseLocation(entity.getReleaseLocation())
                // .imageUrl(entity.getImageUrl())
                // .imageFileName(entity.getImageFileName())
                // .color(entity.getColor())
                // .unitCny(entity.getUnitCny())
                // .unitKrw(entity.getUnitKrw())
                .packageYn(entity.getPackageYn())
                .safetyStockUnit(entity.getSafetyStockUnit())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .productCid(entity.getProductCid())
                .productId(entity.getProductId())
                .build();

        return dto;
    }

    /**
     * option, option과 Many To One Join(m2oj) 연관관계에 놓여있는 product, user, category, option으로 구성된 객체
     */
    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManyToOneJoin {
        ProductOptionGetDto option;
        ProductGetDto product;
        ProductCategoryGetDto category;
        UserGetDto user;

        public static ManyToOneJoin toDto(ProductOptionProj proj) {
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());
            ProductCategoryGetDto category = ProductCategoryGetDto.toDto(proj.getCategory());
            UserGetDto user = UserGetDto.toDto(proj.getUser());
            ProductOptionGetDto option = ProductOptionGetDto.toDto(proj.getProductOption());

            ManyToOneJoin dto = ManyToOneJoin.builder()
                .product(product)
                .category(category)
                .user(user)
                .option(option)
                .build();

            return dto;
        }
    }

    /**
     * 옵션&패키지 생성 시 넘어오는 객체. option, option의 하위 데이터 package로 구성된 객체
     */
    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReq {
        ProductOptionGetDto optionDto;
        List<OptionPackageDto> packageDtos;
    }

    /**
     * 재고관리 페이지 - 입출고현황 확인 시 반환하는 객체.
     */
    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedProductReceiveAndProductRelease {
        List<ProductReceiveGetDto.RelatedProductAndProductOption> productReceive;
        List<ProductReleaseGetDto.RelatedProductAndProductOption> productRelease;


        public static RelatedProductReceiveAndProductRelease toDto(ProductOptionProjection.RelatedProductReceiveAndProductRelease proj) {
            List<ProductReceiveGetDto.RelatedProductAndProductOption> productReceive = proj.getProductReceive().stream().map(r -> ProductReceiveGetDto.RelatedProductAndProductOption.toDto(r)).collect(Collectors.toList());
            List<ProductReleaseGetDto.RelatedProductAndProductOption> productRelease = proj.getProductRelease().stream().map(r -> ProductReleaseGetDto.RelatedProductAndProductOption.toDto(r)).collect(Collectors.toList());


            RelatedProductReceiveAndProductRelease dto = RelatedProductReceiveAndProductRelease.builder()
                .productRelease(productRelease)
                .productReceive(productReceive)
                .build();

            return dto;
        }


    }
}