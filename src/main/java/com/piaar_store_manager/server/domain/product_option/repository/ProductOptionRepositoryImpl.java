package com.piaar_store_manager.server.domain.product_option.repository;

import java.util.List;

import com.piaar_store_manager.server.domain.product.entity.QProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.QProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.product_receive.entity.QProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_release.entity.QProductReleaseEntity;
import com.piaar_store_manager.server.domain.stock_analysis.proj.StockAnalysisProj;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductOptionRepositoryImpl implements ProductOptionRepositoryCustom {
    private final JPAQueryFactory query;

    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QProductOptionEntity qProductOptionEntity = QProductOptionEntity.productOptionEntity;
    private final QProductCategoryEntity qProductCategoryEntity = QProductCategoryEntity.productCategoryEntity;
    private final QProductReleaseEntity qProductReleaseEntity = QProductReleaseEntity.productReleaseEntity;
    private final QProductReceiveEntity qProductReceiveEntity = QProductReceiveEntity.productReceiveEntity;

    @Autowired
    public ProductOptionRepositoryImpl(
        JPAQueryFactory query
    ) {
        this.query = query;
    }

    @Override
    public List<ProductOptionProj> qfindAllM2OJ() {
        JPQLQuery customQuery = query.from(qProductOptionEntity)
            .select(Projections.fields(ProductOptionProj.class,
                    qProductOptionEntity.as("productOption"),
                    qProductEntity.as("product"),
                    qProductCategoryEntity.as("productCategory")
                    ))
            .leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
            .leftJoin(qProductCategoryEntity).on(qProductCategoryEntity.cid.eq(qProductEntity.productCategoryCid));

        QueryResults<ProductOptionProj> result = customQuery.fetchResults();
        return result.getResults();
    }

    // TODO :: 리팩토링 필요
    @Override
    public List<StockAnalysisProj> qfindStockAnalysis() {
        JPQLQuery customQuery = query.from(qProductOptionEntity)
                .select(Projections.fields(StockAnalysisProj.class,
                        qProductOptionEntity.as("option"),
                        qProductEntity.as("product"),
                        qProductCategoryEntity.as("category"),
                        ExpressionUtils.as(
                            JPAExpressions.select(qProductReleaseEntity.releaseUnit.sum())
                                .from(qProductReleaseEntity)
                                .where((qProductReleaseEntity.productOptionCid).eq(qProductOptionEntity.cid)),
                            "releasedUnit"),
                        ExpressionUtils.as(
                            JPAExpressions.select(qProductReceiveEntity.receiveUnit.sum())
                                .from(qProductReceiveEntity)
                                .where((qProductReceiveEntity.productOptionCid).eq(qProductOptionEntity.cid)),
                                "receivedUnit"),
                        ExpressionUtils.as(
                            JPAExpressions.select(qProductReleaseEntity.createdAt.max())
                                .from(qProductReleaseEntity)
                                .where((qProductReleaseEntity.productOptionCid).eq(qProductOptionEntity.cid))
                                .orderBy(qProductReleaseEntity.createdAt.desc()),
                                "lastReleasedAt")))
                .leftJoin(qProductEntity).on(qProductEntity.cid.eq(qProductOptionEntity.productCid))
                .leftJoin(qProductCategoryEntity).on(qProductCategoryEntity.cid.eq(qProductEntity.productCategoryCid));

        QueryResults<StockAnalysisProj> result = customQuery.fetchResults();
        return result.getResults();
    }
}
