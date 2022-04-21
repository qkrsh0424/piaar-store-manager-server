package com.piaar_store_manager.server.model.excel_translator_header.dto;

import java.util.UUID;

import com.piaar_store_manager.server.model.excel_translator_header.entity.ExcelTranslatorHeaderEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExcelTranslatorHeaderGetDto {
    private Integer cid;
    private UUID id;
    private String uploadHeaderTitle;
    private String downloadHeaderTitle;
    private ExcelTranslatorUploadHeaderDetailDto uploadHeaderDetail = new ExcelTranslatorUploadHeaderDetailDto();
    private ExcelTranslatorDownloadHeaderDetailDto downloadHeaderDetail = new ExcelTranslatorDownloadHeaderDetailDto();
    private Integer rowStartNumber;

    public static ExcelTranslatorHeaderGetDto toDto(ExcelTranslatorHeaderEntity entity) {
        ExcelTranslatorHeaderGetDto dto = ExcelTranslatorHeaderGetDto.builder()
            .id(entity.getId())
            .uploadHeaderTitle(entity.getUploadHeaderTitle())
            .downloadHeaderTitle(entity.getDownloadHeaderTitle())
            .uploadHeaderDetail(entity.getUploadHeaderDetail())
            .downloadHeaderDetail(entity.getDownloadHeaderDetail())
            .rowStartNumber(entity.getRowStartNumber())
            .build();

        return dto;
    }
}
