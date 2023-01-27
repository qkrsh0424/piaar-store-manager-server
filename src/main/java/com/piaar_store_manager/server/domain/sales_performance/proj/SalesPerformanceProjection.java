package com.piaar_store_manager.server.domain.sales_performance.proj;

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
public class SalesPerformanceProjection {
    private String datetime;
    
    @Setter
    private Integer orderRegistration;
    @Setter
    private Integer salesRegistration;
    @Setter
    private Integer orderUnit;
    @Setter
    private Integer salesUnit;
    @Setter
    private Integer orderPayAmount;
    @Setter
    private Integer salesPayAmount;
}
