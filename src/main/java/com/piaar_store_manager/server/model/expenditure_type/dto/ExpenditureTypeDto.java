package com.piaar_store_manager.server.model.expenditure_type.dto;

import lombok.Data;

@Data
public class ExpenditureTypeDto {
    private Integer expenditureTypeId;
    private String expenditureType;
    private Long sumValue;
    private String expenditureTypeDesc;
}
