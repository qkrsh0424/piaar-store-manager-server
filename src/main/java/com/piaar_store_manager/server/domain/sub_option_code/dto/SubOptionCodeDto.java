package com.piaar_store_manager.server.domain.sub_option_code.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.piaar_store_manager.server.domain.product_option.dto.ProductOptionGetDto;
import com.piaar_store_manager.server.domain.sub_option_code.entity.SubOptionCodeEntity;
import com.piaar_store_manager.server.domain.sub_option_code.proj.SubOptionCodeProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SubOptionCodeDto {
    private UUID id;

    @Setter
    @NotBlank(message = "'대체옵션코드'를 입력해주세요.")
    @Size(max = 50, message = "'대체옵션코드'는 50자 이내로 입력해주세요.")
    private String subOptionCode;

    @Setter
    @Size(max = 50, message = "'대체옵션코드 메모'는 50자 이내로 입력해주세요.")
    private String memo;

    private UUID productOptionId;
    private LocalDateTime createdAt;
    private UUID createdBy;
    @Setter
    private LocalDateTime updatedAt;
    @Setter
    private UUID updatedBy;

    public static SubOptionCodeDto toDto(SubOptionCodeEntity entity) {
        SubOptionCodeDto dto = SubOptionCodeDto.builder()
            .id(entity.getId())
            .subOptionCode(entity.getSubOptionCode())
            .memo(entity.getMemo())
            .productOptionId(entity.getProductOptionId())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .updatedBy(entity.getUpdatedBy())
            .build();
        return dto;
    }

    public static void removeBlank(SubOptionCodeDto subOptionCodeDto) {
        if(subOptionCodeDto == null) return;

        subOptionCodeDto.setSubOptionCode(subOptionCodeDto.getSubOptionCode().strip())
            .setMemo(subOptionCodeDto.getMemo() != null ? subOptionCodeDto.getMemo().strip() : null);
    }

    @Getter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RelatedProductOption {
        SubOptionCodeDto subOptionCode;
        ProductOptionGetDto productOption;

        public static RelatedProductOption toDto(SubOptionCodeProjection.RelatedProductOption proj) {
            SubOptionCodeDto subOptionCodeDto = SubOptionCodeDto.toDto(proj.getSubOptionCode());
            ProductOptionGetDto productOptionDto = ProductOptionGetDto.toDto(proj.getProductOption());
            
            RelatedProductOption dto = RelatedProductOption.builder()
                .subOptionCode(subOptionCodeDto)
                .productOption(productOptionDto)
                .build();

            return dto;
        }
    }
}
