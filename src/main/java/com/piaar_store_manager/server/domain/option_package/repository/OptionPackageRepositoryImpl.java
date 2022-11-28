package com.piaar_store_manager.server.domain.option_package.repository;

import com.piaar_store_manager.server.domain.option_package.entity.QOptionPackageEntity;
import com.piaar_store_manager.server.domain.option_package.proj.OptionPackageProjection;
import com.piaar_store_manager.server.domain.product.entity.QProductEntity;
import com.piaar_store_manager.server.domain.product_option.entity.QProductOptionEntity;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
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
    private final QProductEntity qProductEntity = QProductEntity.productEntity;

    @Autowired
    public OptionPackageRepositoryImpl (
        JPAQueryFactory query
    ) {
        this.query = query;
    }

    @Override
    public List<OptionPackageProjection.RelatedProductAndOption> qfindBatchByParentOptionId(UUID parentOptionId) {
        List<OptionPackageProjection.RelatedProductAndOption> projs = query.from(qOptionPackageEntity)
            .where(qOptionPackageEntity.parentOptionId.eq(parentOptionId))
            .leftJoin(qProductOptionEntity).on(qProductOptionEntity.id.eq(qOptionPackageEntity.originOptionId))
            .leftJoin(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
            .transform(
                GroupBy.groupBy(qOptionPackageEntity.cid)
                    .list(
                        Projections.fields(
                            OptionPackageProjection.RelatedProductAndOption.class, 
                            qOptionPackageEntity.as("optionPackage"),
                            qProductOptionEntity.as("productOption"),
                            qProductEntity.as("product")
                        )
                    )
            )
            ;

        return projs;
    }

    @Override
    public List<OptionPackageProjection.RelatedProductAndOption> qfindBatchByParentOptionIds(List<UUID> parentOptionIds) {
        List<OptionPackageProjection.RelatedProductAndOption> projs = query.from(qOptionPackageEntity)
            .where(qOptionPackageEntity.parentOptionId.in(parentOptionIds))
            .leftJoin(qProductOptionEntity).on(qProductOptionEntity.id.eq(qOptionPackageEntity.originOptionId))
            .leftJoin(qProductEntity).on(qProductEntity.id.eq(qProductOptionEntity.productId))
            .transform(
                GroupBy.groupBy(qOptionPackageEntity.cid)
                    .list(
                        Projections.fields(
                            OptionPackageProjection.RelatedProductAndOption.class, 
                            qOptionPackageEntity.as("optionPackage"),
                            qProductOptionEntity.as("productOption"),
                            qProductEntity.as("product")
                        )
                    )
            );

        return projs;
    }   
}
