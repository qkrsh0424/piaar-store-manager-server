package com.piaar_store_manager.server.domain.sub_option_code.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.domain.sub_option_code.entity.SubOptionCodeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SubOptionCodeDto {
    private UUID id;
    private String subOptionCode;
    private String memo;
    private UUID productOptionId;
    private String productOptionCode;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private LocalDateTime updatedAt;

    public static SubOptionCodeDto toDto(SubOptionCodeEntity entity) {
        SubOptionCodeDto dto = SubOptionCodeDto.builder()
            .id(entity.getId())
            .subOptionCode(entity.getSubOptionCode())
            .memo(entity.getMemo())
            .productOptionId(entity.getProductOptionId())
            .productOptionCode(entity.getProductOptionCode())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .build();
        return dto;
    }
}
