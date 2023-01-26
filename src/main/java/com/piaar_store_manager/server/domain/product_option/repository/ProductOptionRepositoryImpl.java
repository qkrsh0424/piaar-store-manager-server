package com.piaar_store_manager.server.domain.product_option.repository;

import java.util.List;

import com.piaar_store_manager.server.domain.product.entity.QProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.QProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection.RelatedProduct;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection.RelatedProductAndProductCategory;
import com.piaar_store_manager.server.domain.product_receive.entity.QProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_release.entity.QProductReleaseEntity;
import com.piaar_store_manager.server.domain.stock_analysis.proj.StockAnalysisProj;
import com.querydsl.core.QueryResults;
import com.querydsl.core.group.GroupBy;
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
    public List<RelatedProduct> qfindAllRelatedProduct() {
        List<RelatedProduct> projs = query.from(qProductOptionEntity)
            .leftJoin(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
            .transform(
                GroupBy.groupBy(qProductOptionEntity.cid)
                    .list(
                        Projections.fields(
                            ProductOptionProjection.RelatedProduct.class,
                            qProductOptionEntity.as("option"),
                            qProductEntity.as("product") 
                        )
                    )
            );
            
        return projs;
    }

    @Override
    public List<RelatedProductAndProductCategory> qfindAllRelatedProductAndProductCategory() {
        List<RelatedProductAndProductCategory> projs = query.from(qProductOptionEntity)
            .leftJoin(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
            .leftJoin(qProductCategoryEntity).on(qProductCategoryEntity.cid.eq(qProductEntity.productCategoryCid))
            .transform(
                GroupBy.groupBy(qProductOptionEntity.cid)
                    .list(
                        Projections.fields(
                            ProductOptionProjection.RelatedProductAndProductCategory.class,
                            qProductOptionEntity.as("option"),
                            qProductEntity.as("product"),
                            qProductCategoryEntity.as("productCategory")
                        )
                    )
            );

        return projs;
    }

    /*
    재고 계산을 위한 데이터 추출.
    (옵션, 상품, 카테고리, 출고 수량, 입고 수량, 최근 출고 일)
     */
    // TODO :: REFACTOR
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
                                .where((qProductReleaseEntity.productOptionCid).eq(qProductOptionEntity.cid))
                            ,"releasedUnit"),
                        ExpressionUtils.as(
                            JPAExpressions.select(qProductReceiveEntity.receiveUnit.sum())
                                .from(qProductReceiveEntity)
                                .where((qProductReceiveEntity.productOptionCid).eq(qProductOptionEntity.cid))
                                ,"receivedUnit"),
                        ExpressionUtils.as(
                            JPAExpressions.select(qProductReleaseEntity.createdAt.max())
                                .from(qProductReleaseEntity)
                                .where((qProductReleaseEntity.productOptionCid).eq(qProductOptionEntity.cid))
                                ,"lastReleasedAt")))
                .leftJoin(qProductEntity).on((qProductEntity.cid).eq(qProductOptionEntity.productCid))
                .leftJoin(qProductCategoryEntity).on((qProductCategoryEntity.cid).eq(qProductEntity.productCategoryCid));

        QueryResults<StockAnalysisProj> result = customQuery.fetchResults();
        return result.getResults();
    }
}
