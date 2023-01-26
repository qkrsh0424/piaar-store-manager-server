package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.filter.ChannelPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.DashboardPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.ProductPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.SalesPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesProductPerformanceProjection;

@Repository
public interface SalesPerformanceRepositoryCustom {
    List<SalesPerformanceProjection> qSearchDashBoardByParams(DashboardPerformanceSearchFilter filter);
    List<SalesPerformanceProjection> qSearchSalesPerformance(SalesPerformanceSearchFilter filter);
    List<SalesChannelPerformanceProjection.Performance> qSearchSalesPerformanceByChannel(ChannelPerformanceSearchFilter filter);
    List<SalesChannelPerformanceProjection> qSearchProductOptionSalesPerformanceByChannel(ChannelPerformanceSearchFilter filter);
    List<SalesCategoryPerformanceProjection.Performance> qSearchSalesPerformanceByCategory(SalesPerformanceSearchFilter filter, List<String> categoryName);
    List<SalesCategoryPerformanceProjection.ProductPerformance> qSearchSalesProductPerformanceByCategory(SalesPerformanceSearchFilter filter, List<String> categoryName);
    List<SalesProductPerformanceProjection.Performance> qSearchSalesPerformanceByProductOption(ProductPerformanceSearchFilter filter);
    List<SalesPerformanceProjection> qSearchSalesPerformanceByProduct(ProductPerformanceSearchFilter filter);
    List<SalesProductPerformanceProjection.BestProductPerformance> qSearchBestProductPerformance(ProductPerformanceSearchFilter filter);
    List<SalesProductPerformanceProjection.BestProductPerformance> qSearchCategoryBestProductPerformance(ProductPerformanceSearchFilter filter);
    List<SalesProductPerformanceProjection.BestOptionPerformance> qSearchBestProductOptionPerformance(ProductPerformanceSearchFilter filter);
    List<SalesProductPerformanceProjection.BestOptionPerformance> qSearchCategoryBestProductOptionPerformance(ProductPerformanceSearchFilter filter);
}
