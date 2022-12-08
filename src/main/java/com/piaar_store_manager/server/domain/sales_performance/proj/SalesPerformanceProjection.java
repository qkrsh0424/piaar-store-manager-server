package com.piaar_store_manager.server.domain.sales_performance.proj;

import lombok.Getter;
import lombok.ToString;

public class SalesPerformanceProjection {

    @Getter
    @ToString
    public static class Dashboard {
        private String datetime;

        private Integer orderRegistration;
        private Integer orderPayAmount;

        private Integer salesRegistration;
        private Integer salesPayAmount;
    }

    @Getter
    @ToString
    public static class PayAmount {
        private String datetime;

        private Integer orderPayAmount;
        private Integer salesPayAmount;
    }
}
