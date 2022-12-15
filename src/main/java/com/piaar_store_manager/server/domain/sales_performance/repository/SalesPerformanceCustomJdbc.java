package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;

@Repository
public interface SalesPerformanceCustomJdbc {
    List<SalesPerformanceProjection.Dashboard> jdbcSearchDashboard(Map<String, Object> params);
}
