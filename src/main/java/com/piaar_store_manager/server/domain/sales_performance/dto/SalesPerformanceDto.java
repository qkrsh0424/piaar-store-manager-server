package com.piaar_store_manager.server.domain.sales_performance.dto;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SalesPerformanceDto {
    private String datetime;
    private Integer orderRegistration;
    private Integer orderUnit;
    private Integer orderPayAmount;
    private Integer salesRegistration;
    private Integer salesUnit;
    private Integer salesPayAmount;

    public static SalesPerformanceDto toDto(SalesPerformanceProjection proj) {
        SalesPerformanceDto dto = SalesPerformanceDto.builder()
            .datetime(proj.getDatetime())
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
