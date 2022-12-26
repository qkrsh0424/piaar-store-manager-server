package com.piaar_store_manager.server.domain.sales_performance.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjectionV2;

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
public class SalesChannelPerformanceDtoV2 {
    private String datetime;
    private List<Performance> performances;

    public static SalesChannelPerformanceDtoV2 toDto(SalesChannelPerformanceProjectionV2 proj) {
        List<Performance> performanceDtos = proj.getPerformance().stream().map(r -> Performance.toDto(r)).collect(Collectors.toList());
        
        SalesChannelPerformanceDtoV2 dto = SalesChannelPerformanceDtoV2.builder()
            .datetime(proj.getDatetime())
            .performances(performanceDtos)
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
        private String salesChannel;
        private Integer orderPayAmount;
        private Integer salesPayAmount;
        private Integer orderRegistration;
        private Integer salesRegistration;
        private Integer orderUnit;
        private Integer salesUnit;

        public static Performance toDto(SalesChannelPerformanceProjectionV2.Performance proj) {
            Performance dto = Performance.builder()
                .salesChannel(proj.getSalesChannel())
                .orderRegistration(proj.getOrderRegistration())
                .orderUnit(proj.getOrderUnit())
                .orderPayAmount(proj.getOrderPayAmount())
                .salesRegistration(proj.getSalesRegistration())
                .salesUnit(proj.getSalesUnit())
                .salesPayAmount(proj.getSalesPayAmount())
                .build();
                
            return dto;
        }
    }
}
