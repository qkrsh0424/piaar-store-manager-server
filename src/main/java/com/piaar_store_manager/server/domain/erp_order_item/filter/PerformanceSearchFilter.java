package com.piaar_store_manager.server.domain.erp_order_item.filter;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PerformanceSearchFilter {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer dayIndex;    
    private String salesYn;
    private Integer utcHourDifference;

    private List<String> salesChannels;
    private List<String> categoryNames;
}
