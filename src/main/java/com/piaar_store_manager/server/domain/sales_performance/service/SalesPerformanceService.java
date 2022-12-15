package com.piaar_store_manager.server.domain.sales_performance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.repository.SalesPerformanceCustomJdbc;
import com.piaar_store_manager.server.domain.sales_performance.repository.SalesPerformanceRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesPerformanceService {
    private final SalesPerformanceRepositoryCustom salesPerformanceRepositoryCustom;
    private final SalesPerformanceCustomJdbc salesPerformanceCustomJdbc;

    public List<SalesPerformanceProjection.Dashboard> qSearchDashBoardByParams(Map<String, Object> params) {
        return salesPerformanceCustomJdbc.jdbcSearchDashboard(params);
    }
    
    public List<SalesPerformanceProjection.PayAmount> qSearchPayAmountByParams(Map<String, Object> params) {
        String dimension = params.get("dimension") != null ? params.get("dimension").toString() : "date";

        List<SalesPerformanceProjection.PayAmount> projs = new ArrayList<>();
        switch(dimension) {
            case "date":
                projs = salesPerformanceRepositoryCustom.qSearchDailyPayAmountByParams(params);
                break;
            case "week":
                projs = salesPerformanceRepositoryCustom.qSearchWeeklyPayAmountByParams(params);
                break;
            case "month":
                projs = salesPerformanceRepositoryCustom.qSearchMonthlyPayAmountByParams(params);
                break;
            default: break;
        }

        return projs;
    }

    public List<SalesPerformanceProjection.RegistrationAndUnit> qSearchRegistrationAndUnitByParams(Map<String, Object> params) {
        String dimension = params.get("dimension") != null ? params.get("dimension").toString() : "date";

        List<SalesPerformanceProjection.RegistrationAndUnit> projs = new ArrayList<>();
        switch(dimension) {
            case "date":
                projs = salesPerformanceRepositoryCustom.qSearchDailyRegistrationAndUnitByParams(params);
                break;
            case "week":
                projs = salesPerformanceRepositoryCustom.qSearchWeeklyRegistrationAndUnitByParams(params);
                break;
            case "month":
                projs = salesPerformanceRepositoryCustom.qSearchMonthlyRegistrationAndUnitByParams(params);
                break;
            default: break;
        }

        return projs;
    }

    public List<SalesPerformanceProjection.SalesPayAmount> qSearchSalesPayAmountByParams(Map<String, Object> params) {
        List<SalesPerformanceProjection.SalesPayAmount> projs = salesPerformanceRepositoryCustom.qSearchSalesPayAmountByParams(params);
        return projs;
    }

    public List<SalesPerformanceProjection.SummaryTable> qSearchSummaryTableByParams(Map<String, Object> params) {
        List<SalesPerformanceProjection.SummaryTable> projs = salesPerformanceRepositoryCustom.qSearchSummaryTableByParams(params);
        return projs;
    }
}
