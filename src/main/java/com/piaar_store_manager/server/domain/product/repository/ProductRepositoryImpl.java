package com.piaar_store_manager.server.domain.product.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product.entity.QProductEntity;
import com.piaar_store_manager.server.domain.product.proj.ProductManagementProj;
import com.piaar_store_manager.server.domain.product_category.entity.QProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.piaar_store_manager.server.domain.user.entity.QUserEntity;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.piaar_store_manager.server.utils.CustomFieldUtils;
import com.querydsl.core.QueryException;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;
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
    public List<ProductManagementProj> qfindAllFJ(Map<String, Object> params) {
        List<ProductManagementProj> projs = query.from(qProductEntity)
            .where(eqStockManagement(params))
            .where(lkCategorySearchCondition(params), lkProductSearchCondition(params), lkOptionSearchCondition(params))
            .leftJoin(qUserEntity).on(qProductEntity.createdBy.eq(qUserEntity.id))
            .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid))
            .leftJoin(qProductOptionEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
            .transform(
                GroupBy.groupBy(qProductEntity.cid)
                    .list(
                        Projections.fields(
                            ProductManagementProj.class,
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

        return projs;
    }

    @Override
    public Page<ProductManagementProj> qfindAllFJByPage(Map<String, Object> params, Pageable pageable) {
        JPQLQuery customQuery = query.from(qProductEntity)
            .select(
                qProductEntity.cid
            )
            .where(eqStockManagement(params))
            .where(lkCategorySearchCondition(params), lkProductSearchCondition(params), lkOptionSearchCondition(params))
            .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid))
            .leftJoin(qProductOptionEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
            .groupBy(qProductEntity.cid)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            ;

        try {
            this.sortPagedData(customQuery, pageable);
        } catch (QueryException e) {
            throw new CustomInvalidDataException(e.getMessage());
        }

        List<Integer> productCids = customQuery.fetch();
        Long totalCount = customQuery.fetchCount();

        List<ProductManagementProj> projs = query.from(qProductEntity)
            .where(eqStockManagement(params))
            .where(lkCategorySearchCondition(params), lkProductSearchCondition(params), lkOptionSearchCondition(params))
            .where(qProductEntity.cid.in(productCids))
            .orderBy(this.orderByProductCids(productCids), qProductOptionEntity.defaultName.asc())
            .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid))
            .leftJoin(qProductOptionEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
            .transform(
                GroupBy.groupBy(qProductEntity.cid)
                    .list(
                        Projections.fields(
                            ProductManagementProj.class,
                            qProductEntity.as("product"),
                            qProductCategoryEntity.as("category"),
                            GroupBy.set(
                                qProductOptionEntity
                            ).as("options")
                        )
                    )
            )
            ;

        return new PageImpl<ProductManagementProj>(projs, pageable, totalCount);
    }

    @Override
    public ProductManagementProj qSelectProductAndOptions(UUID productId) {
        ProductManagementProj proj = query.from(qProductEntity)
            .where(qProductEntity.id.eq(productId))
            .leftJoin(qProductCategoryEntity).on(qProductEntity.productCategoryCid.eq(qProductCategoryEntity.cid))
            .leftJoin(qProductOptionEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
            .transform(
                GroupBy.groupBy(qProductEntity.cid)
                    .list(
                        Projections.fields(
                            ProductManagementProj.class,
                            qProductEntity.as("product"),
                            qProductCategoryEntity.as("category"),
                            GroupBy.set(
                                qProductOptionEntity
                            ).as("options")
                        )
                    )
            ).get(0)
            ;

        // TODO :: 예외처리

        return proj;
    }

    private OrderSpecifier<?> orderByProductCids(List<Integer> productCids) {
        // qProductEntity.cid를 정렬된 productCids 순서로 정렬한다.
        return Expressions.stringTemplate("FIELD({0}, {1})", qProductEntity.cid, productCids).asc();
    }

    private void sortPagedData(JPQLQuery customQuery, Pageable pageable) {
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder productBuilder = new PathBuilder(qProductEntity.getType(), qProductEntity.getMetadata());

            if(CustomFieldUtils.getFieldByName(qProductEntity, o.getProperty().toString()) == null) {
                throw new QueryException("올바른 데이터가 아닙니다.");
            }
            customQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, productBuilder.get(o.getProperty())));
        }
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
        UUID searchQuery = params.get("categorySearchQuery") == null ? null : UUID.fromString(params.get("categorySearchQuery").toString());

        if (searchQuery == null) {
            return null;
        }

        try {
            // StringPath headerNameStringPath = CustomFieldUtils.getFieldValue(qProductCategoryEntity, "id");
            // System.out.println(headerName);
            return qProductCategoryEntity.id.eq(searchQuery);
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
