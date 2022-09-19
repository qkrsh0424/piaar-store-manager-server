package com.piaar_store_manager.server.domain.erp_return_item.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.erp_order_item.entity.QErpOrderItemEntity;
import com.piaar_store_manager.server.domain.erp_return_item.entity.ErpReturnItemEntity;
import com.piaar_store_manager.server.domain.erp_return_item.entity.QErpReturnItemEntity;
import com.piaar_store_manager.server.domain.erp_return_item.proj.ErpReturnItemProj;
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
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ErpReturnItemRepositoryImpl implements ErpReturnItemRepositoryCustom {
    private final JPAQueryFactory query;
    
    private final QErpReturnItemEntity qErpReturnItemEntity = QErpReturnItemEntity.erpReturnItemEntity;
    private final QErpOrderItemEntity qErpOrderItemEntity = QErpOrderItemEntity.erpOrderItemEntity;
    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QProductOptionEntity qProductOptionEntity = QProductOptionEntity.productOptionEntity;
    private final QProductCategoryEntity qProductCategoryEntity = QProductCategoryEntity.productCategoryEntity;

    @Autowired
    public ErpReturnItemRepositoryImpl (
        JPAQueryFactory query
    ) {
        this.query = query;
    }

    

    @Override
    public List<ErpReturnItemEntity> qfindAllByIdList(List<UUID> idList) {
        JPQLQuery customQuery = query.from(qErpReturnItemEntity)
            .select(qErpReturnItemEntity)
            .where(qErpReturnItemEntity.id.in(idList));

        QueryResults<ErpReturnItemEntity> result = customQuery.fetchResults();
        return result.getResults();
    }

    @Override
    public Page<ErpReturnItemProj> qfindAllM2OJByPage(Map<String, Object> params, Pageable pageable) {
        JPQLQuery customQuery = query.from(qErpReturnItemEntity)
            .select(Projections.fields(ErpReturnItemProj.class,
                qErpReturnItemEntity.as("erpReturnItem"),
                qErpOrderItemEntity.as("erpOrderItem"),
                qProductEntity.as("product"),
                qProductOptionEntity.as("productOption"),
                qProductCategoryEntity.as("productCategory")
            ))
            .where(eqCollectYn(params), eqCollectCompleteYn(params), eqReturnCompleteYn(params))
            .where(withinDateRange(params))
            .leftJoin(qErpOrderItemEntity).on(qErpReturnItemEntity.erpOrderItemId.eq(qErpOrderItemEntity.id))
            .leftJoin(qProductOptionEntity).on(qErpOrderItemEntity.releaseOptionCode.eq(qProductOptionEntity.code))     // 반품상태에서 출고 옵션코드로 데이터를 조회한다
            .leftJoin(qProductEntity).on(qProductOptionEntity.productCid.eq(qProductEntity.cid))
            .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());
        
        try {
            this.sortPagedData(customQuery, pageable);
        } catch(QueryException e) {
            throw new CustomInvalidDataException(e.getMessage());
        }

        QueryResults<ErpReturnItemProj> result = customQuery.fetchResults();
        return new PageImpl<ErpReturnItemProj>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<ErpReturnItemProj> qfindAllM2OJByReleasedItemIdList(List<UUID> idList, Map<String, Object> params) {
        JPQLQuery customQuery = query.from(qErpReturnItemEntity)
                .select(
                        Projections.fields(ErpReturnItemProj.class,
                                qErpReturnItemEntity.as("erpReturnItem"),
                                qErpOrderItemEntity.as("erpOrderItem"),
                                qProductEntity.as("product"),
                                qProductOptionEntity.as("productOption"),
                                qProductCategoryEntity.as("productCategory")
                        )
                )                
                .where(qErpReturnItemEntity.id.in(idList))
                .where(eqCollectYn(params), eqCollectCompleteYn(params), eqReturnCompleteYn(params))
                .where(withinDateRange(params))
                .leftJoin(qErpOrderItemEntity).on(qErpReturnItemEntity.erpOrderItemId.eq(qErpOrderItemEntity.id))
                .leftJoin(qProductOptionEntity).on(qErpOrderItemEntity.releaseOptionCode.eq(qProductOptionEntity.code))
                .leftJoin(qProductEntity).on(qProductOptionEntity.productCid.eq(qProductEntity.cid))
                .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid));

        QueryResults<ErpReturnItemProj> result = customQuery.fetchResults();
        return result.getResults();
    }

    private void sortPagedData(JPQLQuery customQuery, Pageable pageable) {
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder erpReturnItemBuilder = new PathBuilder(qErpReturnItemEntity.getType(), qErpReturnItemEntity.getMetadata());
            PathBuilder erpOrderItemBuilder = new PathBuilder(qErpOrderItemEntity.getType(), qErpOrderItemEntity.getMetadata());
            PathBuilder productBuilder = new PathBuilder(qProductEntity.getType(), qProductEntity.getMetadata());
            PathBuilder productOptionBuilder = new PathBuilder(qProductOptionEntity.getType(), qProductOptionEntity.getMetadata());
            PathBuilder productCategoryBuilder = new PathBuilder(qProductCategoryEntity.getType(), qProductCategoryEntity.getMetadata());

            if(o.getProperty().toString().startsWith("order_")) {
                String matchedColumnName = o.getProperty().toString().replace("order_", "");
                switch (matchedColumnName) {
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
                        if(CustomFieldUtils.getFieldByName(qErpOrderItemEntity, matchedColumnName) == null) {
                            throw new QueryException("올바른 데이터가 아닙니다.");
                        }
                        customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, erpOrderItemBuilder.get(matchedColumnName)));
                }
            } else {
                if(CustomFieldUtils.getFieldByName(qErpReturnItemEntity, o.getProperty().toString()) == null) {
                    throw new QueryException("올바른 데이터가 아닙니다.");
                }
                customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, erpReturnItemBuilder.get(o.getProperty())));
            }
            customQuery.orderBy(qErpOrderItemEntity.cid.desc());
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
            return qErpReturnItemEntity.createdAt.between(startDate, endDate);
        } else if (periodType.equals("collecting")) {
            return qErpReturnItemEntity.collectAt.between(startDate, endDate);
        } else if (periodType.equals("collected")) {
            return qErpReturnItemEntity.collectCompleteAt.between(startDate, endDate);
        } else if (periodType.equals("completed")) {
            return qErpReturnItemEntity.returnCompleteAt.between(startDate, endDate);
        } else {
            throw new CustomInvalidDataException("상세조건이 올바르지 않습니다.");
        }
    }

    private BooleanExpression eqCollectYn(Map<String, Object> params) {
        String collectYn = params.get("collectYn") == null ? null : params.get("collectYn").toString();

        if(collectYn == null) {
            return null;
        } else {
            return qErpReturnItemEntity.collectYn.eq(collectYn);
        }
    }

    private BooleanExpression eqCollectCompleteYn(Map<String, Object> params) {
        String collectCompleteYn = params.get("collectCompleteYn") == null ? null : params.get("collectCompleteYn").toString();

        if(collectCompleteYn == null) {
            return null;
        } else {
            return qErpReturnItemEntity.collectCompleteYn.eq(collectCompleteYn);
        }
    }

    private BooleanExpression eqReturnCompleteYn(Map<String, Object> params) {
        String returnCompleteYn = params.get("returnCompleteYn") == null ? null : params.get("returnCompleteYn").toString();

        if(returnCompleteYn == null) {
            return null;
        } else {
            return qErpReturnItemEntity.returnCompleteYn.eq(returnCompleteYn);
        }
    }
}
