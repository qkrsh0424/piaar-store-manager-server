package com.piaar_store_manager.server.domain.sales_analysis.dto;

import java.util.UUID;

import com.piaar_store_manager.server.domain.sales_analysis.proj.SalesAnalysisItemProj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@Accessors(chain=true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesAnalysisItemDto {
    private String salesProdDefaultName;
    private String salesOptionDefaultName;
    private String salesOptionCode;
    private String salesProdImageUrl;
    private Integer erpSalesUnit;
    
    private String categoryName;
    private Integer salesOptionPrice;   // DB에서 가져오는 값이 아닌 직접 구하는 값
    private UUID salesProductId;

    public static SalesAnalysisItemDto toDto(SalesAnalysisItemProj proj) {
        SalesAnalysisItemDto dto = SalesAnalysisItemDto.builder()
            .salesProdDefaultName(proj.getProduct().getDefaultName())
            .salesOptionDefaultName(proj.getProductOption().getDefaultName())
            .salesOptionCode(proj.getProductOption().getCode())
            .salesProdImageUrl(proj.getProduct().getImageUrl())
            .erpSalesUnit(proj.getErpSalesUnit())
            .salesOptionPrice(proj.getProductOption().getSalesPrice())
            .categoryName(proj.getProductCategory().getName())
            .salesProductId(proj.getProduct().getId())
            .build();

        return dto;
    }
}
