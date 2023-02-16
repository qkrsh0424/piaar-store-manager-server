package com.piaar_store_manager.server.domain.sales_performance.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;

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
public class SalesChannelPerformanceDto {
    private String salesChannel;
    private String optionCode;  // 상품 상세 - 옵션별 판매스토어 매출 분석을 위해 추가 
    private Integer orderPayAmount;
    private Integer salesPayAmount;
    private Integer orderRegistration;
    private Integer salesRegistration;
    private Integer orderUnit;
    private Integer salesUnit;

    public static SalesChannelPerformanceDto toDto(SalesChannelPerformanceProjection proj) {
        SalesChannelPerformanceDto dto = SalesChannelPerformanceDto.builder()
            .salesChannel(proj.getSalesChannel())
            .optionCode(proj.getOptionCode() == null ? "미지정" : proj.getOptionCode())
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
    public static class PerformanceByDate {
        private String datetime;
        private List<SalesChannelPerformanceDto> performances;

        public static PerformanceByDate toDto(SalesChannelPerformanceProjection.PerformanceByDate proj) {
            List<SalesChannelPerformanceDto> performanceDtos = new ArrayList<>();

            if (proj.getPerformance() != null) {
                performanceDtos = proj.getPerformance().stream().map(SalesChannelPerformanceDto::toDto).collect(Collectors.toList());
            }

            PerformanceByDate dto = PerformanceByDate.builder()
                    .datetime(proj.getDatetime())
                    .performances(performanceDtos)
                    .build();

            return dto;
        }
    }
}
