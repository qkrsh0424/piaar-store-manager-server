package com.piaar_store_manager.server.domain.excel_translator_header.dto;

import java.util.UUID;

import com.piaar_store_manager.server.domain.excel_translator_header.entity.ExcelTranslatorHeaderEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter @Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExcelTranslatorHeaderGetDto {
    private Integer cid;
    private UUID id;
    private String uploadHeaderTitle;
    private String downloadHeaderTitle;
    private ExcelTranslatorUploadHeaderDetailDto uploadHeaderDetail;
    private ExcelTranslatorDownloadHeaderDetailDto downloadHeaderDetail;
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
