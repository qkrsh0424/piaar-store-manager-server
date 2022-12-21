package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.utils.CustomDateUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SalesChannelPerformanceJdbcImpl implements SalesChannelPerformanceCustomJdbc {
    private final JdbcTemplate jdbcTemplate;

    /*
     * 판매채널별 매출액
     * 20221220 사용하지 않는 메서드
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
            dateDiff = dateDiff / 7;
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
     * 선택된 판매스토어의 매출액
     */
    @Override
    public List<SalesChannelPerformanceProjection.PayAmount> jdbcSearchSelectedSalesChannelPayAmount(Map<String, Object> params) {
        List<SalesChannelPerformanceProjection.PayAmount> projs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        List<String> searchParams = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        if (params.get("startDate") == null || params.get("endDate") == null
            || params.get("dimension") == null || params.get("channel") == null) {
            return null;
        }

        LocalDateTime startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);
        String dimension = params.get("dimension").toString();
        List<String> channel = Arrays.asList(params.get("channel").toString().split(","));
        
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        if(dimension.equals("week")) {
            dateDiff = dateDiff / 7;
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

        sql.append(
            "END) AS datetime, " + 
            "(CASE WHEN item.sales_channel IS NULL OR item.sales_channel='' THEN '미지정' ELSE item.sales_channel END) AS salesChannel, " + 
            "SUM(item.price + item.delivery_charge) AS orderPayAmount, " +
            "SUM(CASE WHEN item.sales_yn='y' THEN item.price + item.delivery_charge ELSE 0 END) AS salesPayAmount " + 
            "FROM erp_order_item item " + 
            "WHERE ("
        );

        for(int i = 0; i < channel.size(); i++) {
            sql.append("item.sales_channel = ? ");
            if(i != channel.size()-1) {
                sql.append("OR ");
            }
            searchParams.add(channel.get(i));
        }
            
        sql.append(
            ") AND item.channel_order_date IS NOT NULL AND item.channel_order_date BETWEEN ? AND ? " +
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
     * 선택된 판매스토어의 주문, 판매건 & 수량
     */
    @Override
    public List<SalesChannelPerformanceProjection.RegistrationAndUnit> jdbcSearchSelectedSalesChannelRegistrationAndUnit(Map<String, Object> params) {
        List<SalesChannelPerformanceProjection.RegistrationAndUnit> projs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        List<String> searchParams = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        if (params.get("startDate") == null || params.get("endDate") == null
            || params.get("dimension") == null || params.get("channel") == null) {
            return null;
        }

        LocalDateTime startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);
        String dimension = params.get("dimension").toString();
        List<String> channel = Arrays.asList(params.get("channel").toString().split(","));
        
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        if(dimension.equals("week")) {
            dateDiff = dateDiff / 7;
        }else if(dimension.equals("month")) {
            dateDiff = endDate.getMonthValue() - startDate.getMonthValue();
        }

        // 일별 객체 초기화
        List<SalesChannelPerformanceProjection.RegistrationAndUnit> resultProjs = this.getSalesChannelRegistrationAndUnitInitProjs(startDate, endDate, dimension);
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

        sql.append(
            "END) AS datetime, " + 
            "(CASE WHEN item.sales_channel IS NULL OR item.sales_channel='' THEN '미지정' ELSE item.sales_channel END) AS salesChannel, " + 
            "COUNT(item.cid) AS orderRegistration, " +
            "SUM(item.unit) AS orderUnit, " +
            "COUNT(CASE WHEN item.sales_yn='y' THEN item.cid ELSE null END) AS salesRegistration, " + 
            "SUM(CASE WHEN item.sales_yn='y' THEN item.unit ELSE 0 END) AS salesUnit " +
            "FROM erp_order_item item " + 
            "WHERE ("
        );

        for(int i = 0; i < channel.size(); i++) {
            sql.append("item.sales_channel = ? ");
            if(i != channel.size()-1) {
                sql.append("OR ");
            }
            searchParams.add(channel.get(i));
        }
            
        sql.append(
            ") AND item.channel_order_date IS NOT NULL AND item.channel_order_date BETWEEN ? AND ? " +
            "GROUP BY salesChannel, datetime " +
            "HAVING salesChannel IS NOT NULL AND datetime IS NOT NULL " + 
            "ORDER BY datetime asc"
        );
        searchParams.add(startDate.toString());
        searchParams.add(endDate.toString());

        Object[] objs = new Object[searchParams.size()];
        objs = searchParams.stream().toArray();
        projs = jdbcTemplate.query(sql.toString(), new SalesChannelPerformanceProjection.RegistrationAndUnit.Mapper(), objs);
        // 초기화 객체 업데이트
        List<SalesChannelPerformanceProjection.RegistrationAndUnit> updatedProjs = this.updateSalesChannelRegistrationAndUnitProjs(resultProjs, projs);
        return updatedProjs;
    }

    /*
     * 선택된 판매스토어의 주문, 판매건 & 수량
     * dimension 설정 x
     */
    @Override
    public List<SalesChannelPerformanceProjection.SalesPayAmount> jdbcSearchSelectedSalesChannelSalesPayAmountParams(Map<String, Object> params) {
        List<SalesChannelPerformanceProjection.SalesPayAmount> projs = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        List<String> searchParams = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        if (params.get("startDate") == null || params.get("endDate") == null || params.get("channel") == null) {
            return null;
        }

        LocalDateTime startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);
        List<String> channel = Arrays.asList(params.get("channel").toString().split(","));
        
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        // 일별 객체 초기화
        List<SalesChannelPerformanceProjection.SalesPayAmount> resultProjs = this.getSalesChannelSalesPayAmountInitProjs(startDate, endDate);
        sql.append("SELECT (CASE ");

        for(int i = 0; i <= dateDiff; i++) {
            String searchStartDate = startDate.plusDays(i).toString();
            String searchEndDate = startDate.plusDays(i+1).minusMinutes(1).toString();

            sql.append("WHEN item.channel_order_date BETWEEN ? AND ? THEN ? ");

            searchParams.add(searchStartDate);
            searchParams.add(searchEndDate);
            searchParams.add(searchStartDate);
        }

        sql.append(
            "END) AS datetime, " + 
            "(CASE WHEN item.sales_channel IS NULL OR item.sales_channel='' THEN '미지정' ELSE item.sales_channel END) AS salesChannel, " + 
            "SUM(item.price + item.delivery_charge) AS orderPayAmount, " +
            "SUM(CASE WHEN item.sales_yn='y' THEN item.price + item.delivery_charge ELSE 0 END) AS salesPayAmount " + 
            "FROM erp_order_item item " + 
            "WHERE ("
        );

        for(int i = 0; i < channel.size(); i++) {
            sql.append("item.sales_channel = ? ");
            if(i != channel.size()-1) {
                sql.append("OR ");
            }
            searchParams.add(channel.get(i));
        }
            
        sql.append(
            ") AND item.channel_order_date IS NOT NULL AND item.channel_order_date BETWEEN ? AND ? " +
            "GROUP BY salesChannel, datetime " +
            "HAVING salesChannel IS NOT NULL AND datetime IS NOT NULL " + 
            "ORDER BY datetime asc"
        );
        searchParams.add(startDate.toString());
        searchParams.add(endDate.toString());

        Object[] objs = new Object[searchParams.size()];
        objs = searchParams.stream().toArray();
        projs = jdbcTemplate.query(sql.toString(), new SalesChannelPerformanceProjection.SalesPayAmount.Mapper(), objs);
        // 초기화 객체 업데이트
        List<SalesChannelPerformanceProjection.SalesPayAmount> updatedProjs = this.updateSalesChannelSalesPayAmountProjs(resultProjs, projs);
        return updatedProjs;
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

    /*
     * sales channel registration and unit projs 세팅
     */
    private List<SalesChannelPerformanceProjection.RegistrationAndUnit> getSalesChannelRegistrationAndUnitInitProjs(LocalDateTime startDate, LocalDateTime endDate, String dimension) {
        List<SalesChannelPerformanceProjection.RegistrationAndUnit> projs = new ArrayList<>();

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
            
            SalesChannelPerformanceProjection.RegistrationAndUnit proj = SalesChannelPerformanceProjection.RegistrationAndUnit.builder()
                    .datetime(datetime.toString())
                    .build();
            projs.add(proj);
        }
        return projs;
    }

    private List<SalesChannelPerformanceProjection.RegistrationAndUnit> updateSalesChannelRegistrationAndUnitProjs(List<SalesChannelPerformanceProjection.RegistrationAndUnit> initProjs, List<SalesChannelPerformanceProjection.RegistrationAndUnit> regAndUnitProjs) {
        List<SalesChannelPerformanceProjection.RegistrationAndUnit> projs = new ArrayList<>();

        initProjs.forEach(r -> {
            List<SalesChannelPerformanceProjection> salesChannelProjs = new ArrayList<>();
            regAndUnitProjs.forEach(r2 -> {
                if(r.getDatetime().equals(r2.getDatetime())) {
                    SalesChannelPerformanceProjection salesChannelProj = SalesChannelPerformanceProjection.builder()
                        .salesChannel(r2.getPerformance().stream().findFirst().get().getSalesChannel())
                        .orderRegistration(r2.getPerformance().stream().findFirst().get().getOrderRegistration())
                        .salesRegistration(r2.getPerformance().stream().findFirst().get().getSalesRegistration())
                        .orderUnit(r2.getPerformance().stream().findFirst().get().getOrderUnit())
                        .salesUnit(r2.getPerformance().stream().findFirst().get().getSalesUnit())
                        .build();

                    salesChannelProjs.add(salesChannelProj);
                }
                r.setPerformance(salesChannelProjs);
            });
        });
        projs.addAll(initProjs);
        return projs;
    }

    /*
     * sales channel sales payamount projs 세팅
     */
    private List<SalesChannelPerformanceProjection.SalesPayAmount> getSalesChannelSalesPayAmountInitProjs(LocalDateTime startDate, LocalDateTime endDate) {
        List<SalesChannelPerformanceProjection.SalesPayAmount> projs = new ArrayList<>();

        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        for (long i = 0; i <= dateDiff; i++) {
            LocalDateTime datetime = startDate.plusDays(i);

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }
            
            SalesChannelPerformanceProjection.SalesPayAmount proj = SalesChannelPerformanceProjection.SalesPayAmount.builder()
                    .datetime(datetime.toString())
                    .build();
            projs.add(proj);
        }
        return projs;
    }

    private List<SalesChannelPerformanceProjection.SalesPayAmount> updateSalesChannelSalesPayAmountProjs(List<SalesChannelPerformanceProjection.SalesPayAmount> initProjs, List<SalesChannelPerformanceProjection.SalesPayAmount> salesPayAmountProjs) {
        List<SalesChannelPerformanceProjection.SalesPayAmount> projs = new ArrayList<>();

        initProjs.forEach(r -> {
            List<SalesChannelPerformanceProjection> salesChannelProjs = new ArrayList<>();
            salesPayAmountProjs.forEach(r2 -> {
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
