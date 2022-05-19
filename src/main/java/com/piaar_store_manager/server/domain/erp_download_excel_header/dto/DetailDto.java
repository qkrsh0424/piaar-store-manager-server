package com.piaar_store_manager.server.domain.erp_download_excel_header.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class DetailDto {
    // private Integer cellNumber;
    // private String originCellName;
    // private String customCellName;
    // private String matchedColumnName;
    // private String mergeYn;
    // private String fixedValue;
    // private String splitter;
    // private List<ViewDetailDto> viewDetails;
    
    private UUID id;
    private String customCellName;
    private String fieldType;   // 일반, 고정값, 운송코드
    private String fixedValue;
    private String mergeYn;
    private String mergeSplitter;   // 합배송 데이터 구분자
    private String valueSplitter;   // viewDetails 구분자
    private List<ViewDetailDto> viewDetails;
}
