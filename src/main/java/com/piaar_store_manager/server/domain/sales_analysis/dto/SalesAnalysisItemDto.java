package com.piaar_store_manager.server.domain.sales_analysis.dto;

import java.util.UUID;

import com.piaar_store_manager.server.domain.sales_analysis.proj.SalesAnalysisItemProj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
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
    
    private String categoryName;
    @Setter
    private Integer totalSalesUnit;
    private Integer salesOptionPrice;   // DB에서 가져오는 값이 아닌 직접 구하는 값
    private UUID salesProductId;

    public static SalesAnalysisItemDto toDto(SalesAnalysisItemProj proj) {
        SalesAnalysisItemDto dto = SalesAnalysisItemDto.builder()
            .salesProdManagementName(proj.getProduct().getManagementName())
            .salesOptionManagementName(proj.getProductOption().getManagementName())
            .salesOptionCode(proj.getProductOption().getCode())
            .salesProdImageUrl(proj.getProduct().getImageUrl())
            .naverSalesUnit(proj.getDeliveryReadyNaverSalesUnit())
            .coupangSalesUnit(proj.getDeliveryReadyCoupangSalesUnit())
            .salesOptionPrice(proj.getProductOption().getSalesPrice())
            .categoryName(proj.getProductCategory().getName())
            .salesProductId(proj.getProduct().getId())
            .build();

        return dto;
    }
}
