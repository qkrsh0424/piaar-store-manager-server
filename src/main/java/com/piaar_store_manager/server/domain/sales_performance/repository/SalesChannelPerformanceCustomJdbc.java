package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;

@Repository
public interface SalesChannelPerformanceCustomJdbc {
    List<SalesChannelPerformanceProjection.PayAmount> jdbcSearchSalesChannelPayAmount(Map<String, Object> params);  // client에서 사용하지 않는 메서드
    List<SalesChannelPerformanceProjection.PayAmount> jdbcSearchSelectedSalesChannelPayAmount(Map<String, Object> params);
    List<SalesChannelPerformanceProjection.RegistrationAndUnit> jdbcSearchSelectedSalesChannelRegistrationAndUnit(Map<String, Object> params);
    List<SalesChannelPerformanceProjection.SalesPayAmount> jdbcSearchSelectedSalesChannelSalesPayAmountParams(Map<String, Object> params);
    List<SalesChannelPerformanceProjection.PayAmount> jdbcSearchSelectedSalesChannelProductPayAmount(Map<String, Object> params);  // client에서 사용하지 않는 메서드
}
