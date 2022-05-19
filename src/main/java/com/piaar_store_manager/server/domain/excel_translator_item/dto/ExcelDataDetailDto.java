package com.piaar_store_manager.server.domain.excel_translator_item.dto;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExcelDataDetailDto {
    List<UploadedDetailDto> details;

    /**
     * 업로드된 자유형식의 엑셀 데이터
     */
    @Getter @Setter
    @ToString
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UploadedDetailDto {
        private UUID id;
        private Object colData;
        private String cellType;
    }
}
