package com.piaar_store_manager.server.domain.sales_performance.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesProductPerformanceProjection;

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
public class SalesProductPerformanceDto {
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

    public static SalesProductPerformanceDto toDto(SalesProductPerformanceProjection proj) {
        SalesProductPerformanceDto dto = SalesProductPerformanceDto.builder()
            .productCode(proj.getProductCode())
            .productDefaultName(proj.getProductDefaultName())
            .optionCode(proj.getOptionCode())
            .optionDefaultName(proj.getOptionDefaultName())
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
        private List<SalesProductPerformanceDto> performances;

        public static Performance toDto(SalesProductPerformanceProjection.Performance proj) {
            List<SalesProductPerformanceDto> performanceDtos = new ArrayList<>();

            if (proj.getPerformance() != null) {
                performanceDtos = proj.getPerformance().stream().map(SalesProductPerformanceDto::toDto).collect(Collectors.toList());
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
    public static class BestProductPerformance {
        private String productDefaultName;
        private Integer orderPayAmount;
        private Integer salesPayAmount;
        private Integer orderRegistration;
        private Integer salesRegistration;
        private Integer orderUnit;
        private Integer salesUnit;

        public static BestProductPerformance toDto(SalesProductPerformanceProjection.BestProductPerformance proj) {
            BestProductPerformance dto = BestProductPerformance.builder()
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
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BestOptionPerformance {
        private String productDefaultName;
        private String optionDefaultName;
        private Integer orderPayAmount;
        private Integer salesPayAmount;
        private Integer orderRegistration;
        private Integer salesRegistration;
        private Integer orderUnit;
        private Integer salesUnit;

        public static BestOptionPerformance toDto(SalesProductPerformanceProjection.BestOptionPerformance proj) {
            BestOptionPerformance dto = BestOptionPerformance.builder()
                .productDefaultName(proj.getProductDefaultName() == null ? "미지정" : proj.getProductDefaultName())
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
