package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;

@Repository
public interface SalesPerformanceRepositoryCustom {
    List<SalesPerformanceProjection> qSearchDashBoardByParams(Map<String, Object> params);
    List<SalesPerformanceProjection> qSearchSalesPerformance(Map<String, Object> params);
    List<SalesChannelPerformanceProjection> qSearchSalesPerformanceByChannel(Map<String, Object> params);
    List<SalesCategoryPerformanceProjection> qSearchSalesPerformanceByCategory(Map<String, Object> params);
}
