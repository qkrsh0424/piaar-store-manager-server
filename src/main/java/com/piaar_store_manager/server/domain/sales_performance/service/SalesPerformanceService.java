package com.piaar_store_manager.server.domain.sales_performance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.sales_performance.filter.ChannelPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.DashboardPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.ProductPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.SalesPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesProductPerformanceProjection;
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

    public List<SalesChannelPerformanceProjection> qSearchProductChannelSalesPerformanceByFilter(ChannelPerformanceSearchFilter filter) {
        return salesPerformanceRepositoryCustom.qSearchProductOptionSalesPerformanceByChannel(filter);
    }

    public List<SalesCategoryPerformanceProjection.Performance> qSearchCategorySalesPerformanceByParams(SalesPerformanceSearchFilter filter , List<String> categoryName) {
        return salesPerformanceRepositoryCustom.qSearchSalesPerformanceByCategory(filter, categoryName);
    }

    public List<SalesCategoryPerformanceProjection.ProductPerformance> qSearchCategoryAndProductSalesPerformanceByParams(SalesPerformanceSearchFilter filter, List<String> categoryName) {
        return salesPerformanceRepositoryCustom.qSearchSalesProductPerformanceByCategory(filter, categoryName);
    }

    public List<SalesProductPerformanceProjection.Performance> qSearchProductOptionSalesPerformanceByFilter(ProductPerformanceSearchFilter filter) {
        return salesPerformanceRepositoryCustom.qSearchSalesPerformanceByProductOption(filter);
    }

    public List<SalesPerformanceProjection> qSearchProductSalesPerformanceByFilter(ProductPerformanceSearchFilter filter) {
        return salesPerformanceRepositoryCustom.qSearchSalesPerformanceByProduct(filter);
    }

    public List<SalesProductPerformanceProjection.BestProductPerformance> qSearchBestProductPerformanceByFilter(ProductPerformanceSearchFilter filter) {
        return salesPerformanceRepositoryCustom.qSearchBestProductPerformance(filter);
    }

    public List<SalesProductPerformanceProjection.BestProductPerformance> qSearchCategoryBestProductPerformanceByFilter(ProductPerformanceSearchFilter filter) {
        return salesPerformanceRepositoryCustom.qSearchCategoryBestProductPerformance(filter);
    }
    
    public List<SalesProductPerformanceProjection.BestOptionPerformance> qSearchBestProductOptionPerformanceByFilter(ProductPerformanceSearchFilter filter) {
        return salesPerformanceRepositoryCustom.qSearchBestProductOptionPerformance(filter);
    }

    public List<SalesProductPerformanceProjection.BestOptionPerformance> qSearchCategoryBestProductOptionPerformanceByFilter(ProductPerformanceSearchFilter filter) {
        return salesPerformanceRepositoryCustom.qSearchCategoryBestProductOptionPerformance(filter);
    }
}
