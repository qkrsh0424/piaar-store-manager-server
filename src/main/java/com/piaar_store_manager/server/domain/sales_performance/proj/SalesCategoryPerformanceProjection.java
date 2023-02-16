package com.piaar_store_manager.server.domain.sales_performance.proj;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SalesCategoryPerformanceProjection {
    private String datetime;
    private String productCategoryName;
    private String productDefaultName;
    private Integer orderPayAmount;
    private Integer salesPayAmount;
    private Integer orderRegistration;
    private Integer salesRegistration;
    private Integer orderUnit;
    private Integer salesUnit;

    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PerformanceByDate {
        private String datetime;
        @Setter
        private List<SalesCategoryPerformanceProjection> performance;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PerformanceByProductCategoryName {
        private String productCategoryName;
        @Setter
        private List<SalesCategoryPerformanceProjection> performance;
    }
}
