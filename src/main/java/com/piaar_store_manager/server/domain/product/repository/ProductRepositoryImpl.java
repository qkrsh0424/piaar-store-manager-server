package com.piaar_store_manager.server.domain.product.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product.entity.QProductEntity;
import com.piaar_store_manager.server.domain.product.proj.ProductFJProj;
import com.piaar_store_manager.server.domain.product_category.entity.QProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.piaar_store_manager.server.domain.user.entity.QUserEntity;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.piaar_store_manager.server.utils.CustomFieldUtils;
import com.querydsl.core.QueryException;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory query;

    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QProductOptionEntity qProductOptionEntity = QProductOptionEntity.productOptionEntity;
    private final QProductCategoryEntity qProductCategoryEntity = QProductCategoryEntity.productCategoryEntity;
    private final QUserEntity qUserEntity = QUserEntity.userEntity;

    @Autowired
    public ProductRepositoryImpl(
        JPAQueryFactory query
    ) {
        this.query = query;
    }

    @Override
    public List<ProductFJProj> qfindAllFJ(Map<String, Object> params) {
        // JPQLQuery customQuery = query.from(qProductEntity)
        //     // .select(Projections.fields(ProductFJProj.class,
        //     //     qProductEntity.as("product"),
        //     //     qProductOptionEntity.as("option"),
        //     //     qProductCategoryEntity.as("category"),
        //     //     qUserEntity.as("user")
        //     // ))
        //     .select(
        //         qProductEntity.cid
        //     )
        //     .where(eqStockManagement(params))
        //     .where(lkCategorySearchCondition(params), lkProductSearchCondition(params), lkOptionSearchCondition(params))
        //     .leftJoin(qUserEntity).on(qProductEntity.createdBy.eq(qUserEntity.id))
        //     .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid))
        //     .join(qProductOptionEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
        //     .groupBy(qProductEntity.cid)
        //     ;

        // List<Integer> productCids = customQuery.fetch();
        // Long totalCount = customQuery.fetchCount();

        List<ProductFJProj> projs = query.from(qProductEntity)
            .where(eqStockManagement(params))
            .where(lkCategorySearchCondition(params), lkProductSearchCondition(params), lkOptionSearchCondition(params))
            // .where(qProductEntity.cid.in(productCids))
            .leftJoin(qUserEntity).on(qProductEntity.createdBy.eq(qUserEntity.id))
            .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid))
            .leftJoin(qProductOptionEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
            .transform(
                GroupBy.groupBy(qProductEntity.cid)
                    .list(
                        Projections.fields(
                            ProductFJProj.class,
                            qProductEntity.as("product"),
                            qProductCategoryEntity.as("category"),
                            GroupBy.set(
                                qProductOptionEntity
                            ).as("options"),
                            qUserEntity.as("user")
                        )
                    )
            )
            ;

        // QueryResults<ProductFJProj> result = customQuery.fetchResults();
        // return result.getResults();
        return projs;
    }

    private BooleanExpression eqStockManagement(Map<String, Object> params) {
        Boolean stockManagement = params.get("stockManagement") == null ? null : Boolean.valueOf(params.get("stockManagement").toString());

        if (stockManagement == null) {
            return null;
        } else {
            return qProductEntity.stockManagement.eq(stockManagement);
        }
    }

    private BooleanExpression lkCategorySearchCondition(Map<String, Object> params) {
        String searchQuery = params.get("categorySearchQuery") == null ? null : params.get("categorySearchQuery").toString();

        if (searchQuery == null) {
            return null;
        }

        try {
            StringPath headerNameStringPath = CustomFieldUtils.getFieldValue(qProductCategoryEntity, "id");
            return headerNameStringPath.contains(searchQuery);
        
        } catch (ClassCastException e) {
            throw new CustomInvalidDataException("허용된 데이터 타입이 아닙니다.");
        }
    }

    private BooleanExpression lkProductSearchCondition(Map<String, Object> params) {
        String headerName = params.get("productSearchHeaderName") == null ? null : params.get("productSearchHeaderName").toString();
        String searchQuery = params.get("productSearchQuery") == null ? null : params.get("productSearchQuery").toString();

        if (headerName == null || searchQuery == null) {
            return null;
        }

        try {
            if(CustomFieldUtils.getFieldByName(qProductEntity, headerName) == null) {
                throw new QueryException("올바른 데이터가 아닙니다.");
            }
            StringPath headerNameStringPath = CustomFieldUtils.getFieldValue(qProductEntity, headerName);

            return headerNameStringPath.contains(searchQuery);
        
        } catch (ClassCastException e) {
            throw new CustomInvalidDataException("허용된 데이터 타입이 아닙니다.");
        } catch (QueryException e) {
            throw new CustomInvalidDataException(e.getMessage());
        }
    }

    private BooleanExpression lkOptionSearchCondition(Map<String, Object> params) {
        String headerName = params.get("optionSearchHeaderName") == null ? null : params.get("optionSearchHeaderName").toString();
        String searchQuery = params.get("optionSearchQuery") == null ? null : params.get("optionSearchQuery").toString();

        if (headerName == null || searchQuery == null) {
            return null;
        }

        try {
            if(CustomFieldUtils.getFieldByName(qProductOptionEntity, headerName) == null) {
                throw new QueryException("올바른 데이터가 아닙니다.");
            }
            StringPath headerNameStringPath = CustomFieldUtils.getFieldValue(qProductOptionEntity, headerName);

            return headerNameStringPath.contains(searchQuery);
        
        } catch (ClassCastException e) {
            throw new CustomInvalidDataException("허용된 데이터 타입이 아닙니다.");
        } catch (QueryException e) {
            throw new CustomInvalidDataException(e.getMessage());
        }
    }
}
