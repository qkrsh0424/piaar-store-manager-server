package com.piaar_store_manager.server.domain.sales_performance.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.product_category.entity.ProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_category.service.ProductCategoryService;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesCategoryPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesChannelPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.filter.ChannelPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.DashboardPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesPerformanceBusinessService {
    private final SalesPerformanceService salesPerformanceService;
    private final ProductCategoryService productCategoryService;

    public List<SalesPerformanceDto> searchDashboard(DashboardPerformanceSearchFilter filter) {
        List<SalesPerformanceProjection> projs = salesPerformanceService.qSearchDashBoardByParams(filter);
        List<SalesPerformanceDto> dtos = projs.stream().map(SalesPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesPerformanceDto> searchSalesPerformance(Map<String, Object> params) {
        List<SalesPerformanceProjection> projs = salesPerformanceService.qSearchSalesPerformanceByParams(params);
        List<SalesPerformanceDto> dtos = projs.stream().map(SalesPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesChannelPerformanceDto.Performance> searchSalesPerformanceByChannel(ChannelPerformanceSearchFilter filter) {
        List<SalesChannelPerformanceProjection.Performance> projs = salesPerformanceService.qSearchChannelSalesPerformanceByFilter(filter);
        List<SalesChannelPerformanceDto.Performance> dtos = projs.stream().map(SalesChannelPerformanceDto.Performance::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesCategoryPerformanceDto.Performance> searchSalesPerformanceByCategory(Map<String, Object> params) {
        List<ProductCategoryEntity> categoryEntities = productCategoryService.searchAll();
        List<String> categoryName = categoryEntities.stream().map(r -> r.getName()).collect(Collectors.toList());
        List<SalesCategoryPerformanceProjection.Performance> projs = salesPerformanceService.qSearchCategorySalesPerformanceByParams(params, categoryName);
        List<SalesCategoryPerformanceDto.Performance> dtos = projs.stream().map(SalesCategoryPerformanceDto.Performance::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesCategoryPerformanceDto.ProductPerformance> searchSalesProductPerformanceByCategory(Map<String, Object> params) {
        List<ProductCategoryEntity> categoryEntities = productCategoryService.searchAll();
        List<String> categoryName = categoryEntities.stream().map(r -> r.getName()).collect(Collectors.toList());
        List<SalesCategoryPerformanceProjection.ProductPerformance> projs = salesPerformanceService.qSearchCategoryAndProductSalesPerformanceByParams(params, categoryName);
        List<SalesCategoryPerformanceDto.ProductPerformance> dtos = projs.stream().map(SalesCategoryPerformanceDto.ProductPerformance::toDto).collect(Collectors.toList());
        return dtos;
    }
}
