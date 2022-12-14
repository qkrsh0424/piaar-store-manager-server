package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;

@Repository
public interface SalesPerformanceRepositoryCustom {
    List<SalesPerformanceProjection.Dashboard> qSearchDashBoardByParams(Map<String, Object> params);

    // 총 매출액
    List<SalesPerformanceProjection.PayAmount> qSearchDailyPayAmountByParams(Map<String, Object> params);
    List<SalesPerformanceProjection.PayAmount> qSearchWeeklyPayAmountByParams(Map<String, Object> params);
    List<SalesPerformanceProjection.PayAmount> qSearchMonthlyPayAmountByParams(Map<String, Object> params);
    
    // 총 판매건 & 수량
    List<SalesPerformanceProjection.RegistrationAndUnit> qSearchDailyRegistrationAndUnitByParams(Map<String, Object> params);
    List<SalesPerformanceProjection.RegistrationAndUnit> qSearchWeeklyRegistrationAndUnitByParams(Map<String, Object> params);
    List<SalesPerformanceProjection.RegistrationAndUnit> qSearchMonthlyRegistrationAndUnitByParams(Map<String, Object> params);
    
    // 판매 총 매출액
    List<SalesPerformanceProjection.SalesPayAmount> qSearchSalesPayAmountByParams(Map<String, Object> params);

    // 판매 매출 BEST
    List<SalesPerformanceProjection.SummaryTable> qSearchSummaryTableByParams(Map<String, Object> params);
}
