package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;

@Repository
public interface SalesPerformanceRepositoryCustom {
    List<SalesPerformanceProjection.Dashboard> qSearchDashBoardByParams(Map<String, Object> params);
    List<SalesPerformanceProjection.PayAmount> qSearchDailyPayAmountByParams(Map<String, Object> params);
    List<SalesPerformanceProjection.PayAmount> qSearchWeeklyPayAmountByParams(Map<String, Object> params);
    List<SalesPerformanceProjection.PayAmount> qSearchMonthlyPayAmountByParams(Map<String, Object> params);
}
