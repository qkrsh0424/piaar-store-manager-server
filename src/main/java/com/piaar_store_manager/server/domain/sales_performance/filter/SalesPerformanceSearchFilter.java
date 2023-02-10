package com.piaar_store_manager.server.domain.sales_performance.filter;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SalesPerformanceSearchFilter {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer utcHourDifference;
    
    private List<String> optionCodes;
    private List<String> productCodes;
    private List<String> salesChannels;
    private List<String> productCategoryNames;
    private String pageOrderByColumn;
}
