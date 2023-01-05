package com.piaar_store_manager.server.domain.sales_performance.filter;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SalesPerformanceSearchFilter {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer utcHourDifference;
}
