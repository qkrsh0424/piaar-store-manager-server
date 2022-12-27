package com.piaar_store_manager.server.domain.sales_performance.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.repository.SalesPerformanceRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesPerformanceService {
    private final SalesPerformanceRepositoryCustom salesPerformanceRepositoryCustom;

    public List<SalesPerformanceProjection> qSearchDashBoardByParams(Map<String, Object> params) {
        return salesPerformanceRepositoryCustom.qSearchDashBoardByParams(params);
    }
    
    public List<SalesPerformanceProjection> qSearchSalesPerformanceByParams(Map<String, Object> params) {
        return salesPerformanceRepositoryCustom.qSearchSalesPerformance(params);
    }

    public List<SalesChannelPerformanceProjection> qSearchChannelSalesPerformanceByParams(Map<String, Object> params) {
        return salesPerformanceRepositoryCustom.qSearchSalesPerformanceByChannel(params);
    }

    public List<SalesCategoryPerformanceProjection> qSearchCategorySalesPerformanceByParams(Map<String, Object> params) {
        return salesPerformanceRepositoryCustom.qSearchSalesPerformanceByCategory(params);
    }
}
