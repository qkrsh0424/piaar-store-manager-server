package com.piaar_store_manager.server.domain.erp_return_item.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.erp_order_item.entity.QErpOrderItemEntity;
import com.piaar_store_manager.server.domain.erp_return_item.entity.ErpReturnItemEntity;
import com.piaar_store_manager.server.domain.erp_return_item.entity.QErpReturnItemEntity;
import com.piaar_store_manager.server.domain.erp_return_item.proj.ErpReturnItemProj;
import com.piaar_store_manager.server.domain.product.entity.QProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.QProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.querydsl.core.QueryException;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
            .leftJoin(qErpOrderItemEntity).on(qErpReturnItemEntity.erpOrderItemId.eq(qErpOrderItemEntity.id))
            .leftJoin(qProductOptionEntity).on(qErpOrderItemEntity.releaseOptionCode.eq(qProductOptionEntity.code))     // 반품상태에서 출고 옵션코드로 데이터를 조회한다
            .leftJoin(qProductEntity).on(qProductOptionEntity.productCid.eq(qProductEntity.cid))
            .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());
        
        try {
            // this.sortPagedData(customQuery, pageable);
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
                .leftJoin(qErpOrderItemEntity).on(qErpReturnItemEntity.erpOrderItemId.eq(qErpOrderItemEntity.id))
                .leftJoin(qProductOptionEntity).on(qErpOrderItemEntity.releaseOptionCode.eq(qProductOptionEntity.code))
                .leftJoin(qProductEntity).on(qProductOptionEntity.productCid.eq(qProductEntity.cid))
                .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid));

        QueryResults<ErpReturnItemProj> result = customQuery.fetchResults();
        return result.getResults();
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
