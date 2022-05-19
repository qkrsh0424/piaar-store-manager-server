package com.piaar_store_manager.server.model.excel_translator_header.dto;

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
public class UploadDetailDto {
    private UUID id;
    private String headerName;
    private Integer cellNumber;
    private String cellType;
}
