package com.piaar_store_manager.server.model.excel_translator_header.dto;

import java.util.List;

import org.hibernate.annotations.Type;

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
public class ExcelTranslatorUploadHeaderDetailDto {
    @Type(type = "jsonb")
    private List<UploadDetailDto> details;
}
