package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection.Dashboard;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SalesPerformanceJdbcImpl implements SalesPerformanceCustomJdbc {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<SalesPerformanceProjection.Dashboard> jdbcSearchDashboard(Map<String, Object> params) {
        List<SalesPerformanceProjection.Dashboard> projs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        List<String> searchParams = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        StringBuilder withinDateRange = new StringBuilder();
        List<String> searchWithinDateRange = new ArrayList<>();

        String date = params.get("date") != null ? params.get("date").toString() : null;

        if(date == null) {
            return null;
        }

        List<String> dateList = Arrays.asList(date.split(","));
        List<LocalDateTime> dateValues = dateList.stream().map(r -> LocalDateTime.parse(r, formatter)).collect(Collectors.toList());

        // TODO :: 초기화 해줘야함
        List<Dashboard> resultProjs = this.getDashboardInitProjs(dateValues);
        sql.append("SELECT (CASE ");
        
        for(int i = 0; i < dateValues.size(); i++) {
            String startDate = dateValues.get(i).toString();
            String endDate = dateValues.get(i).plusDays(1).minusMinutes(1).toString();

            sql.append("WHEN item.channel_order_date BETWEEN ? AND ? THEN ? ");
            searchParams.add(startDate);
            searchParams.add(endDate);
            searchParams.add(startDate);

            // where 절에서 날짜 검색을 위한 조건절 추가
            withinDateRange.append("item.channel_order_date BETWEEN ? AND ? ");
            if(i != dateValues.size()-1) {
                withinDateRange.append("OR ");
            }
            searchWithinDateRange.add(startDate);
            searchWithinDateRange.add(endDate);
        }
        sql.append("END) AS datetime, COUNT(item.cid) AS orderRegistration, " + 
            "SUM(item.price + item.delivery_charge) AS orderPayAmount, " + 
            "COUNT(CASE WHEN item.sales_yn='y' THEN item.cid ELSE null END) AS salesRegistration, " + 
            "SUM(CASE WHEN item.sales_yn='y' THEN item.price + item.delivery_charge ELSE 0 END) AS salesPayAmount " + 
            "FROM erp_order_item item " +
            "WHERE item.channel_order_date IS NOT NULL AND ("
        );

        sql.append(withinDateRange.toString());
        searchParams.addAll(searchWithinDateRange);
        sql.append(") GROUP BY datetime " + 
            "HAVING datetime IS NOT NULL " +
            "ORDER BY datetime asc "
        );

        Object[] objs = new Object[searchParams.size()];
        objs = searchParams.stream().toArray();
        projs = jdbcTemplate.query(sql.toString(), new SalesPerformanceProjection.Dashboard.Mapper(), objs);
        this.updateDashboardProjs(resultProjs, projs);
        return resultProjs;
    }

    private List<Dashboard> getDashboardInitProjs(List<LocalDateTime> dateValues) {
        List<Dashboard> projs = new ArrayList<>();
        for (int i = 0; i < dateValues.size(); i++) {
            Dashboard proj = Dashboard.builder()
                    .datetime(dateValues.get(i).toString())
                    .orderRegistration(0)
                    .orderPayAmount(0)
                    .salesRegistration(0)
                    .salesPayAmount(0)
                    .build();

            projs.add(proj);
        }

        return projs;
    }
    
    private void updateDashboardProjs(List<Dashboard> initProjs, List<Dashboard> dashboardProjs) {
        initProjs.forEach(r -> {
            dashboardProjs.forEach(r2 -> {
                if(r.getDatetime().equals(r2.getDatetime())) {
                    r.setOrderRegistration(r2.getOrderRegistration())
                     .setOrderPayAmount(r2.getOrderPayAmount())
                     .setSalesRegistration(r2.getSalesRegistration())
                     .setSalesPayAmount(r2.getSalesPayAmount());
                }
            });
        });
    }
}
