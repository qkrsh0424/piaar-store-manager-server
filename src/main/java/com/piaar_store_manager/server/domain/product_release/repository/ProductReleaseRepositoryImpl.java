package com.piaar_store_manager.server.domain.product_release.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product.entity.QProductEntity;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.piaar_store_manager.server.domain.product_release.entity.QProductReleaseEntity;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProjection;
import com.piaar_store_manager.server.domain.product_release.proj.ProductReleaseProjection.RelatedProductAndProductOption;
import com.piaar_store_manager.server.exception.CustomInvalidDataException;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ProductReleaseRepositoryImpl implements ProductReleaseRepositoryCustom {
    private final JPAQueryFactory query;
    
    private final QProductEntity qProductEntity = QProductEntity.productEntity;
    private final QProductOptionEntity qProductOptionEntity = QProductOptionEntity.productOptionEntity;
    private final QProductReleaseEntity qProductReleaseEntity = QProductReleaseEntity.productReleaseEntity;

    @Autowired
    public ProductReleaseRepositoryImpl(
        JPAQueryFactory query
    ) {
        this.query = query;
    }

    @Override
    public List<RelatedProductAndProductOption> qSearchBatchByOptionIds(List<UUID> optionIds, Map<String, Object> params) {
        List<ProductReleaseProjection.RelatedProductAndProductOption> projs = query.from(qProductOptionEntity)
        .where(qProductOptionEntity.id.in(optionIds), withinDateRange(params))
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

        return projs;
    }

    private BooleanExpression withinDateRange(Map<String, Object> params) {
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

        return qProductReleaseEntity.createdAt.between(startDate, endDate);
    }
}
