package com.piaar_store_manager.server.domain.erp_sales_header.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_sales_header.entity.ErpSalesHeaderEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Builder
@Getter
@ToString
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class ErpSalesHeaderDto {
    private Integer cid;
    @Setter
    private UUID id;

    @Setter
    private String headerTitle;

    private ErpSalesHeaderDetailDto headerDetail;

    @Setter
    private LocalDateTime createdAt;

    @Setter
    private UUID createdBy;

    @Setter
    private LocalDateTime updatedAt;

    public static ErpSalesHeaderDto toDto(ErpSalesHeaderEntity entity) {
        if(entity == null) return null;

        ErpSalesHeaderDto dto = ErpSalesHeaderDto.builder()
            .cid(entity.getCid())
            .id(entity.getId())
            .headerTitle(entity.getHeaderTitle())
            .headerDetail(entity.getHeaderDetail())
            .createdAt(entity.getCreatedAt())
            .createdBy(entity.getCreatedBy())
            .updatedAt(entity.getUpdatedAt())
            .build();

        return dto;
    }
}
