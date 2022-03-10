package com.piaar_store_manager.server.domain.sales_analysis.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.piaar_store_manager.server.domain.sales_analysis.proj.SalesAnalysisItemProj;
import com.piaar_store_manager.server.handler.DateHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesAnalysisItemDto {
    private String salesProdManagementName;
    private String salesOptionManagementName;
    private String salesOptionCode;
    private String salesProdImageUrl;
    private Integer naverSalesUnit;
    private Integer coupangSalesUnit;

    public static SalesAnalysisItemDto toDto(SalesAnalysisItemProj proj) {
        SalesAnalysisItemDto dto = SalesAnalysisItemDto.builder()
            .salesProdManagementName(proj.getProduct().getManagementName())
            .salesOptionManagementName(proj.getProductOption().getManagementName())
            .salesOptionCode(proj.getProductOption().getCode())
            .salesProdImageUrl(proj.getProduct().getImageUrl())
            .naverSalesUnit(proj.getDeliveryReadyNaverSalesUnit())
            .coupangSalesUnit(proj.getDeliveryReadyCoupangSalesUnit())
            .build();

        return dto;
    }
}
