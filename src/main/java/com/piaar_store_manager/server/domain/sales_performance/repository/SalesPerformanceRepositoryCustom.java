package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.filter.ChannelPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.DashboardPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;

@Repository
public interface SalesPerformanceRepositoryCustom {
    List<SalesPerformanceProjection> qSearchDashBoardByParams(DashboardPerformanceSearchFilter filter);
    List<SalesPerformanceProjection> qSearchSalesPerformance(Map<String, Object> params);
    List<SalesChannelPerformanceProjection.Performance> qSearchSalesPerformanceByChannel(ChannelPerformanceSearchFilter filter);
    List<SalesCategoryPerformanceProjection.Performance> qSearchSalesPerformanceByCategory(Map<String, Object> params, List<String> categoryName);
    List<SalesCategoryPerformanceProjection.ProductPerformance> qSearchSalesProductPerformanceByCategory(Map<String, Object> params, List<String> categoryName);
}
