package com.piaar_store_manager.server.domain.erp_download_excel_header.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class DetailDto {
    private Integer cellNumber;
    private String originCellName;
    private String customCellName;
    private String matchedColumnName;
    private String mergeYn;
    private String fixedValue;
    private String splitter;
    private List<ViewDetailDto> viewDetails;
}
