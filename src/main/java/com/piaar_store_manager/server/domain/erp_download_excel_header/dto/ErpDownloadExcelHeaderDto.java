package com.piaar_store_manager.server.domain.erp_download_excel_header.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_download_excel_header.entity.ErpDownloadExcelHeaderEntity;

@Builder
@Getter
@ToString
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class ErpDownloadExcelHeaderDto {
    private Integer cid;
    private UUID id;
    private String title;
    private ErpDownloadExcelHeaderDetailDto headerDetail;

    @Setter
    private LocalDateTime createdAt;

    @Setter
    private UUID createdBy;

    @Setter
    private LocalDateTime updatedAt;

    public static ErpDownloadExcelHeaderDto toDto(ErpDownloadExcelHeaderEntity entity) {
        if(entity == null) return null;

        ErpDownloadExcelHeaderDto dto = ErpDownloadExcelHeaderDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .headerDetail(entity.getHeaderDetail())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .build();

        return dto;
    }
}
