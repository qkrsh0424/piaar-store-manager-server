package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.filter.DashboardPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.SalesPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.proj.BestProductPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesProductPerformanceProjection;

@Repository
public interface SalesPerformanceRepositoryCustom {
    List<SalesPerformanceProjection> qSearchDashBoardByParams(DashboardPerformanceSearchFilter filter);
    List<SalesPerformanceProjection> qSearchSalesPerformance(SalesPerformanceSearchFilter filter);
    List<SalesChannelPerformanceProjection.Performance> qSearchSalesPerformanceByChannel(SalesPerformanceSearchFilter filter);
    List<SalesChannelPerformanceProjection> qSearchProductOptionSalesPerformanceByChannel(SalesPerformanceSearchFilter filter);
    List<SalesCategoryPerformanceProjection.Performance> qSearchSalesPerformanceByCategory(SalesPerformanceSearchFilter filter, List<String> categoryName);
    List<SalesCategoryPerformanceProjection.ProductPerformance> qSearchSalesProductPerformanceByCategory(SalesPerformanceSearchFilter filter, List<String> categoryName);
    List<SalesProductPerformanceProjection.Performance> qSearchSalesPerformanceByProductOption(SalesPerformanceSearchFilter filter);
    List<SalesPerformanceProjection> qSearchSalesPerformanceByProduct(SalesPerformanceSearchFilter filter);
    Page<BestProductPerformanceProjection> qSearchBestProductPerformanceByPaging(SalesPerformanceSearchFilter filter, Pageable pageable);
    List<BestProductPerformanceProjection.RelatedProductOptionPerformance> qSearchProductOptionPerformance(SalesPerformanceSearchFilter filter);
}
