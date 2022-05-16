package com.piaar_store_manager.server.domain.excel_translator_item.dto;

import java.util.UUID;

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
public class UploadExcelDataGetDto {
    private UUID id;
    private ExcelDataDetailDto uploadedData;
}
