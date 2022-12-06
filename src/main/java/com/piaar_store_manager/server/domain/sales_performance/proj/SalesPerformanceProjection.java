package com.piaar_store_manager.server.domain.sales_performance.proj;

import lombok.Getter;
import lombok.ToString;

public class SalesPerformanceProjection {

    @Getter
    @ToString
    public static class Dashboard {
        private String datetime;

        private Long orderRegistration;
        private Integer orderPayAmount;

        private Long salesRegistration;
        private Integer salesPayAmount;
    }
}
