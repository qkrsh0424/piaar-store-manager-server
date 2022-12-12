package com.piaar_store_manager.server.domain.sales_performance.proj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

public class SalesPerformanceProjection {

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Dashboard {
        private String datetime;

        @Setter
        private Integer orderRegistration;
        @Setter
        private Integer orderPayAmount;
        @Setter
        private Integer salesRegistration;
        @Setter
        private Integer salesPayAmount;
    }

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class PayAmount {
        private String datetime;

        @Setter
        private Integer orderPayAmount;
        @Setter
        private Integer salesPayAmount;
    }

    @Getter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class RegistrationAndUnit {
        private String datetime;

        @Setter
        private Integer orderRegistration;
        @Setter
        private Integer orderUnit;
        @Setter
        private Integer salesRegistration;
        @Setter
        private Integer salesUnit;
    }
}
