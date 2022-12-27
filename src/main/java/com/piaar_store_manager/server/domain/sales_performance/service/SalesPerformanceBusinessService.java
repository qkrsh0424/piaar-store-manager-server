package com.piaar_store_manager.server.domain.sales_performance.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.sales_performance.dto.SalesCategoryPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesChannelPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesPerformanceBusinessService {
    private final SalesPerformanceService salesPerformanceService;

    public List<SalesPerformanceDto> searchDashboard(Map<String, Object> params) {
        List<SalesPerformanceProjection> projs = salesPerformanceService.qSearchDashBoardByParams(params);
        List<SalesPerformanceDto> dtos = projs.stream().map(SalesPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesPerformanceDto> searchSalesPerformance(Map<String, Object> params) {
        List<SalesPerformanceProjection> projs = salesPerformanceService.qSearchSalesPerformanceByParams(params);
        List<SalesPerformanceDto> dtos = projs.stream().map(SalesPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesChannelPerformanceDto> searchSalesPerformanceByChannel(Map<String, Object> params) {
        List<SalesChannelPerformanceProjection> projs = salesPerformanceService.qSearchChannelSalesPerformanceByParams(params);
        List<SalesChannelPerformanceDto> dtos = projs.stream().map(SalesChannelPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesCategoryPerformanceDto> searchSalesPerformanceByCategory(Map<String, Object> params) {
        List<SalesCategoryPerformanceProjection> projs = salesPerformanceService.qSearchCategorySalesPerformanceByParams(params);
        List<SalesCategoryPerformanceDto> dtos = projs.stream().map(SalesCategoryPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }
}
