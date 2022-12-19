package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection.Dashboard;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection.PayAmount;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection.RegistrationAndUnit;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection.SalesPayAmount;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection.TotalSummary;
import com.piaar_store_manager.server.utils.CustomDateUtils;

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

    

    @Override
    public List<PayAmount> jdbcSearchPayAmount(Map<String, Object> params) {
        List<SalesPerformanceProjection.PayAmount> projs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        List<String> searchParams = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        if (params.get("startDate") == null || params.get("endDate") == null || params.get("dimension") == null) {
            return null;
        }
        LocalDateTime startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);
        String dimension = params.get("dimension").toString();
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        if(dimension.equals("week")) {
            dateDiff = endDate.compareTo(startDate) / 7;
        }else if(dimension.equals("month")) {
            dateDiff = endDate.getMonthValue() - startDate.getMonthValue();
        }

        // 일별 객체 초기화
        List<PayAmount> resultProjs = this.getPayAmountInitProjs(startDate, endDate, dimension);
        sql.append("SELECT (CASE ");

        for(int i = 0; i <= dateDiff; i++) {
            String searchStartDate = startDate.plusDays(i).toString();
            String searchEndDate = startDate.plusDays(i+1).minusMinutes(1).toString();

            if(dimension.equals("week")) {   
                if(dateDiff == 0) {
                    searchStartDate = startDate.toString();
                    searchEndDate = endDate.toString();
                }else {
                    if(i == 0) {
                        searchStartDate = startDate.toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfWeek(startDate.plusWeeks(1)).toString();
                    }else if(i == dateDiff) {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfWeek(startDate.plusWeeks(i)).toString();
                        searchEndDate = endDate.toString();
                    }else {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfWeek(startDate.plusWeeks(i)).toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfWeek(startDate.plusWeeks(i+1)).toString();
                    }
                }
            }else if (dimension.equals("month")) {
                if(dateDiff == 0) {
                    searchStartDate = startDate.toString();
                    searchEndDate = endDate.toString();
                }else {
                    if(i == 0) {
                        searchStartDate = startDate.toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfMonth(startDate).toString();
                    }else if(i == dateDiff) {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfMonth(startDate.plusMonths(i)).toString();
                        searchEndDate = endDate.toString();
                    }else {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfMonth(startDate.plusMonths(i)).toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfMonth(startDate.plusMonths(i)).toString();
                    }
                }
            }

            sql.append("WHEN item.channel_order_date BETWEEN ? AND ? THEN ? ");

            searchParams.add(searchStartDate);
            searchParams.add(searchEndDate);
            searchParams.add(searchStartDate);
        }

        sql.append("END) AS datetime, SUM(item.price + item.delivery_charge) AS orderPayAmount, " +
            "SUM(CASE WHEN item.sales_yn='y' THEN item.price + item.delivery_charge ELSE 0 END) AS salesPayAmount " + 
            "FROM erp_order_item item " + 
            "WHERE item.channel_order_date IS NOT NULL AND item.channel_order_date BETWEEN ? AND ? " +
            "GROUP BY datetime " +
            "HAVING datetime IS NOT NULL " + 
            "ORDER BY datetime asc"
        );
        searchParams.add(startDate.toString());
        searchParams.add(endDate.toString());

        Object[] objs = new Object[searchParams.size()];
        objs = searchParams.stream().toArray();
        projs = jdbcTemplate.query(sql.toString(), new SalesPerformanceProjection.PayAmount.Mapper(), objs);
        // 초기화 객체 업데이트
        this.updatePayAmountProjs(resultProjs, projs);
        return resultProjs;
    }

    @Override
    public List<RegistrationAndUnit> jdbcSearchRegistrationAndUnit(Map<String, Object> params) {
        List<SalesPerformanceProjection.RegistrationAndUnit> projs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        List<String> searchParams = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        if (params.get("startDate") == null || params.get("endDate") == null || params.get("dimension") == null) {
            return null;
        }
        LocalDateTime startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);
        String dimension = params.get("dimension").toString();
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        if(dimension.equals("week")) {
            dateDiff = endDate.compareTo(startDate) / 7;
        }else if(dimension.equals("month")) {
            dateDiff = endDate.getMonthValue() - startDate.getMonthValue();
        }

        // 일별 객체 초기화
        List<RegistrationAndUnit> resultProjs = this.getRegistrationAndUnitInitProjs(startDate, endDate, dimension);
        sql.append("SELECT (CASE ");

        for(int i = 0; i <= dateDiff; i++) {
            String searchStartDate = startDate.plusDays(i).toString();
            String searchEndDate = startDate.plusDays(i+1).minusMinutes(1).toString();

            if(dimension.equals("week")) {   
                if(dateDiff == 0) {
                    searchStartDate = startDate.toString();
                    searchEndDate = endDate.toString();
                }else {
                    if(i == 0) {
                        searchStartDate = startDate.toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfWeek(startDate.plusWeeks(1)).toString();
                    }else if(i == dateDiff) {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfWeek(startDate.plusWeeks(i)).toString();
                        searchEndDate = endDate.toString();
                    }else {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfWeek(startDate.plusWeeks(i)).toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfWeek(startDate.plusWeeks(i+1)).toString();
                    }
                }
            }else if (dimension.equals("month")) {
                if(dateDiff == 0) {
                    searchStartDate = startDate.toString();
                    searchEndDate = endDate.toString();
                }else {
                    if(i == 0) {
                        searchStartDate = startDate.toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfMonth(startDate).toString();
                    }else if(i == dateDiff) {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfMonth(startDate.plusMonths(i)).toString();
                        searchEndDate = endDate.toString();
                    }else {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfMonth(startDate.plusMonths(i)).toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfMonth(startDate.plusMonths(i)).toString();
                    }
                }
            }

            sql.append("WHEN item.channel_order_date BETWEEN ? AND ? THEN ? ");

            searchParams.add(searchStartDate);
            searchParams.add(searchEndDate);
            searchParams.add(searchStartDate);
        }

        sql.append("END) AS datetime, COUNT(item.cid) AS orderRegistration, " +
            "SUM(item.unit) AS orderUnit, " +
            "COUNT(CASE WHEN item.sales_yn='y' THEN item.cid ELSE null END) AS salesRegistration, " + 
            "SUM(CASE WHEN item.sales_yn='y' THEN item.unit ELSE 0 END) AS salesUnit " +
            "FROM erp_order_item item " + 
            "WHERE item.channel_order_date IS NOT NULL AND item.channel_order_date BETWEEN ? AND ? " +
            "GROUP BY datetime " +
            "HAVING datetime IS NOT NULL " + 
            "ORDER BY datetime asc"
        );
        searchParams.add(startDate.toString());
        searchParams.add(endDate.toString());

        Object[] objs = new Object[searchParams.size()];
        objs = searchParams.stream().toArray();
        projs = jdbcTemplate.query(sql.toString(), new SalesPerformanceProjection.RegistrationAndUnit.Mapper(), objs);
        // 초기화 객체 업데이트
        this.updateRegistrationAndUnitProjs(resultProjs, projs);
        return resultProjs;
    }

    // dimension 설정 x
    @Override
    public List<SalesPayAmount> jdbcSearchSalesPayAmount(Map<String, Object> params) {
        List<SalesPerformanceProjection.SalesPayAmount> projs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        List<String> searchParams = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        if (params.get("startDate") == null || params.get("endDate") == null) {
            return null;
        }
        LocalDateTime startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        // 일별 객체 초기화
        List<SalesPayAmount> resultProjs = this.getSalesPayAmountInitProjs(startDate, endDate);
        sql.append("SELECT (CASE ");

        for(int i = 0; i <= dateDiff; i++) {
            String searchStartDate = startDate.plusDays(i).toString();
            String searchEndDate = startDate.plusDays(i+1).minusMinutes(1).toString();

            sql.append("WHEN item.channel_order_date BETWEEN ? AND ? THEN ? ");

            searchParams.add(searchStartDate);
            searchParams.add(searchEndDate);
            searchParams.add(searchStartDate);
        }

        sql.append("END) AS datetime, " +
            "SUM(CASE WHEN item.sales_yn='y' THEN item.price + item.delivery_charge ELSE 0 END) AS salesPayAmount " + 
            "FROM erp_order_item item " + 
            "WHERE item.channel_order_date IS NOT NULL AND item.channel_order_date BETWEEN ? AND ? " +
            "GROUP BY datetime " +
            "HAVING datetime IS NOT NULL " + 
            "ORDER BY datetime asc"
        );
        searchParams.add(startDate.toString());
        searchParams.add(endDate.toString());

        Object[] objs = new Object[searchParams.size()];
        objs = searchParams.stream().toArray();
        projs = jdbcTemplate.query(sql.toString(), new SalesPerformanceProjection.SalesPayAmount.Mapper(), objs);
        // 초기화 객체 업데이트
        this.updateSalesPayAmountProjs(resultProjs, projs);
        return resultProjs;
    }

    // dimension 설정 x
    @Override
    public List<TotalSummary> jdbcSearchTotalSummary(Map<String, Object> params) {
        List<SalesPerformanceProjection.TotalSummary> projs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        List<String> searchParams = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        if (params.get("startDate") == null || params.get("endDate") == null) {
            return null;
        }
        LocalDateTime startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        // 일별 객체 초기화
        List<TotalSummary> resultProjs = this.getTotalSummaryInitProjs(startDate, endDate);
        sql.append("SELECT (CASE ");

        for(int i = 0; i <= dateDiff; i++) {
            String searchStartDate = startDate.plusDays(i).toString();
            String searchEndDate = startDate.plusDays(i+1).minusMinutes(1).toString();

            sql.append("WHEN item.channel_order_date BETWEEN ? AND ? THEN ? ");

            searchParams.add(searchStartDate);
            searchParams.add(searchEndDate);
            searchParams.add(searchStartDate);
        }

        sql.append("END) AS datetime, COUNT(item.cid) AS orderRegistration, " +
            "SUM(item.unit) AS orderUnit, " +
            "SUM(item.price + item.delivery_charge) AS orderPayAmount, " + 
            "COUNT(CASE WHEN item.sales_yn='y' THEN item.cid ELSE null END) AS salesRegistration, " + 
            "SUM(CASE WHEN item.sales_yn='y' THEN item.unit ELSE 0 END) AS salesUnit, " +
            "SUM(CASE WHEN item.sales_yn='y' THEN item.price + item.delivery_charge ELSE 0 END) AS salesPayAmount " + 
            "FROM erp_order_item item " + 
            "WHERE item.channel_order_date IS NOT NULL AND item.channel_order_date BETWEEN ? AND ? " +
            "GROUP BY datetime " +
            "HAVING datetime IS NOT NULL " + 
            "ORDER BY datetime asc"
        );
        searchParams.add(startDate.toString());
        searchParams.add(endDate.toString());

        Object[] objs = new Object[searchParams.size()];
        objs = searchParams.stream().toArray();
        projs = jdbcTemplate.query(sql.toString(), new SalesPerformanceProjection.TotalSummary.Mapper(), objs);
        // 초기화 객체 업데이트
        this.updateTotalSummaryProjs(resultProjs, projs);
        return resultProjs;
    }
    
    /*
     * 판매채널별 매출액
     */
    @Override
    public List<SalesChannelPerformanceProjection.PayAmount> jdbcSearchSalesChannelPayAmount(Map<String, Object> params) {
        List<SalesChannelPerformanceProjection.PayAmount> projs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        List<String> searchParams = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        if (params.get("startDate") == null || params.get("endDate") == null || params.get("dimension") == null) {
            return null;
        }
        LocalDateTime startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);
        String dimension = params.get("dimension").toString();
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        if(dimension.equals("week")) {
            dateDiff = endDate.compareTo(startDate) / 7;
        }else if(dimension.equals("month")) {
            dateDiff = endDate.getMonthValue() - startDate.getMonthValue();
        }

        // 일별 객체 초기화
        List<SalesChannelPerformanceProjection.PayAmount> resultProjs = this.getSalesChannelPayAmountInitProjs(startDate, endDate, dimension);
        sql.append("SELECT (CASE ");

        for(int i = 0; i <= dateDiff; i++) {
            String searchStartDate = startDate.plusDays(i).toString();
            String searchEndDate = startDate.plusDays(i+1).minusMinutes(1).toString();

            if(dimension.equals("week")) {   
                if(dateDiff == 0) {
                    searchStartDate = startDate.toString();
                    searchEndDate = endDate.toString();
                }else {
                    if(i == 0) {
                        searchStartDate = startDate.toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfWeek(startDate.plusWeeks(1)).toString();
                    }else if(i == dateDiff) {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfWeek(startDate.plusWeeks(i)).toString();
                        searchEndDate = endDate.toString();
                    }else {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfWeek(startDate.plusWeeks(i)).toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfWeek(startDate.plusWeeks(i+1)).toString();
                    }
                }
            }else if (dimension.equals("month")) {
                if(dateDiff == 0) {
                    searchStartDate = startDate.toString();
                    searchEndDate = endDate.toString();
                }else {
                    if(i == 0) {
                        searchStartDate = startDate.toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfMonth(startDate).toString();
                    }else if(i == dateDiff) {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfMonth(startDate.plusMonths(i)).toString();
                        searchEndDate = endDate.toString();
                    }else {
                        searchStartDate = CustomDateUtils.getFirstDateTimeOfMonth(startDate.plusMonths(i)).toString();
                        searchEndDate = CustomDateUtils.getLastDateTimeOfMonth(startDate.plusMonths(i)).toString();
                    }
                }
            }

            sql.append("WHEN item.channel_order_date BETWEEN ? AND ? THEN ? ");

            searchParams.add(searchStartDate);
            searchParams.add(searchEndDate);
            searchParams.add(searchStartDate);
        }

        sql.append("END) AS datetime, " + 
            "(CASE WHEN item.sales_channel IS NULL OR item.sales_channel='' THEN '미지정' ELSE item.sales_channel END) AS salesChannel, " + 
            "SUM(item.price + item.delivery_charge) AS orderPayAmount, " +
            "SUM(CASE WHEN item.sales_yn='y' THEN item.price + item.delivery_charge ELSE 0 END) AS salesPayAmount " + 
            "FROM erp_order_item item " + 
            "WHERE item.channel_order_date IS NOT NULL AND item.channel_order_date BETWEEN ? AND ? " +
            "GROUP BY salesChannel, datetime " +
            "HAVING salesChannel IS NOT NULL AND datetime IS NOT NULL " + 
            "ORDER BY datetime asc"
        );
        searchParams.add(startDate.toString());
        searchParams.add(endDate.toString());

        Object[] objs = new Object[searchParams.size()];
        objs = searchParams.stream().toArray();
        projs = jdbcTemplate.query(sql.toString(), new SalesChannelPerformanceProjection.PayAmount.Mapper(), objs);
        // 초기화 객체 업데이트
        List<SalesChannelPerformanceProjection.PayAmount> updatedProjs = this.updateSalesChannelPayAmountProjs(resultProjs, projs);
        return updatedProjs;
    }

    /*
     * dashboard projs 세팅
     */
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

    /*
     * payamount projs 세팅
     */
    private List<PayAmount> getPayAmountInitProjs(LocalDateTime startDate, LocalDateTime endDate, String dimension) {
        List<PayAmount> projs = new ArrayList<>();

        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        for (long i = 0; i <= dateDiff; i++) {
            LocalDateTime datetime = startDate.plusDays(i);
            if (i != 0) {
                if (dimension.equals("week")) {
                    datetime = startDate.plusWeeks(i);
                    datetime = CustomDateUtils.getFirstDateTimeOfWeek(datetime);
                } else if (dimension.equals("month")) {
                    datetime = startDate.plusMonths(i);
                    datetime = CustomDateUtils.getFirstDateTimeOfMonth(datetime);
                }
            }

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }

            PayAmount proj = PayAmount.builder()
                    .datetime(datetime.toString())
                    .orderPayAmount(0)
                    .salesPayAmount(0)
                    .build();
            projs.add(proj);
        }
        return projs;
    }

    private void updatePayAmountProjs(List<PayAmount> initProjs, List<PayAmount> payAmountProjs) {
        initProjs.forEach(r -> {
            payAmountProjs.forEach(r2 -> {
                if(r.getDatetime().equals(r2.getDatetime())) {
                    r.setOrderPayAmount(r2.getOrderPayAmount())
                     .setSalesPayAmount(r2.getSalesPayAmount());
                }
            });
        });
    }

    /*
     * registration and unit projs 세팅
     */
    private List<RegistrationAndUnit> getRegistrationAndUnitInitProjs(LocalDateTime startDate, LocalDateTime endDate, String dimension) {
        List<RegistrationAndUnit> projs = new ArrayList<>();

        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        for (long i = 0; i <= dateDiff; i++) {
            LocalDateTime datetime = startDate.plusDays(i);
            if (i != 0) {
                if (dimension.equals("week")) {
                    datetime = startDate.plusWeeks(i);
                    datetime = CustomDateUtils.getFirstDateTimeOfWeek(datetime);
                } else if (dimension.equals("month")) {
                    datetime = startDate.plusMonths(i);
                    datetime = CustomDateUtils.getFirstDateTimeOfMonth(datetime);
                }
            }

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }

            RegistrationAndUnit proj = RegistrationAndUnit.builder()
                    .datetime(datetime.toString())
                    .orderRegistration(0)
                    .orderUnit(0)
                    .salesRegistration(0)
                    .salesUnit(0)
                    .build();
            projs.add(proj);
        }
        return projs;
    }

    private void updateRegistrationAndUnitProjs(List<RegistrationAndUnit> initProjs, List<RegistrationAndUnit> registrationAndUnitProjs) {
        initProjs.forEach(r -> {
            registrationAndUnitProjs.forEach(r2 -> {
                if(r.getDatetime().equals(r2.getDatetime())) {
                    r.setOrderRegistration(r2.getOrderRegistration())
                     .setOrderUnit(r2.getOrderUnit())
                     .setSalesRegistration(r2.getSalesRegistration())
                     .setSalesUnit(r2.getSalesUnit());
                }
            });
        });
    }

    /*
     * sales payamount projs 세팅
     * dimension 설정 x
     */
    private List<SalesPayAmount> getSalesPayAmountInitProjs(LocalDateTime startDate, LocalDateTime endDate) {
        List<SalesPayAmount> projs = new ArrayList<>();

        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        for (long i = 0; i <= dateDiff; i++) {
            LocalDateTime datetime = startDate.plusDays(i);

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }

            SalesPayAmount proj = SalesPayAmount.builder()
                    .datetime(datetime.toString())
                    .salesPayAmount(0)
                    .build();
            projs.add(proj);
        }
        return projs;
    }

    private void updateSalesPayAmountProjs(List<SalesPayAmount> initProjs, List<SalesPayAmount> salesPayAmountProjs) {
        initProjs.forEach(r -> {
            salesPayAmountProjs.forEach(r2 -> {
                if(r.getDatetime().equals(r2.getDatetime())) {
                     r.setSalesPayAmount(r2.getSalesPayAmount());
                }
            });
        });
    }

    /*
     * total summary projs 세팅
     * dimension 설정 x
     */
    private List<TotalSummary> getTotalSummaryInitProjs(LocalDateTime startDate, LocalDateTime endDate) {
        List<TotalSummary> projs = new ArrayList<>();

        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        for (long i = 0; i <= dateDiff; i++) {
            LocalDateTime datetime = startDate.plusDays(i);

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }

            TotalSummary proj = TotalSummary.builder()
                    .datetime(datetime.toString())
                    .orderRegistration(0)
                    .orderUnit(0)
                    .orderPayAmount(0)
                    .salesRegistration(0)
                    .salesUnit(0)
                    .salesPayAmount(0)
                    .build();
            projs.add(proj);
        }
        return projs;
    }

    private void updateTotalSummaryProjs(List<TotalSummary> initProjs, List<TotalSummary> totalSummaryProjs) {
        initProjs.forEach(r -> {
            totalSummaryProjs.forEach(r2 -> {
                if(r.getDatetime().equals(r2.getDatetime())) {
                     r.setOrderRegistration(r2.getOrderRegistration())
                        .setOrderUnit(r2.getOrderUnit())
                        .setOrderPayAmount(r2.getOrderPayAmount())
                        .setSalesRegistration(r2.getSalesRegistration())
                        .setSalesUnit(r2.getSalesUnit())
                        .setSalesPayAmount(r2.getSalesPayAmount());
                }
            });
        });
    }

    /*
     * sales channel payamount projs 세팅
     */
    private List<SalesChannelPerformanceProjection.PayAmount> getSalesChannelPayAmountInitProjs(LocalDateTime startDate, LocalDateTime endDate, String dimension) {
        List<SalesChannelPerformanceProjection.PayAmount> projs = new ArrayList<>();

        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        for (long i = 0; i <= dateDiff; i++) {
            LocalDateTime datetime = startDate.plusDays(i);
            if (i != 0) {
                if (dimension.equals("week")) {
                    datetime = startDate.plusWeeks(i);
                    datetime = CustomDateUtils.getFirstDateTimeOfWeek(datetime);
                } else if (dimension.equals("month")) {
                    datetime = startDate.plusMonths(i);
                    datetime = CustomDateUtils.getFirstDateTimeOfMonth(datetime);
                }
            }

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }
            
            SalesChannelPerformanceProjection.PayAmount proj = SalesChannelPerformanceProjection.PayAmount.builder()
                    .datetime(datetime.toString())
                    .build();
            projs.add(proj);
        }
        return projs;
    }

    // private List<SalesChannelPerformanceProjection.PayAmount> updateSalesChannelPayAmountProjs(List<SalesChannelPerformanceProjection.PayAmount> initProjs, List<SalesChannelPerformanceProjection.PayAmount> payAmountProjs) {
    //     List<SalesChannelPerformanceProjection.PayAmount> projs = new ArrayList<>();

    //     initProjs.forEach(r -> {
    //         payAmountProjs.forEach(r2 -> {
    //             if(r.getDatetime().equals(r2.getDatetime())) {
    //                 if(r.getSalesChannel().equals(r2.getSalesChannel())) {
    //                     r.setOrderPayAmount(r2.getOrderPayAmount())
    //                         .setSalesPayAmount(r2.getSalesPayAmount());
    //                 } else{
    //                     SalesChannelPerformanceProjection.PayAmount proj = SalesChannelPerformanceProjection.PayAmount.builder()
    //                         .datetime(r.getDatetime())
    //                         .salesChannel(r2.getSalesChannel())
    //                         .orderPayAmount(r2.getOrderPayAmount())
    //                         .salesPayAmount(r2.getSalesPayAmount())
    //                         .build();
    
    //                         projs.add(proj);
    //                 }
    //             }
    //         });
    //     });
    //     projs.addAll(initProjs);
    //     return projs;
    // }

    private List<SalesChannelPerformanceProjection.PayAmount> updateSalesChannelPayAmountProjs(List<SalesChannelPerformanceProjection.PayAmount> initProjs, List<SalesChannelPerformanceProjection.PayAmount> payAmountProjs) {
        List<SalesChannelPerformanceProjection.PayAmount> projs = new ArrayList<>();

        initProjs.forEach(r -> {
            List<SalesChannelPerformanceProjection> salesChannelProjs = new ArrayList<>();
            payAmountProjs.forEach(r2 -> {
                if(r.getDatetime().equals(r2.getDatetime())) {
                    SalesChannelPerformanceProjection salesChannelProj = SalesChannelPerformanceProjection.builder()
                        .salesChannel(r2.getPerformance().stream().findFirst().get().getSalesChannel())
                        .orderPayAmount(r2.getPerformance().stream().findFirst().get().getOrderPayAmount())
                        .salesPayAmount(r2.getPerformance().stream().findFirst().get().getSalesPayAmount())
                        .build();

                    salesChannelProjs.add(salesChannelProj);
                }
                r.setPerformance(salesChannelProjs);
            });
        });
        projs.addAll(initProjs);
        return projs;
    }
}
