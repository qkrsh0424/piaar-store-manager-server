package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.erp_order_item.entity.QErpOrderItemEntity;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjectionV2;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjectionV2.Performance;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class SalesPerformanceRepositoryImpl implements SalesPerformanceRepositoryCustom {
    private final JPAQueryFactory query;

    private final QErpOrderItemEntity qErpOrderItemEntity = QErpOrderItemEntity.erpOrderItemEntity;

    @Autowired
    public SalesPerformanceRepositoryImpl(
        JPAQueryFactory query
    ) {
        this.query = query;
    }

    @Override
    public List<SalesPerformanceProjection> qSearchDashBoardByParams(Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        if(params.get("date") == null) {
            return null;
        }
        
        List<String> dateList = Arrays.asList(params.get("date").toString().split(","));
        List<LocalDateTime> localDatetimeList = dateList.stream().map(r -> LocalDateTime.parse(r, formatter)).collect(Collectors.toList());
        List<String> dateValues =  localDatetimeList.stream().map(r -> r.plusHours(9).toLocalDate().toString()).collect(Collectors.toList());
        List<SalesPerformanceProjection> projs = this.getDashboardInitProjs(dateValues);

        List<SalesPerformanceProjection> dashboardProjs = (List<SalesPerformanceProjection>) query.from(qErpOrderItemEntity)
            .where(qErpOrderItemEntity.channelOrderDate.isNotNull().and(dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d").in(dateValues)))
            .groupBy(dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d"))
            .orderBy(dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d").asc())
            .transform(
                GroupBy.groupBy(dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d"))
                .list(
                    Projections.fields(
                        SalesPerformanceProjection.class,
                        dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d").as("datetime"),
                        (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                            .then(1)
                            .otherwise(0)
                        ).sum().as("orderRegistration"),
                        (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum()).as("orderPayAmount"),
                        (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                            .then(1)
                            .otherwise(0)
                        ).sum().as("salesRegistration"),
                        (new CaseBuilder()
                            .when(qErpOrderItemEntity.salesYn.eq("y"))
                            .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                            .otherwise(0)
                        ).sum().as("salesPayAmount")
                    )
                )
            );

        this.updateDashboardProjs(projs, dashboardProjs);
        return projs;
    }

    @Override
    public List<SalesPerformanceProjection> qSearchSalesPerformance(Map<String, Object> params) {
        List<SalesPerformanceProjection> projs = this.getSalesPerformanceInitProjs(params);

        List<SalesPerformanceProjection> performanceProjs = (List<SalesPerformanceProjection>) query.from(qErpOrderItemEntity)
            .where(qErpOrderItemEntity.channelOrderDate.isNotNull().and(withinDateRange(params)))
            .groupBy(dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d"))
            .orderBy(dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d").asc())
            .transform(
                GroupBy.groupBy(dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d"))
                .list(
                    Projections.fields(
                        SalesPerformanceProjection.class,
                        dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d").as("datetime"),
                        (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                            .then(1)
                            .otherwise(0)
                        ).sum().as("orderRegistration"),
                        (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                            .then(qErpOrderItemEntity.unit)
                            .otherwise(0)
                        ).sum().as("orderUnit"),
                        (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum()).as("orderPayAmount"),
                        (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                            .then(1)
                            .otherwise(0)
                        ).sum().as("salesRegistration"),
                        (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                            .then(qErpOrderItemEntity.unit)
                            .otherwise(0)
                        ).sum().as("salesUnit"),
                        (new CaseBuilder()
                            .when(qErpOrderItemEntity.salesYn.eq("y"))
                            .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                            .otherwise(0)
                        ).sum().as("salesPayAmount")
                    )
                )
            );

        this.updateSalesPerformanceProjs(projs, performanceProjs);
        return projs;
    }

    @Override
    public List<SalesChannelPerformanceProjectionV2> qSearchSalesChannelPerformance(Map<String, Object> params) {
        List<SalesChannelPerformanceProjectionV2> projs = this.getSalesChannelPerformanceInitProjs(params);

        List<Performance> performanceProjs = (List<Performance>) query.from(qErpOrderItemEntity)
            .where(qErpOrderItemEntity.channelOrderDate.isNotNull().and(withinDateRange(params)))
            .groupBy(
                qErpOrderItemEntity.salesChannel.coalesce("미지정"),
                dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d")
            )
            .orderBy(dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d").asc())
            .transform(
                GroupBy.groupBy(
                    qErpOrderItemEntity.salesChannel.coalesce("미지정"),
                    dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d")
                )
                .list(
                    Projections.fields(
                        Performance.class,
                        dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d").as("datetime"),
                        qErpOrderItemEntity.salesChannel.coalesce("미지정").as("salesChannel"),
                        (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                            .then(1)
                            .otherwise(0)
                        ).sum().as("orderRegistration"),
                        (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                            .then(qErpOrderItemEntity.unit)
                            .otherwise(0)
                        ).sum().as("orderUnit"),
                        (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum()).as("orderPayAmount"),
                        (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                            .then(1)
                            .otherwise(0)
                        ).sum().as("salesRegistration"),
                        (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                            .then(qErpOrderItemEntity.unit)
                            .otherwise(0)
                        ).sum().as("salesUnit"),
                        (new CaseBuilder()
                            .when(qErpOrderItemEntity.salesYn.eq("y"))
                            .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                            .otherwise(0)
                        ).sum().as("salesPayAmount")
                    )
                )
            );

        this.updateSalesChannelPerformanceProjs(projs, performanceProjs);
        return projs;
    }

    /*
     * sales performance projs 세팅
     */
    private List<SalesPerformanceProjection> getSalesPerformanceInitProjs(Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        
        if (params.get("startDate") == null || params.get("endDate") == null) {
            return null;
        }
        
        startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter).plusHours(9);
        endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter).plusHours(9);
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        List<SalesPerformanceProjection> projs = new ArrayList<>();
        for (int i = 0; i <= dateDiff; i++) {
            LocalDateTime datetime = startDate.plusDays(i);

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }

            SalesPerformanceProjection proj = SalesPerformanceProjection.builder()
                    .datetime(datetime.toLocalDate().toString())
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

    private void updateSalesPerformanceProjs(List<SalesPerformanceProjection> initProjs, List<SalesPerformanceProjection> performanceProjs) {
        initProjs.forEach(r -> {
            performanceProjs.forEach(r2 -> {
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
     * sales channel performance projs 세팅
     */
    private List<SalesChannelPerformanceProjectionV2> getSalesChannelPerformanceInitProjs(Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        
        if (params.get("startDate") == null || params.get("endDate") == null) {
            return null;
        }
        
        startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter).plusHours(9);
        endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter).plusHours(9);
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        List<SalesChannelPerformanceProjectionV2> projs = new ArrayList<>();
        for (int i = 0; i <= dateDiff; i++) {
            LocalDateTime datetime = startDate.plusDays(i);

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }

            SalesChannelPerformanceProjectionV2 proj = SalesChannelPerformanceProjectionV2.builder()
                .datetime(datetime.toLocalDate().toString())
                .build();
            projs.add(proj);
        }
        return projs;
    }

    private List<SalesChannelPerformanceProjectionV2> updateSalesChannelPerformanceProjs(List<SalesChannelPerformanceProjectionV2> initProjs, List<Performance> performanceProjs) {
        List<SalesChannelPerformanceProjectionV2> projs = new ArrayList<>();

        initProjs.forEach(r -> {
            List<Performance> salesChannelProjs = new ArrayList<>();
            performanceProjs.forEach(r2 -> {
                if(r.getDatetime().equals(r2.getDatetime())) {
                    Performance salesChannelProj = Performance.builder()
                        .datetime(r2.getDatetime())
                        .salesChannel(r2.getSalesChannel())
                        .orderRegistration(r2.getOrderRegistration())
                        .orderUnit(r2.getOrderUnit())
                        .orderPayAmount(r2.getOrderPayAmount())
                        .salesRegistration(r2.getSalesRegistration())
                        .salesUnit(r2.getSalesUnit())
                        .salesPayAmount(r2.getSalesPayAmount())
                        .build();

                    salesChannelProjs.add(salesChannelProj);
                }
                r.setPerformance(salesChannelProjs);
            });
        });
        projs.addAll(initProjs);
        return projs;
    }

    private List<SalesPerformanceProjection> getDashboardInitProjs(List<String> dateValues) {
        List<SalesPerformanceProjection> projs = new ArrayList<>();
        for (int i = 0; i < dateValues.size(); i++) {
            SalesPerformanceProjection proj = SalesPerformanceProjection.builder()
                    .datetime(dateValues.get(i))
                    .orderRegistration(0)
                    .orderPayAmount(0)
                    .salesRegistration(0)
                    .salesPayAmount(0)
                    .build();

            projs.add(proj);
        }

        return projs;
    }

    private void updateDashboardProjs(List<SalesPerformanceProjection> initProjs, List<SalesPerformanceProjection> dashboardProjs) {
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
    
    private BooleanExpression withinDateRange(Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (params.get("startDate") == null || params.get("endDate") == null) {
            return null;
        }

        startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter).plusHours(9);
        endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter).plusHours(9);

        if (startDate.isAfter(endDate)) {
            throw new CustomInvalidDataException("조회기간을 정확히 선택해 주세요.");
        }

        return qErpOrderItemEntity.channelOrderDate.between(startDate, endDate);
    }

    private DateTemplate<String> dateFormatTemplate(DateTemplate<String> channelOrderDate, String format) {
        DateTemplate<String> formattedDate = Expressions.dateTemplate(
            String.class,
            "DATE_FORMAT({0}, {1})",
            channelOrderDate,
            ConstantImpl.create(format)
        );

        return formattedDate;
    }

    private DateTemplate<String> dateAddHourTemplate(int hour) {
        LocalTime addTime = LocalTime.of(hour, 0);
        
        DateTemplate<String> addDate = Expressions.dateTemplate(
            String.class,
            "ADDTIME({0}, {1})",
            qErpOrderItemEntity.channelOrderDate, 
            ConstantImpl.create(addTime)
        );

        return addDate;
    }
}
