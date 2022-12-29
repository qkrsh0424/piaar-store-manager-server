package com.piaar_store_manager.server.domain.sales_performance.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;

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
public class SalesCategoryPerformanceDto {
    private String productCategoryName;
    private String productName;
    private Integer orderPayAmount;
    private Integer salesPayAmount;
    private Integer orderRegistration;
    private Integer salesRegistration;
    private Integer orderUnit;
    private Integer salesUnit;

    public static SalesCategoryPerformanceDto toDto(SalesCategoryPerformanceProjection proj) {
        SalesCategoryPerformanceDto dto = SalesCategoryPerformanceDto.builder()
            .productCategoryName(proj.getProductCategoryName())
            .productName(proj.getProductName())
            .orderRegistration(proj.getOrderRegistration())
            .orderUnit(proj.getOrderUnit())
            .orderPayAmount(proj.getOrderPayAmount())
            .salesRegistration(proj.getSalesRegistration())
            .salesUnit(proj.getSalesUnit())
            .salesPayAmount(proj.getSalesPayAmount())
            .build();

        return dto;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Performance {
        private String datetime;
        private List<SalesCategoryPerformanceDto> performances;

        public static Performance toDto(SalesCategoryPerformanceProjection.Performance proj) {
            List<SalesCategoryPerformanceDto> performanceDtos = new ArrayList<>();

            if (proj.getPerformance() != null) {
                performanceDtos = proj.getPerformance().stream().map(SalesCategoryPerformanceDto::toDto).collect(Collectors.toList());
            }

            Performance dto = Performance.builder()
                    .datetime(proj.getDatetime())
                    .performances(performanceDtos)
                    .build();

            return dto;
        }
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductPerformance {
        private String productCategoryName;
        private List<SalesCategoryPerformanceDto> performances;

        public static ProductPerformance toDto(SalesCategoryPerformanceProjection.ProductPerformance proj) {
            List<SalesCategoryPerformanceDto> performanceDtos = new ArrayList<>();

            if (proj.getPerformance() != null) {
                performanceDtos = proj.getPerformance().stream().map(SalesCategoryPerformanceDto::toDto).collect(Collectors.toList());
            }

            ProductPerformance dto = ProductPerformance.builder()
                    .productCategoryName(proj.getProductCategoryName())
                    .performances(performanceDtos)
                    .build();

            return dto;
        }
    }
}
