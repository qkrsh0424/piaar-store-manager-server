package com.piaar_store_manager.server.domain.sales_performance.filter;

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
public class ChannelPerformanceSearchFilter {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer utcHourDifference;
    private List<String> optionCodes;
}
