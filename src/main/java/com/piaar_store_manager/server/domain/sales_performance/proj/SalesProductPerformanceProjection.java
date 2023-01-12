package com.piaar_store_manager.server.domain.sales_performance.proj;

import java.util.List;

import com.piaar_store_manager.server.domain.product.entity.ProductEntity;
import com.piaar_store_manager.server.domain.product_option.entity.ProductOptionEntity;

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
public class SalesProductPerformanceProjection {
    private String datetime;
    private String productCode;
    private String optionCode;
    private String productDefaultName;
    private String optionDefaultName;
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
    public static class Performance {
        private String datetime;
        @Setter
        private List<SalesProductPerformanceProjection> performance;
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
    }
}
