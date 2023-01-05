package com.piaar_store_manager.server.domain.sales_performance.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.sales_performance.filter.ChannelPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.DashboardPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.SalesPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.repository.SalesPerformanceRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesPerformanceService {
    private final SalesPerformanceRepositoryCustom salesPerformanceRepositoryCustom;

    public List<SalesPerformanceProjection> qSearchDashBoardByParams(DashboardPerformanceSearchFilter filter) {
        return salesPerformanceRepositoryCustom.qSearchDashBoardByParams(filter);
    }

    public List<SalesPerformanceProjection> qSearchSalesPerformanceByParams(SalesPerformanceSearchFilter filter) {
        return salesPerformanceRepositoryCustom.qSearchSalesPerformance(filter);
    }

    public List<SalesChannelPerformanceProjection.Performance> qSearchChannelSalesPerformanceByFilter(ChannelPerformanceSearchFilter filter) {
        return salesPerformanceRepositoryCustom.qSearchSalesPerformanceByChannel(filter);
    }

    public List<SalesCategoryPerformanceProjection.Performance> qSearchCategorySalesPerformanceByParams(SalesPerformanceSearchFilter filter , List<String> categoryName) {
        return salesPerformanceRepositoryCustom.qSearchSalesPerformanceByCategory(filter, categoryName);
    }

    // public List<SalesCategoryPerformanceProjection.ProductPerformance> qSearchCategoryAndProductSalesPerformanceByParams(Map<String, Object> params, List<String> categoryName) {
    //     return salesPerformanceRepositoryCustom.qSearchSalesProductPerformanceByCategory(params, categoryName);
    // }

    public List<SalesCategoryPerformanceProjection.ProductPerformance> qSearchCategoryAndProductSalesPerformanceByParams(SalesPerformanceSearchFilter filter, List<String> categoryName) {
        return salesPerformanceRepositoryCustom.qSearchSalesProductPerformanceByCategory(filter, categoryName);
    }
}
