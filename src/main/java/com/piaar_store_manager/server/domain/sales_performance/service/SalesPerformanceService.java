package com.piaar_store_manager.server.domain.sales_performance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.repository.SalesPerformanceCustomJdbc;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesPerformanceService {
    private final SalesPerformanceCustomJdbc salesPerformanceCustomJdbc;

    public List<SalesPerformanceProjection.Dashboard> jdbcSearchDashBoardByParams(Map<String, Object> params) {
        return salesPerformanceCustomJdbc.jdbcSearchDashboard(params);
    }
    
    public List<SalesPerformanceProjection.PayAmount> jdbcSearchPayAmountByParams(Map<String, Object> params) {
        return salesPerformanceCustomJdbc.jdbcSearchPayAmount(params);
    }

    public List<SalesPerformanceProjection.RegistrationAndUnit> jdbcSearchRegistrationAndUnitByParams(Map<String, Object> params) {
        return salesPerformanceCustomJdbc.jdbcSearchRegistrationAndUnit(params);
    }

    public List<SalesPerformanceProjection.SalesPayAmount> jdbcSearchSalesPayAmountByParams(Map<String, Object> params) {
        return salesPerformanceCustomJdbc.jdbcSearchSalesPayAmount(params);
    }

    public List<SalesPerformanceProjection.TotalSummary> jdbcSearchTotalSummaryByParams(Map<String, Object> params) {
        return salesPerformanceCustomJdbc.jdbcSearchTotalSummary(params);
    }
}
