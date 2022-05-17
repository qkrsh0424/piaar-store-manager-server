package com.piaar_store_manager.server.domain.expenditure_type.dto;

import com.piaar_store_manager.server.domain.expenditure_type.entity.ExpenditureTypeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenditureTypeDto {
    private Integer expenditureTypeId;
    private String expenditureType;

    @Setter
    private Long sumValue;
    private String expenditureTypeDesc;

    public static ExpenditureTypeDto toDto(ExpenditureTypeEntity entity) {
        ExpenditureTypeDto dto = ExpenditureTypeDto.builder()
            .expenditureTypeId(entity.getExpenditureTypeId())
            .expenditureType(entity.getExpenditureType())
            .expenditureTypeDesc(entity.getExpenditureTypeDesc())
            .build();

        return dto;
    }
}
