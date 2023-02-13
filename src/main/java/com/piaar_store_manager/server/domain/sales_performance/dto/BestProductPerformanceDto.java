package com.piaar_store_manager.server.domain.sales_performance.dto;

import com.piaar_store_manager.server.domain.sales_performance.proj.BestProductPerformanceProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BestProductPerformanceDto {
    private String productCode;
    private String productDefaultName;
    private Integer orderPayAmount;
    private Integer salesPayAmount;
    private Integer orderRegistration;
    private Integer salesRegistration;
    private Integer orderUnit;
    private Integer salesUnit;

    public static BestProductPerformanceDto toDto(BestProductPerformanceProjection proj) {
        BestProductPerformanceDto dto = BestProductPerformanceDto.builder()
                .productCode(proj.getProductCode() == null ? "미지정" : proj.getProductCode().trim())
                .productDefaultName(proj.getProductDefaultName() == null ? "미지정" : proj.getProductDefaultName())
                .orderPayAmount(proj.getOrderPayAmount())
                .salesPayAmount(proj.getSalesPayAmount())
                .orderRegistration(proj.getOrderRegistration())
                .salesRegistration(proj.getSalesRegistration())
                .orderUnit(proj.getOrderUnit())
                .salesUnit(proj.getSalesUnit())
                .build();

        return dto;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RelatedProductOptionPerformance {
        private String productCode;
        private String productDefaultName;
        private String optionCode;
        private String optionDefaultName;
        private Integer orderPayAmount;
        private Integer salesPayAmount;
        private Integer orderRegistration;
        private Integer salesRegistration;
        private Integer orderUnit;
        private Integer salesUnit;

        public static RelatedProductOptionPerformance toDto(BestProductPerformanceProjection.RelatedProductOptionPerformance proj) {
            RelatedProductOptionPerformance dto = RelatedProductOptionPerformance.builder()
                .productCode(proj.getProductCode() == null ? "미지정" : proj.getProductCode().trim())
                .productDefaultName(proj.getProductDefaultName() == null ? "미지정" : proj.getProductDefaultName())
                .optionCode(proj.getOptionCode() == null ? "미지정" : proj.getOptionCode().trim())
                .optionDefaultName(proj.getOptionDefaultName() == null ? "미지정" : proj.getOptionDefaultName())
                .orderPayAmount(proj.getOrderPayAmount())
                .salesPayAmount(proj.getSalesPayAmount())
                .orderRegistration(proj.getOrderRegistration())
                .salesRegistration(proj.getSalesRegistration())
                .orderUnit(proj.getOrderUnit())
                .salesUnit(proj.getSalesUnit())
                .build();
            
            return dto;
        }
    }
}
