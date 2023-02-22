package com.piaar_store_manager.server.domain.sales_performance.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
import com.piaar_store_manager.server.domain.sales_performance.proj.BestProductPerformanceProjection.RelatedProductOptionPerformance;
import com.piaar_store_manager.server.domain.sales_performance.proj.BestProductPerformanceProjection;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.piaar_store_manager.server.utils.CustomDateUtils;
import com.piaar_store_manager.server.utils.CustomFieldUtils;
import com.querydsl.core.QueryException;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.DateTimePath;
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

		String periodType = filter.getPeriodType();
		StringPath datetime = Expressions.stringPath("datetime");

		List<SalesPerformanceProjection> dashboardProjs = query
				.select(
						Projections.fields(SalesPerformanceProjection.class,
								dateFormatTemplate(dateAddHourTemplate(periodType, utcHourDifference), "%Y-%m-%d").as("datetime"),
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
				.where(withinSearchDate(filter))
				.groupBy(datetime)
				.orderBy(datetime.asc())
				.fetch();

		return dashboardProjs;
	}

	@Override
	public List<SalesPerformanceProjection> qSearchSalesPerformance(SalesPerformanceSearchFilter filter) {
		int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
	
		String periodType = filter.getPeriodType();
		StringPath datetime = Expressions.stringPath("datetime");

		List<SalesPerformanceProjection> performanceProjs = query
				.select(
						Projections.fields(SalesPerformanceProjection.class,
								dateFormatTemplate(dateAddHourTemplate(periodType, utcHourDifference), "%Y-%m-%d").as("datetime"),
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
				.where(withinDateRange(periodType, filter.getStartDate(), filter.getEndDate()))
				.groupBy(datetime)
				.orderBy(datetime.asc())
				.fetch();

		return performanceProjs;
	}

	// datetime을 기준으로 판매채널 성과를 조회한다
	@Override
	public List<SalesChannelPerformanceProjection> qSearchSalesPerformanceByChannel(SalesPerformanceSearchFilter filter) {
		int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;
		
		String periodType = filter.getPeriodType();
		StringPath datetime = Expressions.stringPath("datetime");
		StringPath salesChannel = Expressions.stringPath("salesChannel");

		List<SalesChannelPerformanceProjection> performanceProjs = query
				.select(
						Projections.fields(SalesChannelPerformanceProjection.class,
								dateFormatTemplate(dateAddHourTemplate(periodType, utcHourDifference), "%Y-%m-%d").as("datetime"),
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
				.where(withinDateRange(periodType, filter.getStartDate(), filter.getEndDate()))
				.where(includesOptionCodes(filter.getOptionCodes()))
				.groupBy(salesChannel, datetime)
				.orderBy(datetime.asc())
				.fetch();

		return performanceProjs;
	}

	@Override
	public List<SalesChannelPerformanceProjection> qSearchProductOptionSalesPerformanceByChannel(SalesPerformanceSearchFilter filter) {
		String periodType = filter.getPeriodType();
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
				.leftJoin(qProductOptionEntity).on(qProductOptionEntity.code.eq(qErpOrderItemEntity.optionCode))
				.leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
				.where(withinDateRange(periodType, filter.getStartDate(), filter.getEndDate()))
				.where(includesProductCodes(filter.getProductCodes()))
				.where(includesOptionCodes(filter.getOptionCodes()))
				.groupBy(salesChannel, optionCode)
				.fetch();

		// 실행 결과로 projs를 세팅
		return performanceProjs;
	}

	@Override
	public List<SalesCategoryPerformanceProjection> qSearchSalesPerformanceByCategory(
			SalesPerformanceSearchFilter filter) {
		int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;

		String periodType = filter.getPeriodType();
		StringPath productCategoryName = Expressions.stringPath("productCategoryName");
		StringPath datetime = Expressions.stringPath("datetime");

		List<SalesCategoryPerformanceProjection> performanceProjs = query
				.select(
						Projections.fields(SalesCategoryPerformanceProjection.class,
								dateFormatTemplate(dateAddHourTemplate(periodType, utcHourDifference), "%Y-%m-%d").as("datetime"),
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
				.where(withinDateRange(periodType, filter.getStartDate(), filter.getEndDate()))
				.groupBy(productCategoryName, datetime)
				.orderBy(datetime.asc())
				.fetch();

		return performanceProjs;
	}

	@Override
	public List<SalesCategoryPerformanceProjection> qSearchSalesProductPerformanceByCategory(SalesPerformanceSearchFilter filter, List<String> categoryName) {
		String periodType = filter.getPeriodType();
		StringPath productCategoryName = Expressions.stringPath("productCategoryName");
		StringPath productDefaultName = Expressions.stringPath("productDefaultName");

		List<SalesCategoryPerformanceProjection> performanceProjs = query
				.select(
						Projections.fields(SalesCategoryPerformanceProjection.class,
								qProductCategoryEntity.name.as(productCategoryName),
								(ExpressionUtils.as(JPAExpressions.select(qProductEntity.defaultName)
										.from(qProductOptionEntity)
										.join(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
										.where(qErpOrderItemEntity.optionCode.eq(qProductOptionEntity.code)),
										productDefaultName)),
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
				.where(withinDateRange(periodType, filter.getStartDate(), filter.getEndDate()))
				.groupBy(productDefaultName)
				.orderBy(productCategoryName.asc())
				.fetch();

		return performanceProjs;
	}

	@Override
	public List<SalesProductPerformanceProjection> qSearchSalesPerformanceByProductOption(SalesPerformanceSearchFilter filter) {
		int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;

		String periodType = filter.getPeriodType();
		StringPath datetime = Expressions.stringPath("datetime");
		StringPath productDefaultName = Expressions.stringPath("productDefaultName");
		StringPath optionDefaultName = Expressions.stringPath("optionDefaultName");
		StringPath productCode = Expressions.stringPath("productCode");
		StringPath optionCode = Expressions.stringPath("optionCode");

		List<SalesProductPerformanceProjection> performanceProjs = query
				.select(
						Projections.fields(SalesProductPerformanceProjection.class,
								dateFormatTemplate(dateAddHourTemplate(periodType, utcHourDifference), "%Y-%m-%d").as("datetime"),
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
				.where(withinDateRange(periodType, filter.getStartDate(), filter.getEndDate()))
				.where(includesProductCodes(filter.getProductCodes()))
				.where(includesOptionCodes(filter.getOptionCodes()))
				.groupBy(optionCode, datetime)
				.fetch();

		return performanceProjs;
	}

	@Override
	public List<SalesProductPerformanceProjection> qSearchSalesPerformanceByProduct(SalesPerformanceSearchFilter filter) {
		int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;

		String periodType = filter.getPeriodType();
		StringPath datetime = Expressions.stringPath("datetime");
		StringPath productCode = Expressions.stringPath("productCode");

		List<SalesProductPerformanceProjection> performanceProjs = query
				.select(
						Projections.fields(SalesProductPerformanceProjection.class,
								dateFormatTemplate(dateAddHourTemplate(periodType, utcHourDifference), "%Y-%m-%d").as("datetime"),
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
				.where(withinDateRange(periodType, filter.getStartDate(), filter.getEndDate()))
				.where(includesOptionCodes(filter.getOptionCodes()))
				.groupBy(productCode, datetime)
				.fetch();

		return performanceProjs;
	}

	@Override
	public Page<BestProductPerformanceProjection> qSearchBestProductPerformanceByPaging(SalesPerformanceSearchFilter filter, Pageable pageable) {
		NumberPath<Integer> salesPayAmount = Expressions.numberPath(Integer.class, "salesPayAmount");
		NumberPath<Integer> salesUnit = Expressions.numberPath(Integer.class, "salesUnit");
		String periodType = filter.getPeriodType();
		String pageOrderByColumn = filter.getPageOrderByColumn() == null ? "payAmount" : filter.getPageOrderByColumn();

		List<Integer> productCids = query.from(qErpOrderItemEntity)
				.select(qProductEntity.cid)
				.leftJoin(qProductOptionEntity).on(qProductOptionEntity.code.eq(qErpOrderItemEntity.optionCode))
				.leftJoin(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
				.leftJoin(qProductCategoryEntity).on(qProductCategoryEntity.cid.eq(qProductEntity.productCategoryCid))
				.where(withinDateRange(periodType, filter.getStartDate(), filter.getEndDate()))
				.where(includesSearchChannels(filter.getSalesChannels()))
				.where(includesSearchCategorys(filter.getProductCategoryNames()))
				.groupBy(qProductEntity.code)
				.fetch();

		JPQLQuery customQuery = query.from(qErpOrderItemEntity)
				.select(
						Projections.fields(BestProductPerformanceProjection.class,
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
				.where(withinDateRange(periodType, filter.getStartDate(), filter.getEndDate()))
				.where(includesSearchChannels(filter.getSalesChannels()))
				.where(includesSearchCategorys(filter.getProductCategoryNames()))
				.groupBy(qProductEntity.code)
				.orderBy(pageOrderByColumn.equals("payAmount") ? salesPayAmount.desc() : salesUnit.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize());

		List<BestProductPerformanceProjection> performanceProjs = customQuery.fetch();
		long totalCount = productCids.size();

		return new PageImpl<BestProductPerformanceProjection>(performanceProjs, pageable, totalCount);
	}

	@Override
	public List<BestProductPerformanceProjection.RelatedProductOptionPerformance> qSearchProductOptionPerformance(SalesPerformanceSearchFilter filter) {
		NumberPath<Integer> salesPayAmount = Expressions.numberPath(Integer.class, "salesPayAmount");
		String periodType = filter.getPeriodType();

		// 검색된 옵션들의 매출데이터 초기화
		List<RelatedProductOptionPerformance> projs = query
				.select(
						Projections.fields(RelatedProductOptionPerformance.class,
								qProductEntity.defaultName.as("productDefaultName"),
								qProductEntity.code.as("productCode"),
								qProductOptionEntity.defaultName.as("optionDefaultName"),
								qProductOptionEntity.code.as("optionCode"),
								(Expressions.as(Expressions.constant(0), "orderRegistration")),
								(Expressions.as(Expressions.constant(0), "orderUnit")),
								(Expressions.as(Expressions.constant(0), "orderPayAmount")),
								(Expressions.as(Expressions.constant(0), "salesRegistration")),
								(Expressions.as(Expressions.constant(0), "salesUnit")),
								(Expressions.as(Expressions.constant(0), "salesPayAmount"))))
				.from(qProductOptionEntity)
				.leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
				.where(includesProductCodes(filter.getProductCodes()))
				.orderBy(orderByProductCodes(filter.getProductCodes()))
				.fetch();

		List<RelatedProductOptionPerformance> performanceProjs = query.from(qErpOrderItemEntity)
				.select(
						Projections.fields(RelatedProductOptionPerformance.class,
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
				.where(withinDateRange(periodType, filter.getStartDate(), filter.getEndDate()))
				.where(includesProductCodes(filter.getProductCodes()))
				.where(includesSearchChannels(filter.getSalesChannels()))
				.groupBy(qProductOptionEntity.code)
				.fetch();

		this.updateOptionPerformanceProjs(projs, performanceProjs);
		return projs;
	}

	private OrderSpecifier<?> orderByProductCodes(List<String> productCodes) {
		if (productCodes == null) {
			return null;
		}
		return Expressions.stringTemplate("FIELD({0}, {1})", qProductEntity.code, productCodes).asc();
	}

	/*
	 * date range 설정
	 */
	// private BooleanExpression withinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
	// 	if (startDate == null || endDate == null) {
	// 		return null;
	// 	}

	// 	if (startDate.isAfter(endDate)) {
	// 		throw new CustomInvalidDataException("조회기간을 정확히 선택해 주세요.");
	// 	}

	// 	return qErpOrderItemEntity.channelOrderDate.between(startDate, endDate);
	// }

	private BooleanExpression withinDateRange(String periodType, LocalDateTime startDate, LocalDateTime endDate) {
		if (periodType == null || startDate == null || endDate == null) {
			return null;
		}

		if (startDate.isAfter(endDate)) {
			throw new CustomInvalidDataException("조회기간을 정확히 선택해 주세요.");
		}

		try {
			DateTimePath periodTypeDateTimePath = null;
			switch(periodType) {
				case "registration":
					periodTypeDateTimePath = CustomFieldUtils.getFieldValue(qErpOrderItemEntity, "createdAt");
					break;
				case "channelOrderDate":
					periodTypeDateTimePath = CustomFieldUtils.getFieldValue(qErpOrderItemEntity, "channelOrderDate");
					break;
				default:
					if (CustomFieldUtils.getFieldByName(qErpOrderItemEntity, periodType) == null) {
						throw new QueryException("올바른 데이터가 아닙니다.");
					}
			}

			return periodTypeDateTimePath.isNotNull().and(periodTypeDateTimePath.between(startDate, endDate));
		} catch (QueryException e) {
            throw new CustomInvalidDataException(e.getMessage());
		}
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

		if(searchSalesChannels.contains("미지정")) {
			return qErpOrderItemEntity.salesChannel.isEmpty().or(qErpOrderItemEntity.salesChannel.in(searchSalesChannels));
		}

		return qErpOrderItemEntity.salesChannel.in(searchSalesChannels);
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

		if(searchCategoryNames.contains("미지정")) {
			return qErpOrderItemEntity.optionCode.isEmpty().or(qProductCategoryEntity.name.in(searchCategoryNames));
		}

		return qProductCategoryEntity.name.in(searchCategoryNames);
	}

	private BooleanExpression withinSearchDate(DashboardPerformanceSearchFilter filter) {
		int utcHourDifference = filter.getUtcHourDifference() != null ? filter.getUtcHourDifference() : 0;

		String periodType = filter.getPeriodType();
		List<String> dateValues = filter.getSearchDate().stream()
				.map(r -> CustomDateUtils.changeUtcDateTime(r, utcHourDifference).toLocalDate().toString())
				.collect(Collectors.toList());

		if (periodType.equals("registration")) {
			return qErpOrderItemEntity.createdAt.isNotNull()
				.and(dateFormatTemplate(dateAddHourTemplate(periodType, utcHourDifference), "%Y-%m-%d").in(dateValues));
		} else if (periodType.equals("channelOrderDate")) {
			return qErpOrderItemEntity.channelOrderDate.isNotNull()
				.and(dateFormatTemplate(dateAddHourTemplate(periodType, utcHourDifference), "%Y-%m-%d").in(dateValues));
		} else {
            throw new CustomInvalidDataException("날짜검색 조건이 올바르지 않습니다.");
		}
	}

	private DateTemplate<String> dateAddHourTemplate(String periodType, int hour) {
		LocalTime addTime = LocalTime.of(hour, 0);

		if(periodType == null) {
			throw new CustomInvalidDataException("날짜검색 조건이 올바르지 않습니다.");
		}

		try {
			DateTimePath periodTypeDateTimePath = null;
			switch(periodType) {
				case "registration":
					periodTypeDateTimePath = CustomFieldUtils.getFieldValue(qErpOrderItemEntity, "createdAt");
					break;
				case "channelOrderDate":
					periodTypeDateTimePath = CustomFieldUtils.getFieldValue(qErpOrderItemEntity, "channelOrderDate");
					break;
				default:
					if (CustomFieldUtils.getFieldByName(qErpOrderItemEntity, periodType) == null) {
						throw new QueryException("올바른 데이터가 아닙니다.");
					}
			}

			DateTemplate<String> addHourDate = Expressions.dateTemplate(
					String.class,
					"ADDTIME({0}, {1})",
					periodTypeDateTimePath,
					ConstantImpl.create(addTime));

			return addHourDate;
		} catch (QueryException e) {
            throw new CustomInvalidDataException(e.getMessage());
		}
	}

	private DateTemplate<String> dateFormatTemplate(DateTemplate<String> addHourDate, String format) {
		DateTemplate<String> formattedDate = Expressions.dateTemplate(
				String.class,
				"DATE_FORMAT({0}, {1})",
				addHourDate,
				ConstantImpl.create(format));

		return formattedDate;
	}

	private void updateOptionPerformanceProjs(List<RelatedProductOptionPerformance> initProjs, List<RelatedProductOptionPerformance> performanceProjs) {
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
