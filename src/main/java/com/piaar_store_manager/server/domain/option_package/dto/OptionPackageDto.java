package com.piaar_store_manager.server.domain.option_package.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.piaar_store_manager.server.domain.option_package.entity.OptionPackageEntity;
import com.piaar_store_manager.server.domain.option_package.proj.OptionPackageProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Table(name = "option_package")
@Accessors(chain = true)
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OptionPackageDto {
    private UUID id;

    @NotNull
    @Positive(message = "'패키지옵션 수량'은 0보다 큰 값을 입력해주세요.")
    private Integer packageUnit;

    private UUID originOptionId;
    
    @Setter
    private LocalDateTime createdAt;

    @Setter
    private UUID createdBy;
    
    @Setter
    private LocalDateTime updatedAt;
    
    @Setter
    private UUID updatedBy;
    
    @Setter
    private UUID parentOptionId;

    /**
     * <b>Convert Method</b>
     * <p>
     * OptionPackageEntity => OptionPackageDto
     *
     * @param entity : OptionPackageEntity
     * @return OptionPackageDto
     */
    public static OptionPackageDto toDto(OptionPackageEntity entity) {
        OptionPackageDto dto = OptionPackageDto.builder()
            .id(entity.getId())
            .packageUnit(entity.getPackageUnit())
            .originOptionId(entity.getOriginOptionId())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .updatedBy(entity.getUpdatedBy())
            .parentOptionId(entity.getParentOptionId())
            .build();

        return dto;
    }

    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedOriginOption {
        // option package
        private UUID id;
        private Integer packageUnit;
        private UUID originOptionId;
        private LocalDateTime createdAt;
        private UUID createdBy;
        private LocalDateTime updatedAt;
        private UUID updatedBy;
        private UUID parentOptionId;

        // origin option
        private String originOptionCode;
        private String originOptionDefaultName;
        
        // product
        private String originProductDefaultName;

        public static RelatedOriginOption toDto(OptionPackageProjection.RelatedProductAndOption proj) {
            RelatedOriginOption dto = RelatedOriginOption.builder()
                .id(proj.getOptionPackage().getId())
                .packageUnit(proj.getOptionPackage().getPackageUnit())
                .originOptionId(proj.getOptionPackage().getOriginOptionId())
                .createdAt(proj.getOptionPackage().getCreatedAt())
                .createdBy(proj.getOptionPackage().getCreatedBy())
                .updatedAt(proj.getOptionPackage().getUpdatedAt())
                .updatedBy(proj.getOptionPackage().getUpdatedBy())
                .parentOptionId(proj.getOptionPackage().getParentOptionId())
                .originOptionCode(proj.getProductOption().getCode())
                .originOptionDefaultName(proj.getProductOption().getDefaultName())
                .originProductDefaultName(proj.getProduct().getDefaultName())
                .build();

            return dto;
        }
    }
}
