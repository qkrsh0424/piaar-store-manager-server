package com.piaar_store_manager.server.domain.excel_translator_header.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter @Setter
@ToString
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExcelTranslatorUploadHeaderDetailDto {
    private List<DetailDto> details;

    /**
     * 업로드 헤더에 들어갈 detail 객체
     */
    @Getter @Setter
    @ToString
    @Accessors(chain = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailDto {
        private UUID id;
        private String headerName;
        private Integer cellNumber;
        private String cellType;
    }
}
