package com.piaar_store_manager.server.domain.excel_translator_header.dto;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Type;

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
public class ExcelTranslatorDownloadHeaderDetailDto {
    @Type(type = "jsonb")
    private List<DetailDto> details;

    /**
     * 다운로드 헤더에 들어갈 detail
     */
    @Getter @Setter
    @ToString
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailDto {
        private UUID id;
        private String headerName;
        private Integer targetCellNumber;
        private String fixedValue;
        private UUID uploadHeaderId;
    }
}
