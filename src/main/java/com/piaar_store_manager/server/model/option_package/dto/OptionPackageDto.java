package com.piaar_store_manager.server.model.option_package.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Table;

import com.piaar_store_manager.server.model.option_package.entity.OptionPackageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Table(name = "option_package")
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionPackageDto {
    private UUID id;
    private Integer packageUnit;
    private String originOptionCode;
    private Integer originOptionCid;
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

    public static OptionPackageDto toDto(OptionPackageEntity entity) {
        OptionPackageDto dto = OptionPackageDto.builder()
            .id(entity.getId())
            .packageUnit(entity.getPackageUnit())
            .originOptionCode(entity.getOriginOptionCode())
            .originOptionCid(entity.getOriginOptionCid())
            .originOptionId(entity.getOriginOptionId())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .updatedBy(entity.getUpdatedBy())
            .parentOptionId(entity.getParentOptionId())
            .build();

        return dto;
    }
}
