package com.piaar_store_manager.server.domain.sales_performance.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto.Dashboard;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto.PayAmount;
import com.piaar_store_manager.server.domain.sales_performance.dto.SalesPerformanceDto.RegistrationAndUnit;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesPerformanceBusinessService {
    private final SalesPerformanceService salesPerformanceService;

    public List<SalesPerformanceDto.Dashboard> searchDashboard(Map<String, Object> params) {
        List<SalesPerformanceProjection.Dashboard> projs = salesPerformanceService.qSearchDashBoardByParams(params);
        List<SalesPerformanceDto.Dashboard> dtos = projs.stream().map(proj -> Dashboard.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesPerformanceDto.PayAmount> searchTotalPayAmount(Map<String, Object> params) {
        List<SalesPerformanceProjection.PayAmount> projs = salesPerformanceService.qSearchPayAmountByParams(params);
        List<SalesPerformanceDto.PayAmount> dtos = projs.stream().map(proj -> PayAmount.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }

    public List<SalesPerformanceDto.RegistrationAndUnit> searchTotalRegistrationAndUnit(Map<String, Object> params) {
        List<SalesPerformanceProjection.RegistrationAndUnit> projs = salesPerformanceService.qSearchRegistrationAndUnitByParams(params);
        List<SalesPerformanceDto.RegistrationAndUnit> dtos = projs.stream().map(proj -> RegistrationAndUnit.toDto(proj)).collect(Collectors.toList());
        return dtos;
    }
}
