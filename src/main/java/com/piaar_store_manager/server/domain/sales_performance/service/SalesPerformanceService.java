package com.piaar_store_manager.server.domain.sales_performance.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjectionV2;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.repository.SalesChannelPerformanceCustomJdbc;
import com.piaar_store_manager.server.domain.sales_performance.repository.SalesPerformanceCustomJdbc;
import com.piaar_store_manager.server.domain.sales_performance.repository.SalesPerformanceRepositoryCustom;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesPerformanceService {
    private final SalesChannelPerformanceCustomJdbc salesChannelPerformanceCustomJdbc;
    private final SalesPerformanceRepositoryCustom salesPerformanceRepositoryCustom;

    public List<SalesPerformanceProjection> qSearchDashBoardByParams(Map<String, Object> params) {
        // return salesPerformanceCustomJdbc.jdbcSearchDashboard(params);
        return salesPerformanceRepositoryCustom.qSearchDashBoardByParams(params);
    }
    
    public List<SalesPerformanceProjection> qSearchSalesPerformanceByParams(Map<String, Object> params) {
        // return salesPerformanceCustomJdbc.jdbcSearchPayAmount(params);
        return salesPerformanceRepositoryCustom.qSearchSalesPerformance(params);
    }

    public List<SalesChannelPerformanceProjectionV2> qSearchChannelSalesPerformanceByParams(Map<String, Object> params) {
        // return salesChannelPerformanceCustomJdbc.jdbcSearchSalesChannelPayAmount(params);
        return salesPerformanceRepositoryCustom.qSearchSalesChannelPerformance(params);
    }

    public List<SalesChannelPerformanceProjection.PayAmount> jdbcSearchSelectedSalesChannelPayAmountByParams(Map<String, Object> params) {
        return salesChannelPerformanceCustomJdbc.jdbcSearchSelectedSalesChannelPayAmount(params);
    }

    public List<SalesChannelPerformanceProjection.RegistrationAndUnit> jdbcSearchSelectedSalesChannelRegistrationAndUnitByParams(Map<String, Object> params) {
        return salesChannelPerformanceCustomJdbc.jdbcSearchSelectedSalesChannelRegistrationAndUnit(params);
    }

    public List<SalesChannelPerformanceProjection.SalesPayAmount> jdbcSearchSelectedSalesChannelSalesPayAmountParams(Map<String, Object> params) {
        return salesChannelPerformanceCustomJdbc.jdbcSearchSelectedSalesChannelSalesPayAmountParams(params);
    }

    public List<SalesChannelPerformanceProjection.PayAmount> jdbcSearchSelectedSalesChannelProductPayAmountByParams(Map<String, Object> params) {
        return salesChannelPerformanceCustomJdbc.jdbcSearchSelectedSalesChannelProductPayAmount(params);
    }
}
