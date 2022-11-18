package com.piaar_store_manager.server.domain.option_package.repository;

import com.piaar_store_manager.server.domain.option_package.entity.QOptionPackageEntity;
import com.piaar_store_manager.server.domain.option_package.proj.OptionPackageProjection;
import com.piaar_store_manager.server.domain.option_package.proj.OptionPackageProjection.RelatedProductOption;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class OptionPackageRepositoryImpl implements OptionPackageRepositoryCustom {
    private final JPAQueryFactory query;

    private final QOptionPackageEntity qOptionPackageEntity = QOptionPackageEntity.optionPackageEntity;
    private final QProductOptionEntity qProductOptionEntity = QProductOptionEntity.productOptionEntity;

    @Autowired
    public OptionPackageRepositoryImpl (
        JPAQueryFactory query
    ) {
        this.query = query;
    }

    @Override
    public List<OptionPackageProjection.RelatedProductOption> qfindBatchByParentOptionId(UUID parentOptionId) {
        JPQLQuery customQuery = query.from(qOptionPackageEntity)
                .select(
                        Projections.fields(OptionPackageProjection.RelatedProductOption.class,
                                qOptionPackageEntity.as("optionPackage"),
                                qProductOptionEntity.as("productOption")))
                .where(qOptionPackageEntity.parentOptionId.eq(parentOptionId))
                .leftJoin(qProductOptionEntity).on(qProductOptionEntity.id.eq(qOptionPackageEntity.originOptionId));

        QueryResults<OptionPackageProjection.RelatedProductOption> result = customQuery.fetchResults();
        return result.getResults();
    }

    @Override
    public List<RelatedProductOption> qfindBatchByParentOptionIds(List<UUID> parentOptionIds) {
        JPQLQuery customQuery = query.from(qOptionPackageEntity)
            .select(
                Projections.fields(OptionPackageProjection.RelatedProductOption.class,
                qOptionPackageEntity.as("optionPackage"),
                qProductOptionEntity.as("productOption")))
            .where(qOptionPackageEntity.parentOptionId.in(parentOptionIds))
            .leftJoin(qProductOptionEntity).on(qProductOptionEntity.id.eq(qOptionPackageEntity.originOptionId));
        
        QueryResults<OptionPackageProjection.RelatedProductOption> result = customQuery.fetchResults();
        return result.getResults();
    }

    
}
