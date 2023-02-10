package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.erp_order_item.entity.QErpOrderItemEntity;
import com.piaar_store_manager.server.domain.product.entity.QProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.QProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.piaar_store_manager.server.domain.sales_performance.filter.DashboardPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.filter.SalesPerformanceSearchFilter;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesCategoryPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesChannelPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesProductPerformanceProjection;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesProductPerformanceProjection.BestOptionPerformance;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesProductPerformanceProjection.BestProductPerformance;
import com.piaar_store_manager.server.domain.sales_performance.proj.SalesProductPerformanceProjection.Performance;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.piaar_store_manager.server.utils.CustomDateUtils;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class SalesPerformanceRepositoryImpl implements SalesPerformanceRepositoryCustom {
    private final JPAQueryFactory query;

    private final QErpOrderItemEntity qErpOrderItemEntity = QErpOrderItemEntity.erpOrderItemEntity;
    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QProductOptionEntity qProductOptionEntity = QProductOptionEntity.productOptionEntity;
    private final QProductCategoryEntity qProductCategoryEntity = QProductCategoryEntity.productCategoryEntity;

    @Autowired
    public SalesPerformanceRepositoryImpl(JPAQueryFactory query) {
        this.query = query;
    }

    @Override
    public List<SalesPerformanceProjection> qSearchDashBoardByParams(DashboardPerformanceSearchFilter filter) {
        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
        List<String> dateValues = filter.getSearchDate().stream()
                .map(r -> CustomDateUtils.changeUtcDateTime(r, utcHourDifference).toLocalDate().toString())
                .collect(Collectors.toList());

        // 날짜별 데이터 초기화 - dashboardProjs의 결과로 모든 날짜별 데이터가 조회되지 않으므로 초기화 실행
        List<SalesPerformanceProjection> projs = this.getDashboardInitProjs(dateValues);

        StringPath datetime = Expressions.stringPath("datetime");

        List<SalesPerformanceProjection> dashboardProjs = query
                .select(
                        Projections.fields(SalesPerformanceProjection.class,
                                dateFormatTemplate(dateAddHourTemplate(utcHourDifference), "%Y-%m-%d").as(datetime),
                                (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull()).then(1)
                                        .otherwise(0)).sum().as("orderRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(1)
                                        .otherwise(0)).sum().as("salesRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.unit.isNotNull())
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("orderUnit"),
                                (new CaseBuilder()
                                        .when(qErpOrderItemEntity.unit.isNotNull()
                                                .and(qErpOrderItemEntity.salesYn.eq("y")))
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("salesUnit"),
                                (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum())
                                        .as("orderPayAmount"),
                                (new CaseBuilder()
                                        .when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                                        .otherwise(0)).sum().as("salesPayAmount")))
                .from(qErpOrderItemEntity)
                .where(qErpOrderItemEntity.channelOrderDate.isNotNull())
                .where(dateFormatTemplate(dateAddHourTemplate(utcHourDifference), "%Y-%m-%d").in(dateValues))
                .groupBy(datetime)
                .orderBy(datetime.asc())
                .fetch();

        // 실행 결과로 projs를 세팅
        this.updateDashboardProjs(projs, dashboardProjs);
        return projs;
    }

    @Override
    public List<SalesPerformanceProjection> qSearchSalesPerformance(SalesPerformanceSearchFilter filter) {
        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;

        // 날짜별 데이터 초기화
        List<SalesPerformanceProjection> projs = this.getSalesPerformanceInitProjs(filter);

        List<SalesPerformanceProjection> performanceProjs = query
                .select(
                        Projections.fields(SalesPerformanceProjection.class,
                                dateFormatTemplate(dateAddHourTemplate(utcHourDifference), "%Y-%m-%d").as("datetime"),
                                (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                                        .then(1)
                                        .otherwise(0)).sum().as("orderRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.unit.isNotNull())
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("orderUnit"),
                                (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum())
                                        .as("orderPayAmount"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(1)
                                        .otherwise(0)).sum().as("salesRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("salesUnit"),
                                (new CaseBuilder()
                                        .when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                                        .otherwise(0)).sum().as("salesPayAmount")))
                .from(qErpOrderItemEntity)
                .where(qErpOrderItemEntity.channelOrderDate.isNotNull())
                .where(withinDateRange(filter.getStartDate(), filter.getEndDate()))
                .groupBy(dateFormatTemplate(dateAddHourTemplate(utcHourDifference), "%Y-%m-%d"))
                .orderBy(dateFormatTemplate(dateAddHourTemplate(utcHourDifference), "%Y-%m-%d").asc())
                .fetch();

        // 실행 결과로 projs를 세팅
        this.updateSalesPerformanceProjs(projs, performanceProjs);
        return projs;
    }

    // datetime을 기준으로 판매채널 성과를 조회한다
    @Override
    public List<SalesChannelPerformanceProjection.Performance> qSearchSalesPerformanceByChannel(SalesPerformanceSearchFilter filter) {
        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;

        // 날짜별 채널데이터 초기화
        List<SalesChannelPerformanceProjection.Performance> projs = this.getSalesChannelPerformanceInitProjs(filter);

        StringPath datetime = Expressions.stringPath("datetime");
        StringPath salesChannel = Expressions.stringPath("salesChannel");

        List<SalesChannelPerformanceProjection> performanceProjs = query
                .select(
                        Projections.fields(SalesChannelPerformanceProjection.class,
                                dateFormatTemplate(dateAddHourTemplate(utcHourDifference), "%Y-%m-%d").as(datetime),
                                qErpOrderItemEntity.salesChannel.coalesce("").as(salesChannel),
                                (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                                        .then(1)
                                        .otherwise(0)).sum().as("orderRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.unit.isNotNull())
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("orderUnit"),
                                (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum())
                                        .as("orderPayAmount"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(1)
                                        .otherwise(0)).sum().as("salesRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("salesUnit"),
                                (new CaseBuilder()
                                        .when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                                        .otherwise(0)).sum().as("salesPayAmount")))
                .from(qErpOrderItemEntity)
                .where(qErpOrderItemEntity.channelOrderDate.isNotNull())
                .where(withinDateRange(filter.getStartDate(), filter.getEndDate()))
                .where(includesOptionCodes(filter.getOptionCodes()))
                .groupBy(salesChannel, datetime)
                .orderBy(datetime.asc())
                .fetch();

        // 실행 결과로 projs를 세팅
        this.updateSalesChannelPerformanceProjs(projs, performanceProjs);
        return projs;
    }

    @Override
    public List<SalesChannelPerformanceProjection> qSearchProductOptionSalesPerformanceByChannel(SalesPerformanceSearchFilter filter) {
        StringPath optionCode = Expressions.stringPath("optionCode");
        StringPath salesChannel = Expressions.stringPath("salesChannel");

        List<SalesChannelPerformanceProjection> performanceProjs = query
                .select(
                        Projections.fields(SalesChannelPerformanceProjection.class,
                                qErpOrderItemEntity.salesChannel.coalesce("").as(salesChannel),
                                qErpOrderItemEntity.optionCode.coalesce("").as(optionCode),
                                (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                                        .then(1)
                                        .otherwise(0)).sum().as("orderRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.unit.isNotNull())
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("orderUnit"),
                                (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum())
                                        .as("orderPayAmount"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(1)
                                        .otherwise(0)).sum().as("salesRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("salesUnit"),
                                (new CaseBuilder()
                                        .when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                                        .otherwise(0)).sum().as("salesPayAmount")))
                .from(qErpOrderItemEntity)
                .where(qErpOrderItemEntity.channelOrderDate.isNotNull())
                .leftJoin(qProductOptionEntity).on(qProductOptionEntity.code.eq(qErpOrderItemEntity.optionCode))
                .leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
                .where(withinDateRange(filter.getStartDate(), filter.getEndDate()))
                .where(includesProductCodes(filter.getProductCodes()))
                .where(includesOptionCodes(filter.getOptionCodes()))
                .groupBy(salesChannel, optionCode)
                .fetch();

        // 실행 결과로 projs를 세팅
        return performanceProjs;
    }

    @Override
    public List<SalesCategoryPerformanceProjection.Performance> qSearchSalesPerformanceByCategory(SalesPerformanceSearchFilter filter, List<String> categoryName) {
        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;

        // 날짜별 카테고리 데이터 초기화
        List<SalesCategoryPerformanceProjection.Performance> projs = this.getSalesCategoryPerformanceInitProjs(filter, categoryName);

        StringPath productCategoryName = Expressions.stringPath("productCategoryName");
        StringPath datetime = Expressions.stringPath("datetime");

        List<SalesCategoryPerformanceProjection> performanceProjs = query
                .select(
                        Projections.fields(SalesCategoryPerformanceProjection.class,
                                dateFormatTemplate(dateAddHourTemplate(utcHourDifference), "%Y-%m-%d").as(datetime),
                                qProductCategoryEntity.name.as(productCategoryName),
                                (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                                        .then(1)
                                        .otherwise(0)).sum().as("orderRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.unit.isNotNull())
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("orderUnit"),
                                (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum())
                                        .as("orderPayAmount"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(1)
                                        .otherwise(0)).sum().as("salesRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("salesUnit"),
                                (new CaseBuilder()
                                        .when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                                        .otherwise(0)).sum().as("salesPayAmount")))
                .from(qErpOrderItemEntity)
                .leftJoin(qProductOptionEntity).on(qProductOptionEntity.code.eq(qErpOrderItemEntity.optionCode))
                .leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
                .leftJoin(qProductCategoryEntity).on(qProductCategoryEntity.cid.eq(qProductEntity.productCategoryCid))
                .where(qErpOrderItemEntity.channelOrderDate.isNotNull())
                .where(withinDateRange(filter.getStartDate(), filter.getEndDate()))
                .groupBy(productCategoryName, datetime)
                .orderBy(datetime.asc())
                .fetch();

        // 실행 결과로 projs를 세팅
        this.updateSalesCategoryPerformanceProjs(projs, performanceProjs);
        return projs;
    }

    @Override
    public List<SalesCategoryPerformanceProjection.ProductPerformance> qSearchSalesProductPerformanceByCategory(SalesPerformanceSearchFilter filter, List<String> categoryName) {
        // 카테고리별 데이터 초기화
        List<SalesCategoryPerformanceProjection.ProductPerformance> projs = this.getSalesCategoryAndProductPerformanceInitProjs(categoryName);

        StringPath productCategoryName = Expressions.stringPath("productCategoryName");
        StringPath productName = Expressions.stringPath("productName");

        List<SalesCategoryPerformanceProjection> performanceProjs = query
                .select(
                        Projections.fields(SalesCategoryPerformanceProjection.class,
                                qProductCategoryEntity.name.as(productCategoryName),
                                (ExpressionUtils.as(JPAExpressions.select(qProductEntity.defaultName)
                                        .from(qProductOptionEntity)
                                        .join(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
                                        .where(qErpOrderItemEntity.optionCode.eq(qProductOptionEntity.code)),
                                        productName)),
                                (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                                        .then(1)
                                        .otherwise(0)).sum().as("orderRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.unit.isNotNull())
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("orderUnit"),
                                (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum())
                                        .as("orderPayAmount"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(1)
                                        .otherwise(0)).sum().as("salesRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("salesUnit"),
                                (new CaseBuilder()
                                        .when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                                        .otherwise(0)).sum().as("salesPayAmount")))
                .from(qErpOrderItemEntity)
                .leftJoin(qProductOptionEntity).on(qProductOptionEntity.code.eq(qErpOrderItemEntity.optionCode))
                .leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
                .leftJoin(qProductCategoryEntity).on(qProductCategoryEntity.cid.eq(qProductEntity.productCategoryCid))
                .where(qErpOrderItemEntity.channelOrderDate.isNotNull())
                .where(withinDateRange(filter.getStartDate(), filter.getEndDate()))
                .groupBy(productName)
                .orderBy(productCategoryName.asc())
                .fetch();

        // 실행 결과로 projs를 세팅
        this.updateSalesCategoryAndProductPerformanceProjs(projs, performanceProjs);
        return projs;
    }

    @Override
    public List<Performance> qSearchSalesPerformanceByProductOption(SalesPerformanceSearchFilter filter) {
        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;

        // 날짜별 채널데이터 초기화
        List<SalesProductPerformanceProjection.Performance> projs = this.getSalesProductOptionPerformanceInitProjs(filter);

        StringPath datetime = Expressions.stringPath("datetime");
        StringPath productDefaultName = Expressions.stringPath("productDefaultName");
        StringPath optionDefaultName = Expressions.stringPath("optionDefaultName");
        StringPath productCode = Expressions.stringPath("productCode");
        StringPath optionCode = Expressions.stringPath("optionCode");

        List<SalesProductPerformanceProjection> performanceProjs = query
                .select(
                        Projections.fields(SalesProductPerformanceProjection.class,
                                dateFormatTemplate(dateAddHourTemplate(utcHourDifference), "%Y-%m-%d").as(datetime),
                                qProductEntity.defaultName.as(productDefaultName),
                                qProductEntity.code.as(productCode),
                                qProductOptionEntity.defaultName.as(optionDefaultName),
                                qProductOptionEntity.code.as(optionCode),
                                (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                                        .then(1)
                                        .otherwise(0)).sum().as("orderRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.unit.isNotNull())
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("orderUnit"),
                                (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum())
                                        .as("orderPayAmount"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(1)
                                        .otherwise(0)).sum().as("salesRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("salesUnit"),
                                (new CaseBuilder()
                                        .when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                                        .otherwise(0)).sum().as("salesPayAmount")))
                .from(qErpOrderItemEntity)
                .leftJoin(qProductOptionEntity).on(qProductOptionEntity.code.eq(qErpOrderItemEntity.optionCode))
                .leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
                .where(qErpOrderItemEntity.channelOrderDate.isNotNull())
                .where(withinDateRange(filter.getStartDate(), filter.getEndDate()))
                .where(includesProductCodes(filter.getProductCodes()))
                .where(includesOptionCodes(filter.getOptionCodes()))
                .groupBy(optionCode, datetime)
                .fetch();

        // 실행 결과로 projs를 세팅
        this.updateSalesProductOptionPerformanceProjs(projs, performanceProjs);
        return projs;
    }

    @Override
    public List<SalesPerformanceProjection> qSearchSalesPerformanceByProduct(SalesPerformanceSearchFilter filter) {
        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;

        // 날짜별 채널데이터 초기화
        List<SalesPerformanceProjection> projs = this.getProductSalesPerformanceInitProjs(filter);

        StringPath datetime = Expressions.stringPath("datetime");
        StringPath productCode = Expressions.stringPath("productCode");

        List<SalesProductPerformanceProjection> performanceProjs = query
                .select(
                        Projections.fields(SalesProductPerformanceProjection.class,
                                dateFormatTemplate(dateAddHourTemplate(utcHourDifference), "%Y-%m-%d").as(datetime),
                                qProductEntity.code.as(productCode),
                                (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                                        .then(1)
                                        .otherwise(0)).sum().as("orderRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.unit.isNotNull())
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("orderUnit"),
                                (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum())
                                        .as("orderPayAmount"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(1)
                                        .otherwise(0)).sum().as("salesRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("salesUnit"),
                                (new CaseBuilder()
                                        .when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                                        .otherwise(0)).sum().as("salesPayAmount")))
                .from(qErpOrderItemEntity)
                .leftJoin(qProductOptionEntity).on(qProductOptionEntity.code.eq(qErpOrderItemEntity.optionCode))
                .leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
                .where(qErpOrderItemEntity.channelOrderDate.isNotNull())
                .where(withinDateRange(filter.getStartDate(), filter.getEndDate()))
                .where(includesOptionCodes(filter.getOptionCodes()))
                .groupBy(productCode, datetime)
                .fetch();

        // 실행 결과로 projs를 세팅
        this.updateProductSalesPerformanceProjs(projs, performanceProjs);
        return projs;
    }

    @Override
    public Page<BestProductPerformance> qSearchBestProductPerformanceByPaging(SalesPerformanceSearchFilter filter, Pageable pageable) {
        NumberPath<Integer> salesPayAmount = Expressions.numberPath(Integer.class, "salesPayAmount");
        NumberPath<Integer> salesUnit = Expressions.numberPath(Integer.class, "salesUnit");
        String pageOrderByColumn = filter.getPageOrderByColumn() == null ? "payAmount" : filter.getPageOrderByColumn();

        List<Integer> productCids = query.from(qErpOrderItemEntity)
                .select(qProductEntity.cid)
                .leftJoin(qProductOptionEntity).on(qProductOptionEntity.code.eq(qErpOrderItemEntity.optionCode))
                .leftJoin(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
                .leftJoin(qProductCategoryEntity).on(qProductCategoryEntity.cid.eq(qProductEntity.productCategoryCid))
                .where(qErpOrderItemEntity.channelOrderDate.isNotNull())
                .where(withinDateRange(filter.getStartDate(), filter.getEndDate()))
                .where(includesSearchChannels(filter.getSalesChannels()))
                .where(includesSearchCategorys(filter.getProductCategoryNames()))
                .groupBy(qProductEntity.code)
                .fetch();

        JPQLQuery customQuery = query.from(qErpOrderItemEntity)
                .select(
                        Projections.fields(BestProductPerformance.class,
                                qProductEntity.code.as("productCode"),
                                qProductEntity.defaultName.as("productDefaultName"),
                                (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                                        .then(1)
                                        .otherwise(0)).sum().as("orderRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.unit.isNotNull())
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("orderUnit"),
                                (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum())
                                        .as("orderPayAmount"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(1)
                                        .otherwise(0)).sum().as("salesRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("salesUnit"),
                                (new CaseBuilder()
                                        .when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                                        .otherwise(0)).sum().as(salesPayAmount)))
                .leftJoin(qProductOptionEntity).on(qProductOptionEntity.code.eq(qErpOrderItemEntity.optionCode))
                .leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
                .leftJoin(qProductCategoryEntity).on(qProductCategoryEntity.cid.eq(qProductEntity.productCategoryCid))
                .where(qErpOrderItemEntity.channelOrderDate.isNotNull())
                .where(withinDateRange(filter.getStartDate(), filter.getEndDate()))
                .where(includesSearchChannels(filter.getSalesChannels()))
                .where(includesSearchCategorys(filter.getProductCategoryNames()))
                .groupBy(qProductEntity.code)
                .orderBy(pageOrderByColumn.equals("payAmount") ? salesPayAmount.desc() : salesUnit.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<BestProductPerformance> performanceProjs = customQuery.fetch();
        long totalCount = productCids.size();

        return new PageImpl<BestProductPerformance>(performanceProjs, pageable, totalCount);
    }

    @Override
    public List<BestOptionPerformance> qSearchProductOptionPerformance(SalesPerformanceSearchFilter filter) {
        NumberPath<Integer> salesPayAmount = Expressions.numberPath(Integer.class, "salesPayAmount");

        // 검색된 옵션들의 매출데이터 초기화
        List<BestOptionPerformance> projs = query
                .select(
                        Projections.fields(BestOptionPerformance.class,
                                qProductEntity.defaultName.as("productDefaultName"),
                                qProductEntity.code.as("productCode"),
                                qProductOptionEntity.defaultName.as("optionDefaultName"),
                                qProductOptionEntity.code.as("optionCode"),
                                (Expressions.as(Expressions.constant(0), "orderRegistration")),
                                (Expressions.as(Expressions.constant(0), "orderUnit")),
                                (Expressions.as(Expressions.constant(0), "orderPayAmount")),
                                (Expressions.as(Expressions.constant(0), "salesRegistration")),
                                (Expressions.as(Expressions.constant(0), "salesUnit")),
                                (Expressions.as(Expressions.constant(0), "salesPayAmount"))
                        )
                )
                .from(qProductOptionEntity)
                .leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
                .where(includesProductCodes(filter.getProductCodes()))
                .orderBy(orderByProductCodes(filter.getProductCodes()))
                .fetch();

        List<BestOptionPerformance> performanceProjs = query.from(qErpOrderItemEntity)
                .select(
                        Projections.fields(BestOptionPerformance.class,
                                qProductOptionEntity.code.as("optionCode"),
                                (new CaseBuilder().when(qErpOrderItemEntity.cid.isNotNull())
                                        .then(1)
                                        .otherwise(0)).sum().as("orderRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.unit.isNotNull())
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("orderUnit"),
                                (qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge).sum())
                                        .as("orderPayAmount"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(1)
                                        .otherwise(0)).sum().as("salesRegistration"),
                                (new CaseBuilder().when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.unit)
                                        .otherwise(0)).sum().as("salesUnit"),
                                (new CaseBuilder()
                                        .when(qErpOrderItemEntity.salesYn.eq("y"))
                                        .then(qErpOrderItemEntity.price.add(qErpOrderItemEntity.deliveryCharge))
                                        .otherwise(0)).sum().as(salesPayAmount)))
                .leftJoin(qProductOptionEntity).on(qProductOptionEntity.code.eq(qErpOrderItemEntity.optionCode))
                .leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
                .where(qErpOrderItemEntity.channelOrderDate.isNotNull())
                .where(withinDateRange(filter.getStartDate(), filter.getEndDate()))
                .where(includesProductCodes(filter.getProductCodes()))
                .where(includesSearchChannels(filter.getSalesChannels()))
                .groupBy(qProductOptionEntity.code)
                .fetch();
        
        this.updateOptionPerformanceProjs(projs, performanceProjs);
        return projs;
    }

    private OrderSpecifier<?> orderByProductCodes(List<String> productCodes) {
        if(productCodes == null) {
                return null;
        }
        return Expressions.stringTemplate("FIELD({0}, {1})", qProductEntity.code, productCodes).asc();
    }

    /*
     * date range 설정
     */
    private BooleanExpression withinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }

        if (startDate.isAfter(endDate)) {
            throw new CustomInvalidDataException("조회기간을 정확히 선택해 주세요.");
        }

        return qErpOrderItemEntity.channelOrderDate.between(startDate, endDate);
    }

    /*
     * option code 확인
     */
    private BooleanExpression includesOptionCodes(List<String> optionCodes) {
        if (optionCodes == null || optionCodes.size() == 0) {
            return null;
        }
        return qErpOrderItemEntity.optionCode.isNotEmpty().and(qErpOrderItemEntity.optionCode.in(optionCodes));
    }

    /*
     * sales channel 확인
     */
    private BooleanExpression includesSearchChannels(List<String> searchSalesChannels) {
        if (searchSalesChannels == null || searchSalesChannels.size() == 0) {
            return null;
        }
        return qErpOrderItemEntity.salesChannel.isNotEmpty().and(qErpOrderItemEntity.salesChannel.in(searchSalesChannels));
    }

    /*
     * product code 확인
     */
    private BooleanExpression includesProductCodes(List<String> searchProductCodes) {
        if (searchProductCodes == null || searchProductCodes.size() == 0) {
            return null;
        }
        return qProductEntity.code.isNotEmpty().and(qProductEntity.code.in(searchProductCodes));
    }

    /*
     * category name 확인
     */
    private BooleanExpression includesSearchCategorys(List<String> searchCategoryNames) {
        if (searchCategoryNames == null || searchCategoryNames.size() == 0) {
            return null;
        }
        return qProductCategoryEntity.name.isNotEmpty().and(qProductCategoryEntity.name.in(searchCategoryNames));
    }

    /*
     * date format setting
     */
    private DateTemplate<String> dateFormatTemplate(DateTemplate<String> channelOrderDate, String format) {
        DateTemplate<String> formattedDate = Expressions.dateTemplate(
                String.class,
                "DATE_FORMAT({0}, {1})",
                channelOrderDate,
                ConstantImpl.create(format));

        return formattedDate;
    }

    /*
     * hour setting
     */
    private DateTemplate<String> dateAddHourTemplate(int hour) {
        LocalTime addTime = LocalTime.of(hour, 0);

        DateTemplate<String> addDate = Expressions.dateTemplate(
                String.class,
                "ADDTIME({0}, {1})",
                qErpOrderItemEntity.channelOrderDate,
                ConstantImpl.create(addTime));

        return addDate;
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
                    .salesRegistration(0)
                    .orderUnit(0)
                    .salesUnit(0)
                    .orderPayAmount(0)
                    .salesPayAmount(0)
                    .build();

            projs.add(proj);
        }

        return projs;
    }

    private void updateDashboardProjs(List<SalesPerformanceProjection> initProjs, List<SalesPerformanceProjection> resultProjs) {
        initProjs.forEach(initProj -> {
            resultProjs.forEach(resultProj -> {
                if (initProj.getDatetime().equals(resultProj.getDatetime())) {
                    initProj.setOrderRegistration(resultProj.getOrderRegistration())
                            .setOrderUnit(resultProj.getOrderUnit())
                            .setOrderPayAmount(resultProj.getOrderPayAmount())
                            .setSalesRegistration(resultProj.getSalesRegistration())
                            .setSalesUnit(resultProj.getSalesUnit())
                            .setSalesPayAmount(resultProj.getSalesPayAmount());
                }
            });
        });
    }

    /*
     * sales performance projs 세팅
     */
    private List<SalesPerformanceProjection> getSalesPerformanceInitProjs(SalesPerformanceSearchFilter filter) {
        LocalDateTime startDate = filter.getStartDate();
        LocalDateTime endDate = filter.getEndDate();
        
        if (startDate == null || endDate == null) {
                return null;
        }
        
        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
        startDate = CustomDateUtils.changeUtcDateTime(startDate, utcHourDifference);
        endDate = CustomDateUtils.changeUtcDateTime(endDate, utcHourDifference);
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        List<SalesPerformanceProjection> projs = new ArrayList<>();
        LocalDateTime datetime = null;
        for (int i = 0; i <= dateDiff; i++) {
            datetime = startDate.plusDays(i);
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
                if (r.getDatetime().equals(r2.getDatetime())) {
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

    private List<SalesChannelPerformanceProjection.Performance> getSalesChannelPerformanceInitProjs(SalesPerformanceSearchFilter filter) {
        LocalDateTime startDate = filter.getStartDate();
        LocalDateTime endDate = filter.getEndDate();
        
        if (startDate == null || endDate == null) {
                return null;
        }
        
        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
        startDate = CustomDateUtils.changeUtcDateTime(filter.getStartDate(), utcHourDifference);
        endDate = CustomDateUtils.changeUtcDateTime(filter.getEndDate(), utcHourDifference);
        int dateDiff = (int) Duration.between(filter.getStartDate(), filter.getEndDate()).toDays();

        List<SalesChannelPerformanceProjection.Performance> projs = new ArrayList<>();
        LocalDateTime datetime = null;
        for (int i = 0; i <= dateDiff; i++) {
            datetime = startDate.plusDays(i);
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

    private void updateSalesChannelPerformanceProjs(List<SalesChannelPerformanceProjection.Performance> initProjs, List<SalesChannelPerformanceProjection> performanceProjs) {
        initProjs.forEach(r -> {
            List<SalesChannelPerformanceProjection> salesChannelProjs = new ArrayList<>();
            performanceProjs.forEach(r2 -> {
                if (r.getDatetime().equals(r2.getDatetime())) {
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
    }

    /*
     * sales category performance projs 세팅
     */
    private List<SalesCategoryPerformanceProjection.Performance> getSalesCategoryPerformanceInitProjs(SalesPerformanceSearchFilter filter, List<String> categoryName) {
        LocalDateTime startDate = filter.getStartDate();
        LocalDateTime endDate = filter.getEndDate();
        categoryName.add("미지정");
        
        if (startDate == null || endDate == null) {
                return null;
        }
        
        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
        startDate = CustomDateUtils.changeUtcDateTime(startDate, utcHourDifference);
        endDate = CustomDateUtils.changeUtcDateTime(endDate, utcHourDifference);
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
        LocalDateTime datetime = null;
        for (int i = 0; i <= dateDiff; i++) {
            datetime = startDate.plusDays(i);

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }

            SalesCategoryPerformanceProjection.Performance proj = SalesCategoryPerformanceProjection.Performance
                    .builder()
                    .datetime(datetime.toLocalDate().toString())
                    .performance(performances)
                    .build();
            projs.add(proj);
        }

        return projs;
    }

    private void updateSalesCategoryPerformanceProjs(List<SalesCategoryPerformanceProjection.Performance> initProjs,
            List<SalesCategoryPerformanceProjection> performanceProjs) {
        initProjs.forEach(r -> {
            performanceProjs.forEach(r2 -> {
                if (r.getDatetime().equals(r2.getDatetime())) {
                    List<SalesCategoryPerformanceProjection> updatedPerform = r.getPerformance().stream().map(r3 -> {
                        String categoryName = (r3.getProductCategoryName() == null || r3.getProductCategoryName().isBlank()) ? "미지정" : r3.getProductCategoryName();
                        
                        if (categoryName.equals(r2.getProductCategoryName())) {
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
                        } else {
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
    private List<SalesCategoryPerformanceProjection.ProductPerformance> getSalesCategoryAndProductPerformanceInitProjs(List<String> categoryName) {
        List<SalesCategoryPerformanceProjection.ProductPerformance> projs = categoryName.stream().map(category -> {
                SalesCategoryPerformanceProjection.ProductPerformance proj = SalesCategoryPerformanceProjection.ProductPerformance
                    .builder()
                    .productCategoryName(category)
                    .build();

                return proj;
        }).collect(Collectors.toList());
        
        return projs;
    }

    private void updateSalesCategoryAndProductPerformanceProjs(List<SalesCategoryPerformanceProjection.ProductPerformance> initProjs, List<SalesCategoryPerformanceProjection> performanceProjs) {
        initProjs.forEach(r -> {
            List<SalesCategoryPerformanceProjection> performances = new ArrayList<>();
            performanceProjs.forEach(r2 -> {
                if (r.getProductCategoryName().equals(r2.getProductCategoryName())) {
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

    private List<SalesProductPerformanceProjection.Performance> getSalesProductOptionPerformanceInitProjs(SalesPerformanceSearchFilter filter) {
        LocalDateTime startDate = filter.getStartDate();
        LocalDateTime endDate = filter.getEndDate();

        if (startDate == null || endDate == null) {
            return null;
        }

        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
        startDate = CustomDateUtils.changeUtcDateTime(startDate, utcHourDifference);
        endDate = CustomDateUtils.changeUtcDateTime(endDate, utcHourDifference);
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        List<SalesProductPerformanceProjection.Performance> projs = new ArrayList<>();
        LocalDateTime datetime = null;
        for (int i = 0; i <= dateDiff; i++) {
            datetime = startDate.plusDays(i);

            // 검색날짜가 범위에 벗어난다면 for문 탈출
            if (datetime.isAfter(endDate)) {
                break;
            }

            SalesProductPerformanceProjection.Performance proj = SalesProductPerformanceProjection.Performance.builder()
                    .datetime(datetime.toLocalDate().toString())
                    .build();
            projs.add(proj);
        }
        return projs;
    }

    private List<SalesPerformanceProjection> getProductSalesPerformanceInitProjs(SalesPerformanceSearchFilter filter) {
        LocalDateTime startDate = filter.getStartDate();
        LocalDateTime endDate = filter.getEndDate();
        
        if (startDate == null || endDate == null) {
                return null;
        }
        
        int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
        startDate = CustomDateUtils.changeUtcDateTime(startDate, utcHourDifference);
        endDate = CustomDateUtils.changeUtcDateTime(endDate, utcHourDifference);
        int dateDiff = (int) Duration.between(startDate, endDate).toDays();

        List<SalesPerformanceProjection> projs = new ArrayList<>();
        LocalDateTime datetime = null;
        for (int i = 0; i <= dateDiff; i++) {
            datetime = startDate.plusDays(i);
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

    private void updateProductSalesPerformanceProjs(List<SalesPerformanceProjection> initProjs,
            List<SalesProductPerformanceProjection> performanceProjs) {
        initProjs.forEach(r -> {
            performanceProjs.forEach(r2 -> {
                if (r.getDatetime().equals(r2.getDatetime())) {
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

    private List<SalesProductPerformanceProjection.Performance> updateSalesProductOptionPerformanceProjs(
            List<SalesProductPerformanceProjection.Performance> initProjs,
            List<SalesProductPerformanceProjection> performanceProjs) {
        List<SalesProductPerformanceProjection.Performance> projs = new ArrayList<>();

        initProjs.forEach(r -> {
            List<SalesProductPerformanceProjection> salesProductOptionProjs = new ArrayList<>();
            performanceProjs.forEach(r2 -> {
                if (r.getDatetime().equals(r2.getDatetime())) {
                    SalesProductPerformanceProjection salesProductOptionProj = SalesProductPerformanceProjection
                            .builder()
                            .datetime(r2.getDatetime())
                            .productCode(r2.getProductCode())
                            .productDefaultName(r2.getProductDefaultName())
                            .optionCode(r2.getOptionCode())
                            .optionDefaultName(r2.getOptionDefaultName())
                            .orderRegistration(r2.getOrderRegistration())
                            .orderUnit(r2.getOrderUnit())
                            .orderPayAmount(r2.getOrderPayAmount())
                            .salesRegistration(r2.getSalesRegistration())
                            .salesUnit(r2.getSalesUnit())
                            .salesPayAmount(r2.getSalesPayAmount())
                            .build();

                    salesProductOptionProjs.add(salesProductOptionProj);
                }
                r.setPerformance(salesProductOptionProjs);
            });
        });
        projs.addAll(initProjs);
        return projs;
    }

    private void updateOptionPerformanceProjs(List<SalesProductPerformanceProjection.BestOptionPerformance> initProjs, List<SalesProductPerformanceProjection.BestOptionPerformance> performanceProjs) {
        initProjs.forEach(r -> {
            performanceProjs.forEach(r2 -> {
                if (r.getOptionCode().equals(r2.getOptionCode())) {
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
}
