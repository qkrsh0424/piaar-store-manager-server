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
import com.piaar_store_manager.server.domain.product.entity.QProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.QProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class SalesPerformanceRepositoryImpl implements SalesPerformanceRepositoryCustom {
    private final JPAQueryFactory query;

    private final QErpOrderItemEntity qErpOrderItemEntity = QErpOrderItemEntity.erpOrderItemEntity;
    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QProductOptionEntity qProductOptionEntity = QProductOptionEntity.productOptionEntity;
    private final QProductCategoryEntity qProductCategoryEntity = QProductCategoryEntity.productCategoryEntity;

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
    public List<SalesChannelPerformanceProjection.Performance> qSearchSalesPerformanceByChannel(Map<String, Object> params) {
        List<SalesChannelPerformanceProjection.Performance> projs = this.getSalesChannelPerformanceInitProjs(params);

        List<SalesChannelPerformanceProjection> performanceProjs = (List<SalesChannelPerformanceProjection>) query.from(qErpOrderItemEntity)
            .where(qErpOrderItemEntity.channelOrderDate.isNotNull().and(withinDateRange(params)).and(eqSearchCondition(params)))
            .groupBy(
                qErpOrderItemEntity.salesChannel.coalesce(""),
                dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d")
            )
            .orderBy(dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d").asc())
            .transform(
                GroupBy.groupBy(
                    qErpOrderItemEntity.salesChannel.coalesce(""),
                    dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d")
                )
                .list(
                    Projections.fields(
                        SalesChannelPerformanceProjection.class,
                        dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d").as("datetime"),
                        qErpOrderItemEntity.salesChannel.coalesce("").as("salesChannel"),
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

    @Override
    public List<SalesCategoryPerformanceProjection.Performance> qSearchSalesPerformanceByCategory(Map<String, Object> params, List<String> categoryName) {
        // 전체 카테고리 조회
        List<SalesCategoryPerformanceProjection.Performance> projs = this.getSalesCategoryPerformanceInitProjs(categoryName, params);

        StringPath productCategoryName = Expressions.stringPath("productCategoryName");
        StringPath datetime = Expressions.stringPath("datetime");

        List<SalesCategoryPerformanceProjection> performanceProjs = query
            .select(
                Projections.fields(SalesCategoryPerformanceProjection.class,
                dateFormatTemplate(dateAddHourTemplate(9), "%Y-%m-%d").as(datetime),
                (ExpressionUtils.as(JPAExpressions.select(qProductCategoryEntity.name)
                    .from(qProductOptionEntity)
                    .join(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
                    .join(qProductCategoryEntity).on(qProductCategoryEntity.cid.eq(qProductEntity.productCategoryCid))
                    .where(qErpOrderItemEntity.optionCode.eq(qProductOptionEntity.code))
                    ,
                    productCategoryName
                )),
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
            ))
            .from(qErpOrderItemEntity)
            .where(qErpOrderItemEntity.channelOrderDate.isNotNull().and(withinDateRange(params)))
            .groupBy(productCategoryName, datetime)
            .orderBy(datetime.asc())
            .fetch();

        this.updateSalesCategoryPerformanceProjs(projs, performanceProjs);
        return projs;
    }

    @Override
    public List<SalesCategoryPerformanceProjection.ProductPerformance> qSearchSalesProductPerformanceByCategory(Map<String, Object> params, List<String> categoryName) {
        // 전체 카테고리 조회
        List<SalesCategoryPerformanceProjection.ProductPerformance> projs = this.getSalesCategoryAndProductPerformanceInitProjs(categoryName, params);

        StringPath productCategoryName = Expressions.stringPath("productCategoryName");
        StringPath productName = Expressions.stringPath("productName");

        List<SalesCategoryPerformanceProjection> performanceProjs = query
            .select(
                Projections.fields(SalesCategoryPerformanceProjection.class,
                (ExpressionUtils.as(JPAExpressions.select(qProductCategoryEntity.name)
                    .from(qProductOptionEntity)
                    .join(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
                    .join(qProductCategoryEntity).on(qProductCategoryEntity.cid.eq(qProductEntity.productCategoryCid))
                    .where(qErpOrderItemEntity.optionCode.eq(qProductOptionEntity.code))
                    ,
                    productCategoryName
                )),
                (ExpressionUtils.as(JPAExpressions.select(qProductEntity.defaultName)
                    .from(qProductOptionEntity)
                    .join(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
                    .where(qErpOrderItemEntity.optionCode.eq(qProductOptionEntity.code))
                    ,
                    productName
                )),
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
            ))
            .from(qErpOrderItemEntity)
            .where(qErpOrderItemEntity.channelOrderDate.isNotNull().and(withinDateRange(params)))
            .groupBy(productName)
            .orderBy(productCategoryName.asc())
            .fetch();

        this.updateSalesCategoryAndProductPerformanceProjs(projs, performanceProjs);
        return projs;
    }

    /*
     * dashboard projs 세팅
     */
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
    private List<SalesChannelPerformanceProjection.Performance> getSalesChannelPerformanceInitProjs(Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        
        if (params.get("startDate") == null || params.get("endDate") == null) {
            return null;
        }
        
        startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter).plusHours(9);
        endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter).plusHours(9);
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        List<SalesChannelPerformanceProjection.Performance> projs = new ArrayList<>();
        for (int i = 0; i <= dateDiff; i++) {
            LocalDateTime datetime = startDate.plusDays(i);

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }

            SalesChannelPerformanceProjection.Performance proj = SalesChannelPerformanceProjection.Performance.builder()
                .datetime(datetime.toLocalDate().toString())
                .build();
            projs.add(proj);
        }
        return projs;
    }

    private List<SalesChannelPerformanceProjection.Performance> updateSalesChannelPerformanceProjs(List<SalesChannelPerformanceProjection.Performance> initProjs, List<SalesChannelPerformanceProjection> performanceProjs) {
        List<SalesChannelPerformanceProjection.Performance> projs = new ArrayList<>();

        initProjs.forEach(r -> {
            List<SalesChannelPerformanceProjection> salesChannelProjs = new ArrayList<>();
            performanceProjs.forEach(r2 -> {
                if(r.getDatetime().equals(r2.getDatetime())) {
                    String channelName = r2.getSalesChannel().isBlank() ? "미지정" : r2.getSalesChannel();

                    SalesChannelPerformanceProjection salesChannelProj = SalesChannelPerformanceProjection.builder()
                        .datetime(r2.getDatetime())
                        .salesChannel(channelName)
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

    /*
     * sales category performance projs 세팅
     */
    private List<SalesCategoryPerformanceProjection.Performance> getSalesCategoryPerformanceInitProjs(List<String> categoryName, Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        categoryName.add("미지정");
        
        if (params.get("startDate") == null || params.get("endDate") == null) {
            return null;
        }
        
        startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter).plusHours(9);
        endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter).plusHours(9);
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        List<SalesCategoryPerformanceProjection> performances = categoryName.stream().map(r -> {
            SalesCategoryPerformanceProjection performance = SalesCategoryPerformanceProjection.builder()
                .productCategoryName(r)
                .orderRegistration(0)
                .orderUnit(0)
                .orderPayAmount(0)
                .salesRegistration(0)
                .salesUnit(0)
                .salesPayAmount(0)
                .build();
            return performance;
        }).collect(Collectors.toList());

        List<SalesCategoryPerformanceProjection.Performance> projs = new ArrayList<>();
        for (int i = 0; i <= dateDiff; i++) {
            LocalDateTime datetime = startDate.plusDays(i);

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }

            SalesCategoryPerformanceProjection.Performance proj = SalesCategoryPerformanceProjection.Performance.builder()
                .datetime(datetime.toLocalDate().toString())
                .performance(performances)
                .build();
            projs.add(proj);
        }

        return projs;
    }

    private void updateSalesCategoryPerformanceProjs(List<SalesCategoryPerformanceProjection.Performance> initProjs, List<SalesCategoryPerformanceProjection> performanceProjs) {
        initProjs.forEach(r -> {
            performanceProjs.forEach(r2 -> {
                if (r.getDatetime().equals(r2.getDatetime())) {
                    List<SalesCategoryPerformanceProjection> updatedPerform = r.getPerformance().stream().map(r3 -> {
                        String categoryName = (r3.getProductCategoryName() == null || r3.getProductCategoryName().isBlank()) ? "미지정" : r3.getProductCategoryName();
                        if(categoryName.equals(r2.getProductCategoryName())) {
                            SalesCategoryPerformanceProjection p = SalesCategoryPerformanceProjection.builder()
                                    .productCategoryName(categoryName)
                                    .orderRegistration(r2.getOrderRegistration())
                                    .orderUnit(r2.getOrderUnit())
                                    .orderPayAmount(r2.getOrderPayAmount())
                                    .salesRegistration(r2.getSalesRegistration())
                                    .salesUnit(r2.getSalesUnit())
                                    .salesPayAmount(r2.getSalesPayAmount())
                                    .build();
                                return p;
                        }else {
                            return r3;
                        }
                    }).collect(Collectors.toList());
    
                    r.setPerformance(updatedPerform);
                }
            });
        });
    }

    /*
     * sales category and product performance projs 세팅
     */
    private List<SalesCategoryPerformanceProjection.ProductPerformance> getSalesCategoryAndProductPerformanceInitProjs(List<String> categoryName, Map<String, Object> params) {
        List<SalesCategoryPerformanceProjection.ProductPerformance> projs = new ArrayList<>();
        
        for (int i = 0; i < categoryName.size(); i++) {
            SalesCategoryPerformanceProjection.ProductPerformance proj = SalesCategoryPerformanceProjection.ProductPerformance.builder()
                .productCategoryName(categoryName.get(i))
                .build();
            projs.add(proj);
        }
        return projs;
    }

    /*
     * sales category and product performance projs 세팅
     */
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

    private void updateSalesCategoryAndProductPerformanceProjs(List<SalesCategoryPerformanceProjection.ProductPerformance> initProjs, List<SalesCategoryPerformanceProjection> performanceProjs) {
        initProjs.forEach(r -> {
            List<SalesCategoryPerformanceProjection> performances = new ArrayList<>();
            performanceProjs.forEach(r2 -> {
                if(r.getProductCategoryName().equals(r2.getProductCategoryName())) {
                    SalesCategoryPerformanceProjection performance = SalesCategoryPerformanceProjection.builder()
                        .productCategoryName(r2.getProductCategoryName())
                        .productName(r2.getProductName())
                        .orderRegistration(r2.getOrderRegistration())
                        .orderUnit(r2.getOrderUnit())
                        .orderPayAmount(r2.getOrderPayAmount())
                        .salesRegistration(r2.getSalesRegistration())
                        .salesUnit(r2.getSalesUnit())
                        .salesPayAmount(r2.getSalesPayAmount())
                        .build();

                    performances.add(performance);
                }
            });
            r.setPerformance(performances);
        });
    }

    private BooleanExpression eqSearchCondition(Map<String, Object> params) {
        String[] searchCode = params.get("optionCode") == null ? null : params.get("optionCode").toString().split(",");
        if (searchCode == null || searchCode.length == 0) {
            return null;
        }

        return qErpOrderItemEntity.optionCode.isNotEmpty().and(qErpOrderItemEntity.optionCode.in(searchCode));
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
