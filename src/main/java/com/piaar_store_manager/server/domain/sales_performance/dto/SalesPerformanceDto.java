package com.piaar_store_manager.server.domain.sales_performance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

public class SalesPerformanceDto {

    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dashboard {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime datetime;

        private Integer orderRegistration;
        private Integer orderPayAmount;

        private Integer salesRegistration;
        private Integer salesPayAmount;

        public static Dashboard toDto(SalesPerformanceProjection.Dashboard proj) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Dashboard dto = Dashboard.builder()
                .datetime(proj.getDatetime() != null ? CustomDateUtils.toLocalDateStartTime(LocalDate.parse(proj.getDatetime(), formatter)) : null)
                .orderRegistration(proj.getOrderRegistration())
                .orderPayAmount(proj.getOrderPayAmount())
                .salesRegistration(proj.getSalesRegistration())
                .salesPayAmount(proj.getSalesPayAmount())
                .build();

            return dto;
        }
    }

    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayAmount {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime datetime;

        private Integer orderPayAmount;
        private Integer salesPayAmount;

        public static PayAmount toDto(SalesPerformanceProjection.PayAmount proj) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            PayAmount dto = PayAmount.builder()
                .datetime(proj.getDatetime() != null ? CustomDateUtils.toLocalDateStartTime(LocalDate.parse(proj.getDatetime(), formatter)) : null)
                .orderPayAmount(proj.getOrderPayAmount())
                .salesPayAmount(proj.getSalesPayAmount())
                .build();

            return dto;
        }
    }

    @Getter
    @ToString
    @Accessors(chain = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistrationAndUnit {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime datetime;

        private Integer orderRegistration;
        private Integer orderUnit;
        private Integer salesRegistration;
        private Integer salesUnit;

        public static RegistrationAndUnit toDto(SalesPerformanceProjection.RegistrationAndUnit proj) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            RegistrationAndUnit dto = RegistrationAndUnit.builder()
                .datetime(proj.getDatetime() != null ? CustomDateUtils.toLocalDateStartTime(LocalDate.parse(proj.getDatetime(), formatter)) : null)
                .orderRegistration(proj.getOrderRegistration())
                .orderUnit(proj.getOrderUnit())
                .salesRegistration(proj.getSalesRegistration())
                .salesUnit(proj.getSalesUnit())
                .build();

            return dto;
        }
    }
}