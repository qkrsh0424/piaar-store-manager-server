package com.piaar_store_manager.server.domain.sales_performance.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.sales_performance.dto.SalesChannelPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesChannelPerformanceDtoV2;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto.RegistrationAndUnit;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto.SalesPayAmount;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto.TotalSummary;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjectionV2;
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

    public List<SalesPerformanceDto> searchTotalSalesPerformance(Map<String, Object> params) {
        List<SalesPerformanceProjection> projs = salesPerformanceService.qSearchSalesPerformanceByParams(params);
        List<SalesPerformanceDto> dtos = projs.stream().map(SalesPerformanceDto::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesChannelPerformanceDtoV2> searchChannelSalesPerformance(Map<String, Object> params) {
        List<SalesChannelPerformanceProjectionV2> projs = salesPerformanceService.qSearchChannelSalesPerformanceByParams(params);
        List<SalesChannelPerformanceDtoV2> dtos = projs.stream().map(SalesChannelPerformanceDtoV2::toDto).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesChannelPerformanceDto.PayAmount> searchPayAmountByChannel(Map<String, Object> params) {
        List<SalesChannelPerformanceProjection.PayAmount> projs = salesPerformanceService.jdbcSearchSelectedSalesChannelPayAmountByParams(params);
        List<SalesChannelPerformanceDto.PayAmount> dtos = projs.stream().map(proj -> SalesChannelPerformanceDto.PayAmount.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesChannelPerformanceDto.RegistrationAndUnit> searchRegistrationAndUnitByChannel(Map<String, Object> params) {
        List<SalesChannelPerformanceProjection.RegistrationAndUnit> projs = salesPerformanceService.jdbcSearchSelectedSalesChannelRegistrationAndUnitByParams(params);
        List<SalesChannelPerformanceDto.RegistrationAndUnit> dtos = projs.stream().map(proj -> SalesChannelPerformanceDto.RegistrationAndUnit.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }
    
    public List<SalesChannelPerformanceDto.SalesPayAmount> searchPayAmountDayOfWeekByChannel(Map<String, Object> params) {
        List<SalesChannelPerformanceProjection.SalesPayAmount> projs = salesPerformanceService.jdbcSearchSelectedSalesChannelSalesPayAmountParams(params);
        List<SalesChannelPerformanceDto.SalesPayAmount> dtos = projs.stream().map(proj -> SalesChannelPerformanceDto.SalesPayAmount.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesChannelPerformanceDto.PayAmount> searchProductPayAmountByChannel(Map<String, Object> params) {
        List<SalesChannelPerformanceProjection.PayAmount> projs = salesPerformanceService.jdbcSearchSelectedSalesChannelProductPayAmountByParams(params);
        List<SalesChannelPerformanceDto.PayAmount> dtos = projs.stream().map(proj -> SalesChannelPerformanceDto.PayAmount.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }
}
