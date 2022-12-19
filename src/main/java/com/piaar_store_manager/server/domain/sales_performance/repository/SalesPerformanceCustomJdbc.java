package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;

@Repository
public interface SalesPerformanceCustomJdbc {
    List<SalesPerformanceProjection.Dashboard> jdbcSearchDashboard(Map<String, Object> params);
    List<SalesPerformanceProjection.PayAmount> jdbcSearchPayAmount(Map<String, Object> params);
    List<SalesPerformanceProjection.RegistrationAndUnit> jdbcSearchRegistrationAndUnit(Map<String, Object> params);
    List<SalesPerformanceProjection.SalesPayAmount> jdbcSearchSalesPayAmount(Map<String, Object> params);
    List<SalesPerformanceProjection.TotalSummary> jdbcSearchTotalSummary(Map<String, Object> params);
    List<SalesChannelPerformanceProjection.PayAmount> jdbcSearchSalesChannelPayAmount(Map<String, Object> params);
}
