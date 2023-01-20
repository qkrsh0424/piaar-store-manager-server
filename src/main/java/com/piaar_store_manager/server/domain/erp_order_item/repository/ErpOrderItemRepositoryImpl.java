package com.piaar_store_manager.server.domain.erp_order_item.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.domain.erp_order_item.entity.ErpOrderItemEntity;
import com.piaar_store_manager.server.domain.erp_order_item.entity.QErpOrderItemEntity;
import com.piaar_store_manager.server.domain.erp_order_item.proj.ErpOrderItemProj;
import com.piaar_store_manager.server.domain.product.entity.QProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.QProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.piaar_store_manager.server.utils.CustomFieldUtils;
import com.querydsl.core.QueryException;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class ErpOrderItemRepositoryImpl implements ErpOrderItemRepositoryCustom {
    private final JPAQueryFactory query;

    private final QErpOrderItemEntity qErpOrderItemEntity = QErpOrderItemEntity.erpOrderItemEntity;
    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QProductOptionEntity qProductOptionEntity = QProductOptionEntity.productOptionEntity;
    private final QProductCategoryEntity qProductCategoryEntity = QProductCategoryEntity.productCategoryEntity;

    @Autowired
    public ErpOrderItemRepositoryImpl(
            JPAQueryFactory query
    ) {
        this.query = query;
    }

    @Override
    public List<ErpOrderItemEntity> qfindAllByIdList(List<UUID> idList) {
        JPQLQuery customQuery = query.from(qErpOrderItemEntity)
                .select(qErpOrderItemEntity)
                .where(qErpOrderItemEntity.id.in(idList));

        QueryResults<ErpOrderItemEntity> result = customQuery.fetchResults();
        return result.getResults();
    }

    @Override
    public List<ErpOrderItemProj> qfindAllM2OJ(Map<String, Object> params) {
        JPQLQuery customQuery = query.from(qErpOrderItemEntity)
                .select(Projections.fields(ErpOrderItemProj.class,
                        qErpOrderItemEntity.as("erpOrderItem"),
                        qProductEntity.as("product"),
                        qProductOptionEntity.as("productOption"),
                        qProductCategoryEntity.as("productCategory")
                ))
                .where(eqSalesYn(params), eqReleaseYn(params))
                .where(lkSearchCondition(params))
                .where(withinDateRange(params))
                .leftJoin(qProductOptionEntity).on(qErpOrderItemEntity.optionCode.eq(qProductOptionEntity.code))
                .leftJoin(qProductEntity).on(qProductOptionEntity.productCid.eq(qProductEntity.cid))
                .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid));

        QueryResults<ErpOrderItemProj> result = customQuery.fetchResults();

        return result.getResults();
    }
    
    @Override
    public List<ErpOrderItemProj> qfindAllM2OJByReleaseItem(Map<String, Object> params) {
        JPQLQuery customQuery = query.from(qErpOrderItemEntity)
                .select(Projections.fields(ErpOrderItemProj.class,
                        qErpOrderItemEntity.as("erpOrderItem"),
                        qProductEntity.as("product"),
                        qProductOptionEntity.as("productOption"),
                        qProductCategoryEntity.as("productCategory")
                ))
                .where(eqSalesYn(params), eqReleaseYn(params))
                .where(lkSearchCondition(params))
                .where(withinDateRange(params))
                .leftJoin(qProductOptionEntity).on(qErpOrderItemEntity.releaseOptionCode.eq(qProductOptionEntity.code))
                .leftJoin(qProductEntity).on(qProductOptionEntity.productCid.eq(qProductEntity.cid))
                .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid));

        QueryResults<ErpOrderItemProj> result = customQuery.fetchResults();

        return result.getResults();
    }

    @Override
    public List<ErpOrderItemProj> qfindAllM2OJByIdList(List<UUID> idList, Map<String, Object> params) {
        JPQLQuery customQuery = query.from(qErpOrderItemEntity)
                .select(
                        Projections.fields(ErpOrderItemProj.class,
                                qErpOrderItemEntity.as("erpOrderItem"),
                                qProductEntity.as("product"),
                                qProductOptionEntity.as("productOption"),
                                qProductCategoryEntity.as("productCategory")
                        )
                )
                .where(qErpOrderItemEntity.id.in(idList))
                .where(eqSalesYn(params), eqReleaseYn(params))
                .leftJoin(qProductOptionEntity).on(qErpOrderItemEntity.optionCode.eq(qProductOptionEntity.code))
                .leftJoin(qProductEntity).on(qProductOptionEntity.productCid.eq(qProductEntity.cid))
                .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid));

        QueryResults<ErpOrderItemProj> result = customQuery.fetchResults();
        return result.getResults();
    }

    @Override
    public List<ErpOrderItemProj> qfindAllM2OJByReleasedItemIdList(List<UUID> idList, Map<String, Object> params) {
        JPQLQuery customQuery = query.from(qErpOrderItemEntity)
                .select(
                        Projections.fields(ErpOrderItemProj.class,
                                qErpOrderItemEntity.as("erpOrderItem"),
                                qProductEntity.as("product"),
                                qProductOptionEntity.as("productOption"),
                                qProductCategoryEntity.as("productCategory")
                        )
                )
                .where(qErpOrderItemEntity.id.in(idList))
                .where(eqSalesYn(params), eqReleaseYn(params))
                .leftJoin(qProductOptionEntity).on(qErpOrderItemEntity.releaseOptionCode.eq(qProductOptionEntity.code))
                .leftJoin(qProductEntity).on(qProductOptionEntity.productCid.eq(qProductEntity.cid))
                .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid));

        QueryResults<ErpOrderItemProj> result = customQuery.fetchResults();
        return result.getResults();
    }

    @Override
    public Page<ErpOrderItemProj> qfindAllM2OJByPage(Map<String, Object> params, Pageable pageable) {
        JPQLQuery customQuery = query.from(qErpOrderItemEntity)
                .select(Projections.fields(ErpOrderItemProj.class,
                        qErpOrderItemEntity.as("erpOrderItem"),
                        qProductEntity.as("product"),
                        qProductOptionEntity.as("productOption"),
                        qProductCategoryEntity.as("productCategory")
                ))
                .where(eqSalesYn(params), eqReleaseYn(params), eqStockReflectYn(params))
                .where(lkSearchCondition(params))
                .where(withinDateRange(params))
                .leftJoin(qProductOptionEntity).on(qErpOrderItemEntity.optionCode.eq(qProductOptionEntity.code))
                .leftJoin(qProductEntity).on(qProductOptionEntity.productCid.eq(qProductEntity.cid))
                .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        try {
            this.sortPagedData(customQuery, pageable);
        } catch (QueryException e) {
            throw new CustomInvalidDataException(e.getMessage());
        }

        QueryResults<ErpOrderItemProj> result = customQuery.fetchResults();
        return new PageImpl<ErpOrderItemProj>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<ErpOrderItemProj> qfindReleaseItemM2OJByPage(Map<String, Object> params, Pageable pageable) {
        JPQLQuery customQuery = query.from(qErpOrderItemEntity)
                .select(Projections.fields(ErpOrderItemProj.class,
                        qErpOrderItemEntity.as("erpOrderItem"),
                        qProductEntity.as("product"),
                        qProductOptionEntity.as("productOption"),
                        qProductCategoryEntity.as("productCategory")
                ))
                .where(eqSalesYn(params), eqReleaseYn(params), eqStockReflectYn(params))
                .where(lkSearchCondition(params))
                .where(withinDateRange(params))
                .leftJoin(qProductOptionEntity).on(qErpOrderItemEntity.releaseOptionCode.eq(qProductOptionEntity.code))
                .leftJoin(qProductEntity).on(qProductOptionEntity.productCid.eq(qProductEntity.cid))
                .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        try {
            this.sortPagedData(customQuery, pageable);
        } catch (QueryException e) {
            throw new CustomInvalidDataException(e.getMessage());
        }

        QueryResults<ErpOrderItemProj> result = customQuery.fetchResults();
        return new PageImpl<ErpOrderItemProj>(result.getResults(), pageable, result.getTotal());
    }

    private void sortPagedData(JPQLQuery customQuery, Pageable pageable) {
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder erpOrderItemBuilder = new PathBuilder(qErpOrderItemEntity.getType(), qErpOrderItemEntity.getMetadata());
            PathBuilder productBuilder = new PathBuilder(qProductEntity.getType(), qProductEntity.getMetadata());
            PathBuilder productOptionBuilder = new PathBuilder(qProductOptionEntity.getType(), qProductOptionEntity.getMetadata());
            PathBuilder productCategoryBuilder = new PathBuilder(qProductCategoryEntity.getType(), qProductCategoryEntity.getMetadata());

            switch (o.getProperty().toString()) {
                case "categoryName":
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, productCategoryBuilder.get("name")));
                    break;
                case "prodManagementName":
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, productBuilder.get("managementName")));
                    break;
                case "prodDefaultName":
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, productBuilder.get("defaultName")));
                    break;
                case "optionManagementName":
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, productOptionBuilder.get("managementName")));
                    break;
                case "optionReleaseLocation":
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, productOptionBuilder.get("releaseLocation")));
                case "optionDefaultName":
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, productOptionBuilder.get("defaultName")));
                    break;
                case "optionStockUnit":
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, productOptionBuilder.get("stockUnit")));
                    break;
                default:
                    if (CustomFieldUtils.getFieldByName(qErpOrderItemEntity, o.getProperty().toString()) == null) {
                        throw new QueryException("올바른 데이터가 아닙니다.");
                    }
                    customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, erpOrderItemBuilder.get(o.getProperty())));
            }

            customQuery.orderBy(qErpOrderItemEntity.cid.asc());
        }
    }

    private BooleanExpression eqSalesYn(Map<String, Object> params) {
        String salesYn = params.get("salesYn") == null ? null : params.get("salesYn").toString();

        if (salesYn == null) {
            return null;
        } else {
            return qErpOrderItemEntity.salesYn.eq(salesYn);
        }
    }

    private BooleanExpression eqReleaseYn(Map<String, Object> params) {
        String releaseYn = params.get("releaseYn") == null ? null : params.get("releaseYn").toString();

        if (releaseYn == null) {
            return null;
        } else {
            return qErpOrderItemEntity.releaseYn.eq(releaseYn);
        }
    }

    private BooleanExpression eqStockReflectYn(Map<String, Object> params) {
        String stockReflectYn = params.get("stockReflectYn") == null ? null : params.get("stockReflectYn").toString();

        if (stockReflectYn == null) {
            return null;
        } else {
            return qErpOrderItemEntity.stockReflectYn.eq(stockReflectYn);
        }
    }

    private BooleanExpression withinDateRange(Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        String periodType = params.get("periodType") == null ? null : params.get("periodType").toString();

        if (params.get("startDate") == null || params.get("endDate") == null || periodType == null) {
            return null;
        }

        startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);

        if (startDate.isAfter(endDate)) {
            throw new CustomInvalidDataException("조회기간을 정확히 선택해 주세요.");
        }

        if (periodType.equals("registration")) {
            return qErpOrderItemEntity.createdAt.between(startDate, endDate);
        } else if (periodType.equals("sales")) {
            return qErpOrderItemEntity.salesAt.between(startDate, endDate);
        } else if (periodType.equals("release")) {
            return qErpOrderItemEntity.releaseAt.between(startDate, endDate);
        } else if (periodType.equals("channelOrderDate")) {
            return qErpOrderItemEntity.channelOrderDate.between(startDate, endDate);
        } else {
            throw new CustomInvalidDataException("상세조건이 올바르지 않습니다.");
        }
    }

    private BooleanExpression lkSearchCondition(Map<String, Object> params) {
        String columnName = params.get("searchColumnName") == null ? null : params.get("searchColumnName").toString();
        String searchQuery = params.get("searchQuery") == null ? null : params.get("searchQuery").toString();
        if (columnName == null || searchQuery == null) {
            return null;
        }

        try {
            StringPath columnNameStringPath = null;
            switch (columnName) {
                case "categoryName":
                    columnNameStringPath = CustomFieldUtils.getFieldValue(qProductCategoryEntity, "name");
                    break;
                case "prodManagementName":
                    columnNameStringPath = CustomFieldUtils.getFieldValue(qProductEntity, "managementName");
                    break;
                case "prodDefaultName":
                    columnNameStringPath = CustomFieldUtils.getFieldValue(qProductEntity, "defaultName");
                    break;
                case "optionManagementName":
                    columnNameStringPath = CustomFieldUtils.getFieldValue(qProductOptionEntity, "managementName");
                    break;
                case "optionReleaseLocation":
                    columnNameStringPath = CustomFieldUtils.getFieldValue(qProductOptionEntity, "releaseLocation");
                    break;
                case "optionDefaultName":
                    columnNameStringPath = CustomFieldUtils.getFieldValue(qProductOptionEntity, "defaultName");
                    break;
                default:
                    if (CustomFieldUtils.getFieldByName(qErpOrderItemEntity, columnName) == null) {
                        throw new QueryException("올바른 데이터가 아닙니다.");
                    }
                    columnNameStringPath = CustomFieldUtils.getFieldValue(qErpOrderItemEntity, columnName);
            }

            return columnNameStringPath.contains(searchQuery);

        } catch (ClassCastException e) {
            throw new CustomInvalidDataException("허용된 데이터 타입이 아닙니다.");
        } catch (QueryException e) {
            throw new CustomInvalidDataException(e.getMessage());
        }
    }
}
