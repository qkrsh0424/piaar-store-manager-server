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

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import com.piaar_store_manager.server.domain.option_package.dto.OptionPackageDto;
import com.piaar_store_manager.server.domain.product.dto.ProductGetDto;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionGetDto {

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
    @Max(value = 10000000, message = "'판매가'는 10,000,000보다 큰 값을 입력할 수 없습니다.")
    private Integer salesPrice;

    @PositiveOrZero(message = "'매입총합계'는 0보다 작은 값을 입력할 수 없습니다.")
    @Max(value = 10000000, message = "'매입총합계'는 10,000,000보다 큰 값을 입력할 수 없습니다.")
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
    @Max(value = 10000000, message = "'매입총합계'는 10,000,000보다 큰 값을 입력할 수 없습니다.")
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
                .code(entity.getCode() != null ? entity.getCode().trim() : null)
                .defaultName(entity.getDefaultName())
                .managementName(entity.getManagementName())
                .salesPrice(entity.getSalesPrice())
                .totalPurchasePrice(entity.getTotalPurchasePrice())
                .status(entity.getStatus())
                .memo(entity.getMemo())
                .releaseLocation(entity.getReleaseLocation())
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

    /*
     * 필수값 항목은 null인지 검사하지 않고 공백제거 실행
     * 필수값이 아닌 항목은 null이 아니라면 공백제거를 실행한다
     */
    public static void removeBlank(ProductOptionGetDto optionDto) {
        if(optionDto == null) return;

        optionDto.setDefaultName(optionDto.getDefaultName().strip())
            .setManagementName(optionDto.getManagementName() != null ? optionDto.getManagementName().strip() : null)
            .setStatus(optionDto.getStatus() != null ? optionDto.getStatus().strip() : null)
            .setMemo(optionDto.getMemo() != null ? optionDto.getMemo().strip() : null)
            .setReleaseLocation(optionDto.getReleaseLocation() != null ? optionDto.getReleaseLocation().strip() : null);
    }

    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedProduct {
        ProductOptionGetDto option;
        ProductGetDto product;

        public static RelatedProduct toDto(ProductOptionProjection.RelatedProduct proj) {
            ProductOptionGetDto productOption = ProductOptionGetDto.toDto(proj.getOption());
            ProductGetDto product = ProductGetDto.toDto(proj.getProduct());

            RelatedProduct dto = RelatedProduct.builder()
                .option(productOption)
                .product(product)
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
}