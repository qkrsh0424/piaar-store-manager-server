package com.piaar_store_manager.server.domain.sub_option_code.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.piaar_store_manager.server.domain.sub_option_code.entity.QSubOptionCodeEntity;
import com.piaar_store_manager.server.domain.sub_option_code.proj.SubOptionCodeProjection;
import com.piaar_store_manager.server.domain.sub_option_code.proj.SubOptionCodeProjection.RelatedProductOption;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;

@Repository
public class SubOptionCodeRepositoryImpl implements SubOptionCodeRepositoryCustom {
    private final JPQLQueryFactory query;

    private final QSubOptionCodeEntity qSubOptionCodeEntity = QSubOptionCodeEntity.subOptionCodeEntity;
    private final QProductOptionEntity qProductOptionEntity = QProductOptionEntity.productOptionEntity;

    @Autowired
    public SubOptionCodeRepositoryImpl (
        JPQLQueryFactory query
    ) {
        this.query = query;
    }

    @Override
    public List<RelatedProductOption> qSearchAll() {
        List<SubOptionCodeProjection.RelatedProductOption> projs = query.from(qSubOptionCodeEntity)
        .leftJoin(qProductOptionEntity).on(qProductOptionEntity.id.eq(qSubOptionCodeEntity.productOptionId))
        .transform(
            GroupBy.groupBy(qSubOptionCodeEntity.cid)
            .list(
                Projections.fields(
                    SubOptionCodeProjection.RelatedProductOption.class,
                    qSubOptionCodeEntity.as("subOptionCode"),
                    qProductOptionEntity.as("productOption"))
            )
        )
        ;
        return projs;
    }
}
