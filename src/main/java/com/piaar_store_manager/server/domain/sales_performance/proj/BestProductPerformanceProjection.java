package com.piaar_store_manager.server.domain.sales_performance.proj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BestProductPerformanceProjection {
    private String productDefaultName;
    private String productCode;
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
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RelatedProductOptionPerformance {
        private String productDefaultName;
        private String optionDefaultName;
        private String productCode;
        private String optionCode;
        private Integer orderPayAmount;
        private Integer salesPayAmount;
        private Integer orderRegistration;
        private Integer salesRegistration;
        private Integer orderUnit;
        private Integer salesUnit;
    }
}
