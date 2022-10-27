package com.piaar_store_manager.server.domain.product_option.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.piaar_store_manager.server.domain.product.entity.QProductEntity;
import com.piaar_store_manager.server.domain.product_category.entity.QProductCategoryEntity;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProj;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection;
import com.piaar_store_manager.server.domain.product_option.proj.ProductOptionProjection.RelatedProductReceiveAndProductRelease;
import com.piaar_store_manager.server.domain.product_receive.entity.QProductReceiveEntity;
import com.piaar_store_manager.server.domain.product_receive.proj.ProductReceiveProjection;
import com.piaar_store_manager.server.domain.product_release.entity.QProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProjection;
import com.piaar_store_manager.server.domain.stock_analysis.proj.StockAnalysisProj;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.querydsl.core.QueryResults;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /*
    재고 계산을 위한 데이터 추출.
    (옵션, 상품, 카테고리, 출고 수량, 입고 수량, 최근 출고 일)
     */
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

    @Override
    public RelatedProductReceiveAndProductRelease qSearchBatchStockStatus(List<UUID> optionIds, Map<String, Object> params) {
        List<ProductReceiveProjection.RelatedProductAndProductOption> productReceiveProjs = query.from(qProductOptionEntity)
            .where(qProductOptionEntity.id.in(optionIds), withinDateRange("receive", params))
            .orderBy(new OrderSpecifier(Order.DESC, qProductReceiveEntity.createdAt))
            .leftJoin(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
            .leftJoin(qProductReceiveEntity).on(qProductOptionEntity.id.eq(qProductReceiveEntity.productOptionId))
            .transform(
                GroupBy.groupBy(qProductReceiveEntity.cid)
                .list(    
                    Projections.fields(
                        ProductReceiveProjection.RelatedProductAndProductOption.class,
                        qProductReceiveEntity.as("productReceive"),
                        qProductEntity.as("product"),
                        qProductOptionEntity.as("productOption"))
                )
            )
        ;
        
        List<ProductReleaseProjection.RelatedProductAndProductOption> productReleaseProjs = query.from(qProductOptionEntity)
            .where(qProductOptionEntity.id.in(optionIds), withinDateRange("release", params))
            .orderBy(new OrderSpecifier(Order.DESC, qProductReleaseEntity.createdAt))
            .leftJoin(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
            .leftJoin(qProductReleaseEntity).on(qProductOptionEntity.id.eq(qProductReleaseEntity.productOptionId))
            .transform(
                GroupBy.groupBy(qProductReleaseEntity.cid)
                .list(    
                    Projections.fields(
                        ProductReleaseProjection.RelatedProductAndProductOption.class,
                        qProductReleaseEntity.as("productRelease"),
                        qProductEntity.as("product"),
                        qProductOptionEntity.as("productOption"))
                )
            )
            ;

        ProductOptionProjection.RelatedProductReceiveAndProductRelease proj = ProductOptionProjection.RelatedProductReceiveAndProductRelease.builder()
            .productReceive(productReceiveProjs)
            .productRelease(productReleaseProjs)
            .build();

        return proj;
    }

    private BooleanExpression withinDateRange(String status, Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (params.get("startDate") == null || params.get("endDate") == null) {
            return null;
        }

        startDate = LocalDateTime.parse(params.get("startDate").toString(), formatter);
        endDate = LocalDateTime.parse(params.get("endDate").toString(), formatter);

        if (startDate.isAfter(endDate)) {
            throw new CustomInvalidDataException("조회기간을 정확히 선택해 주세요.");
        }

        if (status.equals("receive")) {
            return qProductReceiveEntity.createdAt.between(startDate, endDate);
        } else if(status.equals("release")) {
            return qProductReleaseEntity.createdAt.between(startDate, endDate);
        }

        return null;
    }
}
